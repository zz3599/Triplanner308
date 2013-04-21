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
    private static final String CREATEEVENT = "Insert into events (tripdayid, starttime, endtime, eventtype, comment)"
            + " values (?, ?, ?, ?, ?)";
    private static final String UPDATEEVENT = "Update events set starttime=?, endtime=?, eventtype=?, comment=? "
            + "where id=?";
    private static final String SELECTEVENTS = "Select * from events where tripdayid=?";
    private static final String DELETEEVENT = "Delete from events where id=?";
    
    public  static List<Event> selectAllEvents(Tripday day){
        try {
            Connection connection = DB.getConnection();
            PreparedStatement ps = connection.prepareStatement(SELECTEVENTS);
            ps.setInt(1, day.id);
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
    
    public static Event createEvent(Tripday tripday, Timestamp start, Timestamp end, int eventType, String comment){
        try {
            Connection connection = DB.getConnection();
            PreparedStatement ps = connection.prepareStatement(CREATEEVENT, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, tripday.id);
            ps.setTimestamp(2, start);
            ps.setTimestamp(3, end);
            ps.setInt(4, eventType);
            ps.setString(5, comment);
            int result = ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()){
               return new Event(rs.getInt(1), tripday.id, start, end, eventType, comment);
            }             
        } catch(Exception e){
            e.printStackTrace();
        }
        return null;        
    }
    
    public static Event updateEvent(Event event, Timestamp start, Timestamp end, int eventType, String comment){
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
               return new Event(event.id, event.tripdayid, start, end, eventType, comment);
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
        int tripdayid = rs.getInt("tripdayid");
        Timestamp startTime = rs.getTimestamp("starttime");
        Timestamp endTime = rs.getTimestamp("endtime");
        int eventType = rs.getInt("eventtype");
        String comment = rs.getString("comment");
        return new Event(id, tripdayid, startTime, endTime, eventType, comment);
    }

    
    
    
}
