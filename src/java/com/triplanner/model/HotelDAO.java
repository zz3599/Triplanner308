/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.triplanner.model;

import com.triplanner.entities.Hotel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author brook
 */
public class HotelDAO {
    private static final String CREATEHOTEL = "insert into hotels (tripid, tripdayid, location) values " 
            + "(?, ?, ?)";
    private static final String SELECTHOTELBYDAY = "select * from hotels where tripid=? and tripdayid=?";
    private static final String SELECTHOTELBYTRIP = "select * from hotels where tripid=?";
    
    public static List<Hotel> getAllHotelsByDay(int tripid, int tripdayid){
        try {
            Connection connection = DB.getConnection();
            PreparedStatement ps = connection.prepareStatement(SELECTHOTELBYDAY);
            ps.setInt(1, tripid);
            ps.setInt(2, tripdayid);
            ResultSet rs = ps.executeQuery();
            List<Hotel> hotels = new ArrayList<Hotel>();
            while(rs.next()){
               hotels.add(extractHotel(rs));
            }             
            return hotels;
        } catch(Exception e){
            e.printStackTrace();
        }
        return null;        
    }
    
    public static List<Hotel> getAllHotelsByTrip(int tripid){
        try {
            Connection connection = DB.getConnection();
            PreparedStatement ps = connection.prepareStatement(SELECTHOTELBYTRIP);
            ps.setInt(1, tripid);
            ResultSet rs = ps.executeQuery();
            List<Hotel> hotels = new ArrayList<Hotel>();
            while(rs.next()){
               hotels.add(extractHotel(rs));
            }             
            return hotels;
        } catch(Exception e){
            e.printStackTrace();
        }
        return null;        
    }
    
    public static Hotel createHotel(int tripid, int tripdayid, String location){
        try {
            Connection connection = DB.getConnection();
            PreparedStatement ps = connection.prepareStatement(CREATEHOTEL, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, tripid);
            ps.setInt(2, tripdayid);
            ps.setString(3, location);
            int result = ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();         
            if(rs.next()){
                return new Hotel(rs.getInt(1), tripid, tripdayid, location);
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
    
    public static Hotel extractHotel(ResultSet rs) throws SQLException{
        int id = rs.getInt("id");
        int tripid = rs.getInt("tripid");
        int tripdayid = rs.getInt("tripdayid");
        String location = rs.getString("location");
        return new Hotel(id, tripid, tripdayid, location);
    }
    
}
