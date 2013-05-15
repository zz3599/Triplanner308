package com.triplanner.model;

import com.triplanner.entities.User;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages the users table and users objects
 * @author brook
 */
public class UserDAO implements Serializable {
    private static final String LOGIN = "SELECT * from users where email=? and password=?";
    private static final String CREATEUSER = "INSERT into users "
            + "(email, firstname, lastname, password) values " 
            + "(?, ?, ?, ?)";  
    private static final String UPDATEUSER = "UPDATE users "
            + "Set email=?, firstname=?, lastname=?, password=? "
            + "where id=?";
    private static final String SEARCHUSER = "Select * from users where (firstname like ? or lastname like ?) "
            + "and authority=1";
    private static final String SELECTUSER = "Select * from users where id =?";
    //User type enum 
    enum Authority{
        ADMIN, USER
    }
    
    public static User getUser(int id){
        try {
            Connection connection = DB.getConnection();
            PreparedStatement ps = connection.prepareStatement(SELECTUSER);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if(rs.first()){
               return extractUser(rs);
            }             
        } catch(Exception e){
            e.printStackTrace();
        }
        return null;        
    }
    
    public static List<User>searchUser(String search){
        try {
            Connection connection = DB.getConnection();
            PreparedStatement ps = connection.prepareStatement(SEARCHUSER);
            ps.setString(1, "%" + search + "%");
            ps.setString(2, "%" + search + "%");
            List<User> users = new ArrayList<User>();
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
               users.add(extractUser(rs));
            }             
            return users;
        } catch(Exception e){
            e.printStackTrace();
        }
        return null;  
    }
    
    public static User loginUser(String email, String password){
        try {
            Connection connection = DB.getConnection();
            PreparedStatement ps = connection.prepareStatement(LOGIN);
            ps.setString(1, email);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if(rs.first()){
               return extractUser(rs);
            }             
        } catch(Exception e){
            e.printStackTrace();
        }
        return null;        
    }
    
    public static User createUser(String email, String first, String last, String password){
        try {
            Connection connection = DB.getConnection();
            PreparedStatement ps = connection.prepareStatement(CREATEUSER, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, email);
            ps.setString(2, first);
            ps.setString(3, last);
            ps.setString(4, password);
            int result = ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()){
               return new User(rs.getInt(1), email, first, last, password, Authority.USER.ordinal());
            }             
        } catch(Exception e){
            e.printStackTrace();
        }
        return null;        
    }
    
    public static User updateUser(User user, String email, String first, String last, String password){
        try {
            Connection connection = DB.getConnection();
            PreparedStatement ps = connection.prepareStatement(UPDATEUSER);
            ps.setString(1, email);
            ps.setString(2, first);
            ps.setString(3, last);
            ps.setString(4, password);
            ps.setInt(5, user.id);
            int result = ps.executeUpdate();
            if(result == 1){
               return new User(user.id, email, first, last, password, Authority.USER.ordinal());
            }             
        } catch(Exception e){
            e.printStackTrace();
        }
        return null;        
    }
    
    /**
     * Extract the user from the result set
     * @param rs
     * @return
     * @throws SQLException 
     */
    public static User extractUser(ResultSet rs) throws SQLException{
        int id = rs.getInt("id");
        String email = rs.getString("email");
        String password = rs.getString("password");
        String firstname = rs.getString("firstname");
        String lastname = rs.getString("lastname");
        int authority = rs.getInt("authority");
        return new User(id, email, firstname, lastname, password, authority);
    }

    
    
    
}
