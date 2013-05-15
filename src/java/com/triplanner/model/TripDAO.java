/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.triplanner.model;

import com.triplanner.entities.Trip;
import com.triplanner.entities.User;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages all database calls to the trips database
 * @author brook
 */

public class TripDAO implements Serializable {
    private static final String GETBYID = "Select * from trips where id=?";
    private static final String USERTRIPS = "Select * from trips where userid=?";
    private static final String CREATETRIP = "Insert into trips "
            + "(userid, title, description, starttime, endtime, startlocation, endlocation) "
            + "values (?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATETRIP = "Update trips set "
            + "title=?, description=?, starttime=?, endtime=?, startlocation=?, endlocation=? "
            + "where id=?";    
    private static final String SEARCHTRIP = "Select * from trips where title like ?";
    private static final String UPDATESHARE = "Update trips set shared=? where id=?";
    
    public static boolean updateShareTrip(int tripid, int shareval){
        try {
            Connection connection = DB.getConnection();
            PreparedStatement ps = connection.prepareStatement(UPDATESHARE);
            ps.setInt(1, shareval);
            ps.setInt(2, tripid);
            int r = ps.executeUpdate();
            if(r == 1){
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static List<Trip> searchTrip(String search){
        try {
            Connection connection = DB.getConnection();
            PreparedStatement ps = connection.prepareStatement(SEARCHTRIP);
            ps.setString(1, "%" + search + "%");
            List<Trip> trips = new ArrayList<Trip>();
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                trips.add(extractTrip(rs));
            }
            return trips;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static Trip getTrip(int tripid){
        try {
            Connection connection = DB.getConnection();
            PreparedStatement ps = connection.prepareStatement(GETBYID);
            ps.setInt(1, tripid);
            ResultSet rs = ps.executeQuery();
            if(rs.first()){
                return extractTrip(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static Trip createTrip(User user, String title, String description, Timestamp starttime, 
            Timestamp endtime, String startlocation, String endlocation, boolean shared){
        try {
            Connection connection = DB.getConnection();
            PreparedStatement ps = connection.prepareStatement(CREATETRIP, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, user.id);
            ps.setString(2, title);
            ps.setString(3, description);
            ps.setTimestamp(4, starttime);
            ps.setTimestamp(5, endtime);
            //Store number of days?
            ps.setString(6, startlocation);
            ps.setString(7, endlocation);
            //ps.setBoolean(8, shared);
            int result = ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()){
                return new Trip(rs.getInt(1), user.id, title, description, starttime, endtime, startlocation, 
                        endlocation, shared);
            }            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static Trip updateTrip(User user, String title, String description, Timestamp starttime, 
            Timestamp endtime, String startlocation, String endlocation, boolean shared, int tripid){
        try {
            Connection connection = DB.getConnection();
            PreparedStatement ps = connection.prepareStatement(UPDATETRIP);
            ps.setString(1, title);
            ps.setString(2, description);
            ps.setTimestamp(3, starttime);
            ps.setTimestamp(4, endtime);
            ps.setString(5, startlocation);
            ps.setString(6, endlocation);
            ps.setInt(7, tripid);
            int result = ps.executeUpdate();
            if(result == 1){
                return new Trip(tripid, user.id, title, description, starttime, endtime, startlocation, 
                        endlocation, shared);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static List<Trip> getUserTrips(User user) {
        try {
            Connection connection = DB.getConnection();
            PreparedStatement ps = connection.prepareStatement(USERTRIPS);
            ps.setInt(1, user.id);
            ResultSet rs = ps.executeQuery();
            List<Trip> result = new ArrayList<Trip>();
            while (rs.next()) {
                result.add(extractTrip(rs));
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Trip extractTrip(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        int userid = rs.getInt("userid");
        String title = rs.getString("title");
        String description = rs.getString("description");
        Timestamp startTime = rs.getTimestamp("starttime");
        Timestamp endTime = rs.getTimestamp("endtime");
        String startLocation = rs.getString("startlocation");
        String endLocation = rs.getString("endlocation");
        boolean shared = rs.getBoolean("shared");
        return new Trip(id, userid, title, description, startTime, endTime, startLocation, endLocation, shared);
    }      
}
