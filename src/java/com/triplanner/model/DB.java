/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.triplanner.model;

/**
 *
 * @author brook
 */
import java.sql.*;

public class DB {
    private static String url = "jdbc:mysql://TriPlanner308.db.10332557.hostedresource.com:3306/TriPlanner308?zeroDateTimeBehavior=convertToNull";
    private static String username = "TriPlanner308";
    private static String password = "qwerAsdf!1";

    static {
        try {
            Class.forName("com.mysql.jdbc.Driver"); // You don't need to load it on every single opened connection.
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }
}
