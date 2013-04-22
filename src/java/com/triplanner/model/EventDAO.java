/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.triplanner.model;

import com.triplanner.entities.Event;
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

/**
 *
 * @author brook
 */
public class EventDAO implements Serializable{
    private static final String CREATEEVENT = "Insert into events (tripid, tripdayid, starttime, endtime, eventtype, comment, startlocation, endlocation)"
            + " values (?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATEEVENT = "Update events set starttime=?, endtime=?, eventtype=?, comment=? "
            + "where id=?";
    private static final String SELECTEVENTSBYDAY = "Select * from events where tripdayid=?";
    private static final String SELECTEVENTSBYTRIP  = "Select * from events where tripid=?";
    private static final String DELETEEVENT = "Delete from events where id=?";
    
    public  static List<Event> selectAllEventsByDay(int dayid){
        try {
            Connection connection = DB.getConnection();
            PreparedStatement ps = connection.prepareStatement(SELECTEVENTSBYDAY);
            ps.setInt(1, dayid);
            ResultSet rs = ps.executeQuery();
            List<Event> events = new ArrayList<Event>();
            while(rs.next()){
               events.add(extractEvent(rs));
            }             
            return events;
        } catch(Exception e){
            e.printStackTrace();
        }
        return null;        
    }
    
    public  static List<Event> selectAllEventsByTrip(int tripid){
        try {
            Connection connection = DB.getConnection();
            PreparedStatement ps = connection.prepareStatement(SELECTEVENTSBYTRIP);
            ps.setInt(1, tripid);
            ResultSet rs = ps.executeQuery();
            List<Event> events = new ArrayList<Event>();
            while(rs.next()){
               events.add(extractEvent(rs));
            }             
            return events;
        } catch(Exception e){
            e.printStackTrace();
        }
        return null;        
    }
    
    public static Event createEvent(int tripid, int tripdayid, Timestamp start, Timestamp end, int eventType, String comment,
            String startLocation, String endLocation){
        try {
            Connection connection = DB.getConnection();
            PreparedStatement ps = connection.prepareStatement(CREATEEVENT, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, tripid);
            ps.setInt(2, tripdayid);
            ps.setTimestamp(3, start);
            ps.setTimestamp(4, end);
            ps.setInt(5, eventType);
            ps.setString(6, comment);
            ps.setString(7, startLocation);
            ps.setString(8, endLocation);
            int result = ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()){
               return new Event(rs.getInt(1), tripid, tripdayid, start, end, startLocation, endLocation, eventType, comment);
            }             
        } catch(Exception e){
            e.printStackTrace();
        }
        return null;        
    }
    
    public static Event updateEvent(Event event, Timestamp start, Timestamp end, String startLocation, String endLocation, 
            int eventType, String comment){
        try {
            Connection connection = DB.getConnection();
            PreparedStatement ps = connection.prepareStatement(UPDATEEVENT);           
            ps.setTimestamp(1, start);
            ps.setTimestamp(2, end);
            ps.setInt(3, eventType);
            ps.setString(4, comment);
            ps.setInt(5, event.id);
            int result = ps.executeUpdate();
            if(result == 1){
               return new Event(event.id, event.tripid, event.tripdayid, start, end, startLocation, endLocation, eventType, comment);
            }             
        } catch(Exception e){
            e.printStackTrace();
        }
        return null;        
    }
    
    public static boolean deleteEvent(int eventid){
        try {
            Connection connection = DB.getConnection();
            PreparedStatement ps = connection.prepareStatement(DELETEEVENT); 
            ps.setInt(1, eventid);
            int result = ps.executeUpdate();
            if(result == 1){
               return true;
            }             
        } catch(Exception e){
            e.printStackTrace();
        }
        return false;     
    }
    
    public static Event extractEvent(ResultSet rs) throws SQLException{
        int id = rs.getInt("id");
        int tripid  = rs.getInt("tripid");
        int tripdayid = rs.getInt("tripdayid");
        Timestamp startTime = rs.getTimestamp("starttime");
        Timestamp endTime = rs.getTimestamp("endtime");
        String startLocation = rs.getString("startlocation");
        String endLocation = rs.getString("endlocation");
        int eventType = rs.getInt("eventtype");
        String comment = rs.getString("comment");
        return new Event(id, tripid, tripdayid, startTime, endTime, startLocation, endLocation, eventType, comment);
    }

    
    
    
}
