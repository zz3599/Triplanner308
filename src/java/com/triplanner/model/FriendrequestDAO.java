/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.triplanner.model;

import com.triplanner.entities.Friendrequest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * User 1 - the requester, user2 - the receiver of the request
 * @author brook
 */
public class FriendrequestDAO {
    private static final String ADDREQUEST = "Insert into friendrequests (user1, user2, status) "
            + "values (?, ?, 0)";
    private static final String DELETEREQUEST = "Delete from friendrequests where id=?";
    private static final String ADDFRIEND = "Insert into friends (friend1, friend2) values (?, ?)";
    private static final String GETALLREQUESTS = "Select * from friendrequests where user2=?";
    
    public static Friendrequest createFriendrequest(int user1, int user2){
        try {
            Connection connection = DB.getConnection();
            PreparedStatement ps = connection.prepareStatement(ADDREQUEST, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, user1);
            ps.setInt(2, user2);
            int result = ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();         
            if(rs.next()){
                return new Friendrequest(rs.getInt(1), user1, user2, 0);
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
    
    public static List<Friendrequest> getAllFriendrequests(int userid){
        try {
            Connection connection = DB.getConnection();
            PreparedStatement ps = connection.prepareStatement(GETALLREQUESTS);
            ps.setInt(1, userid);
            List<Friendrequest> requests = new ArrayList<Friendrequest>();
            ResultSet rs = ps.executeQuery();         
            while(rs.next()){
                requests.add(extract(rs));
            }
            return requests;
        } catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
    
    public static boolean acceptFriend(int requestid, int user1, int user2){
        try {
            Connection connection = DB.getConnection();
            PreparedStatement ps = connection.prepareStatement(DELETEREQUEST);
            ps.setInt(1, requestid);
            int r = ps.executeUpdate();         
            if(r == 1){
                ps = connection.prepareStatement(ADDFRIEND);
                ps.setInt(1, user1);
                ps.setInt(2, user2);
                r = ps.executeUpdate();                
                if(r == 1) return true;
                else return false;
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }
    
    public static boolean declineFriend(int requestid) {
        try {
            Connection connection = DB.getConnection();
            PreparedStatement ps = connection.prepareStatement(DELETEREQUEST);
            ps.setInt(1, requestid);
            int r = ps.executeUpdate();         
            if(r == 1){             
                return true;
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }
    
    private static Friendrequest extract(ResultSet rs) throws SQLException{
        int id = rs.getInt("id");
        int user1 = rs.getInt("user1");
        int user2 = rs.getInt("user2");
        int status = rs.getInt("status");
        return new Friendrequest(id, user1, user2, status);
    }
}
