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
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author brook
 */
public class PhotoDAO implements Serializable {
    public static final String UPLOADPHOTO = "Insert into photos (url, userid, tripid, tripdayid, eventid) values"
            + "(?, ?, ?, ?, ?)";
    public static final String EVENTPHOTOS = "Select * from photos where eventid=?";
    public static final String DAYPHOTOS = "Select * from photos where tripdayid=?";
    public static final String TRIPPHOTOS = "Select * from photos where tripid=?";
    public static final String USERPHOTOS = "Select * from photos where userid=?";  
    
    
    
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
        int tripid = rs.getInt("tripid");
        int tripdayid = rs.getInt("tripdayid");
        int eventid = rs.getInt("eventid");
        String comment = rs.getString("comment");
        return new Photo(id, url, userid, tripid, tripdayid, eventid, comment);        
    }

    
    
}
