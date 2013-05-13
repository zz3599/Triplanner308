/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.triplanner.model;

import com.triplanner.entities.Trip;
import com.triplanner.entities.Tripday;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import com.triplanner.utils.DateUtils;

/**
 *
 * @author brook
 */
public class TripdayDAO implements Serializable {

    private static final String CREATEDAY = "INSERT into tripdays(tripid, date, startlocation, endlocation, comment, daynum)"
            + "values (?, ?, ?, ?, ?, ?)";
    private static final String DELETEDAY = "Delete from tripdays where tripid=? and daynum=?";
    private static final String UPDATEDAY = "Update tripdays set "
            + "date=?, startlocation=?, endlocation=?, comment=?, daynum=? "
            + "where id=?";
    private static final String UPDATETIME = "Update tripdays set "
            + "date=? where tripid=? and daynum=?";
    private static final String ALLTRIPDAYS = "SELECT * from tripdays where tripid=? ORDER BY daynum";
    private static final String GETDAY = "SELECT * from tripdays where tripid=? and date=?";

    public static Tripday getDay(int tripid, Timestamp date) {
        try {
            Connection connection = DB.getConnection();
            PreparedStatement ps = connection.prepareStatement(GETDAY);
            ps.setInt(1, tripid);
            ps.setTimestamp(2, date);
            ResultSet rs = ps.executeQuery();
            if (rs.first()) {
                return extractTripday(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean createDaysForTrip(Trip trip) {
        Timestamp tripstart = trip.startTime;
        int numdays = DateUtils.diffDays(tripstart, trip.endTime);
        try {
            Connection connection = DB.getConnection();
            PreparedStatement ps = connection.prepareStatement(CREATEDAY);
            for (int i = 0; i <= numdays; i++) {
                ps.setInt(1, trip.id);
                ps.setTimestamp(2, tripstart);
                ps.setString(3, trip.startLocation);
                ps.setString(4, null);
                ps.setString(5, null);
                ps.setInt(6, i);
                ps.addBatch();
                tripstart = DateUtils.incrementDay(tripstart, 1);
            }
            connection.setAutoCommit(false);
            ps.executeBatch();
            connection.commit();
            connection.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Tripday createTripday(Trip trip, Timestamp date, String start, String end, String comment, int daynum) {
        try {
            Connection connection = DB.getConnection();
            PreparedStatement ps = connection.prepareStatement(CREATEDAY, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, trip.id);
            ps.setTimestamp(2, date);
            ps.setString(3, start);
            ps.setString(4, end);
            ps.setString(5, comment);
            ps.setInt(6, daynum);
            int result = ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.first()) {
                return new Tripday(rs.getInt(1), trip.id, date, start, end, comment, daynum);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean updateTripday(Trip trip) {
        try {
            Connection connection = DB.getConnection();
            PreparedStatement ps = connection.prepareStatement(ALLTRIPDAYS);
            ps.setInt(1, trip.id);
            ResultSet rs = ps.executeQuery();
            List<Tripday> tripdays = new ArrayList<Tripday>();
            while (rs.next()) {
                tripdays.add(extractTripday(rs));
            }
            ps.close();
            //update timestamps of all trip days
            ps = connection.prepareStatement(UPDATETIME);
            Timestamp start = trip.startTime;
            int newTriplength = DateUtils.diffDays(start, trip.endTime) + 1;
            int oldTriplength = tripdays.size();
            for (int i = 0; i < oldTriplength; i++) {
                int tripdaynum = tripdays.get(i).daynum;
                ps.setTimestamp(1, start);
                ps.setInt(2, trip.id);
                ps.setInt(3, tripdaynum);
                ps.addBatch();
                start = DateUtils.incrementDay(start, 1);
            }
            connection.setAutoCommit(false);
            ps.executeBatch();
            connection.commit();
            ps.close();
            //now add if new trip length is longer, or delete if shorter
            if (newTriplength != oldTriplength) {
                if (newTriplength > oldTriplength) {
                    //if new trip length is longer, add extra days
                    ps = connection.prepareStatement(CREATEDAY);
                    for (int i = oldTriplength; i < newTriplength; i++) {
                        ps.setInt(1, trip.id);
                        ps.setTimestamp(2, start);
                        ps.setString(3, trip.startLocation);
                        ps.setString(4, null);
                        ps.setString(5, null);
                        ps.setInt(6, i);
                        ps.addBatch();
                        start = DateUtils.incrementDay(start, 1);
                    }
                } else {
                    //if new trip length is shorter, delete extra days
                    ps = connection.prepareStatement(DELETEDAY);
                    for(int i = newTriplength; i < oldTriplength; i++){
                        ps.setInt(1, trip.id);
                        ps.setInt(2, i);
                        ps.addBatch();
                    }
                }
                connection.setAutoCommit(false);
                ps.executeBatch();
                connection.commit();
            }
            connection.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static List<Tripday> getAllTripDays(int tripid) {
        try {
            Connection connection = DB.getConnection();
            PreparedStatement ps = connection.prepareStatement(ALLTRIPDAYS);
            ps.setInt(1, tripid);
            ResultSet rs = ps.executeQuery();
            List<Tripday> tripdays = new ArrayList<Tripday>();
            while (rs.next()) {
                tripdays.add(extractTripday(rs));
            }
            return tripdays;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Tripday extractTripday(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        int tripid = rs.getInt("tripid");
        Timestamp date = rs.getTimestamp("date");
        String startLocation = rs.getString("startLocation");
        String endLocation = rs.getString("endLocation");
        String comment = rs.getString("comment");
        int daynum = rs.getInt("daynum");
        return new Tripday(id, tripid, date, startLocation, endLocation, comment, daynum);
    }
}
