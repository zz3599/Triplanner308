/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.triplanner.model;

import com.triplanner.entities.Photo;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author brook
 */
public class PhotoDAO implements Serializable {
    public static final String UPLOADPHOTO = "Insert into photos "
            + "(url, userid, tripid, tripdayid, eventid, comment, originalname, uploadtime) values"
            + "(?, ?, ?, ?, ?, ?, ?, ?)";
    public static final String EVENTPHOTOS = "Select * from photos where eventid=? order by uploadtime";
    public static final String DAYPHOTOS = "Select * from photos where tripdayid=? order by uploadtime";
    public static final String TRIPPHOTOS = "Select * from photos where tripid=? order by uploadtime";
    public static final String USERPHOTOS = "Select * from photos where userid=? order by uploadtime";  
    
    public static Photo uploadPhoto(String url, int userid, int tripid, Integer tripdayid, Integer eventid, 
            String comment, String originalName){
        Timestamp now = new Timestamp(new Date().getTime());
        try {
            Connection con = DB.getConnection();
            PreparedStatement ps = con.prepareStatement(UPLOADPHOTO, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, url);
            ps.setInt(2, userid);
            ps.setInt(3, tripid);
            if(tripdayid != null) ps.setInt(4, tripdayid);
                else ps.setNull(4, Types.INTEGER);
            if(eventid != null) ps.setInt(5, eventid);
                else ps.setNull(5, Types.INTEGER);
            
            ps.setString(6, comment);
            ps.setString(7, originalName);
            ps.setTimestamp(8, now);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if(rs.first()) {
                return new Photo(rs.getInt(1), url, userid, tripid, tripdayid, eventid, comment, originalName, now);
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
    
    public static List<Photo> getEventPhotos(int eventid){
        try {
            Connection con = DB.getConnection();
            PreparedStatement ps = con.prepareStatement(EVENTPHOTOS);
            ps.setInt(1, eventid);
            ResultSet rs = ps.executeQuery();
            List<Photo> photos = new ArrayList<Photo>();
            while(rs.next()){
                photos.add(extractPhoto(rs));
            }
            return photos;
        } catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
    
    public static List<Photo> getDayPhotos(int tripdayid){
        try {
            Connection con = DB.getConnection();
            PreparedStatement ps = con.prepareStatement(DAYPHOTOS);
            ps.setInt(1, tripdayid);
            ResultSet rs = ps.executeQuery();
            List<Photo> photos = new ArrayList<Photo>();
            while(rs.next()){
                photos.add(extractPhoto(rs));
            }
            return photos;
        } catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
    
    public static List<Photo> getTripPhotos(int tripid){
        try {
            Connection con = DB.getConnection();
            PreparedStatement ps = con.prepareStatement(TRIPPHOTOS);
            ps.setInt(1, tripid);
            ResultSet rs = ps.executeQuery();
            List<Photo> photos = new ArrayList<Photo>();
            while(rs.next()){
                photos.add(extractPhoto(rs));
            }
            return photos;
        } catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
    
    public static Photo extractPhoto(ResultSet rs) throws SQLException{
        int id = rs.getInt("id");
        String url = rs.getString("url");
        int userid = rs.getInt("userid");
        Integer tripid = rs.getInt("tripid");
        Integer tripdayid = rs.getInt("tripdayid");
        int eventid = rs.getInt("eventid");
        String comment = rs.getString("comment");
        String originalName = rs.getString("originalname");
        Timestamp uploadtime = rs.getTimestamp("uploadtime");
        
        return new Photo(id, url, userid, tripid, tripdayid, eventid, comment, originalName, uploadtime);        
    }

    
    
}
