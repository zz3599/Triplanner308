/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.triplanner.model;

import com.triplanner.entities.Waypoint;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Brook
 */
public class WaypointDAO {

    private static final String SELECTBYDAY = "Select * from waypoints where tripid=? and tripdayid=? order by pointnum ASC";
    private static final String SELECTBYTRIP = "Select * from waypoints where tripid=? order by pointnum ASC";
    private static final String CREATEWAYPOINT = "Insert into waypoints (tripid, tripdayid, location, pointnum) "
            + "values(?, ?, ?, ?)";
    private static final String GETCOUNTBYDAY = "Select count(*) from waypoints where tripid=? and tripdayid=?";
    private static final String UPDATE = "Update waypoints set location=? where tripid=? and tripdayid=? and pointnum=?";
    private static final String DELETE = "Delete from waypoints where tripid=? and tripdayid=? and pointnum=?";

    public static boolean updateAll(String[] locations, int tripid, int tripdayid, int prevTotal) {
        try {
            Connection connection = DB.getConnection();
            PreparedStatement ps = null;
            int prevwaypoints;
            if (locations != null) {
                prevwaypoints = locations.length;
                ps = connection.prepareStatement(UPDATE);
                for (int locationnum = 0; locationnum < locations.length; locationnum++) {
                    String location = locations[locationnum];
                    ps.setString(1, location);
                    ps.setInt(2, tripid);
                    ps.setInt(3, tripdayid);
                    ps.setInt(4, locationnum);
                    ps.addBatch();
                }
                connection.setAutoCommit(false);
                ps.executeBatch();
                connection.commit();
                ps.close();
            } else {
                prevwaypoints = 0;
            }
            //delete if there are fewer waypoints than before, add if greater
            if (prevwaypoints != prevTotal) {
                if (prevwaypoints < prevTotal) {
                    ps = connection.prepareStatement(DELETE);
                    for (int locationnum = prevwaypoints; locationnum < prevTotal; locationnum++) {
                        ps.setInt(1, tripid);
                        ps.setInt(2, tripdayid);
                        ps.setInt(3, locationnum);
                        ps.addBatch();
                    }
                } else if (prevwaypoints > prevTotal) {
                    ps = connection.prepareStatement(CREATEWAYPOINT);
                    for (int locationnum = prevTotal; locationnum < prevwaypoints; locationnum++) {
                        ps.setInt(1, tripid);
                        ps.setInt(2, tripdayid);
                        ps.setString(3, locations[locationnum]);
                        ps.setInt(4, locationnum);
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

    public static Waypoint createWaypoint(int tripid, int tripdayid, String location, int pointnum) {
        try {
            Connection connection = DB.getConnection();
            PreparedStatement ps = connection.prepareStatement(CREATEWAYPOINT, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, tripid);
            ps.setInt(2, tripdayid);
            ps.setString(3, location);
            ps.setInt(4, pointnum);
            int result = ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return new Waypoint(rs.getInt(1), tripid, tripdayid, location, pointnum);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Waypoint> getWaypointsByDay(int tripid, int tripdayid) {
        try {
            Connection connection = DB.getConnection();
            PreparedStatement ps = connection.prepareStatement(SELECTBYDAY);
            ps.setInt(1, tripid);
            ps.setInt(2, tripdayid);
            ResultSet rs = ps.executeQuery();
            List<Waypoint> waypoints = new ArrayList<Waypoint>();
            while (rs.next()) {
                waypoints.add(extractWaypoint(rs));
            }
            return waypoints;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int getWaypointsCountByDay(int tripid, int tripdayid) {
        try {
            Connection connection = DB.getConnection();
            PreparedStatement ps = connection.prepareStatement(GETCOUNTBYDAY);
            ps.setInt(1, tripid);
            ps.setInt(2, tripdayid);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static List<Waypoint> getWaypointsByTrip(int tripid) {
        try {
            Connection connection = DB.getConnection();
            PreparedStatement ps = connection.prepareStatement(SELECTBYTRIP);
            ps.setInt(1, tripid);
            ResultSet rs = ps.executeQuery();
            List<Waypoint> waypoints = new ArrayList<Waypoint>();
            while (rs.next()) {
                waypoints.add(extractWaypoint(rs));
            }
            return waypoints;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Waypoint extractWaypoint(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        int tripid = rs.getInt("tripid");
        int tripdayid = rs.getInt("tripdayid");
        String location = rs.getString("location");
        int pointnum = rs.getInt("pointnum");
        return new Waypoint(id, tripid, tripdayid, location, pointnum);
    }
}
