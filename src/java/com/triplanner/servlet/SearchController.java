/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.triplanner.servlet;

import com.triplanner.entities.Trip;
import com.triplanner.entities.User;
import com.triplanner.model.TripDAO;
import com.triplanner.model.UserDAO;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author brook
 */
public class SearchController {
    public static void doSearchGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        String searchterm = request.getParameter("search");
        List<User> users = UserDAO.searchUser(searchterm);
        List<Trip> trips = TripDAO.searchTrip(searchterm);
        JSONArray a = new JSONArray();
        JSONArray b = new JSONArray();
        for(User user : users){
            a.put(user.toJSON());
        }
        for(Trip trip : trips){
            b.put(trip.toJSON());
        }
        JSONObject o = new JSONObject();
        o.put("users", a);
        o.put("trips", b);
        response.setContentType("application/json");
        response.getWriter().println(o);
    }
}
