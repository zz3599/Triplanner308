/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.triplanner.servlet;

import com.triplanner.entities.Event;
import com.triplanner.entities.Trip;
import com.triplanner.entities.User;
import com.triplanner.model.EventDAO;
import com.triplanner.model.TripDAO;
import com.triplanner.model.TripdayDAO;
import com.triplanner.utils.DateUtils;
import java.io.IOException;
import java.sql.Timestamp;
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
public class TripController {

    public static void doTripsGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        List<Trip> trips = TripDAO.getUserTrips(user);
        JSONArray result = new JSONArray();
        for (Trip trip : trips) {
            result.put(trip.toJSON());
        }
        response.getWriter().println(result);
    }

    public static void doTripPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String startLocation = request.getParameter("startLocation");
        String endLocation = request.getParameter("endLocation");
        String s = request.getParameter("startTime");
        String e = request.getParameter("endTime");
        if (startLocation.isEmpty() || s.isEmpty() || e.isEmpty()) {
            return;
        }
        Timestamp startTime = DateUtils.toTimestamp(s);
        Timestamp endTime = DateUtils.toTimestamp(e);
        User user = (User) request.getSession().getAttribute("user");
        Trip trip = null;
        JSONObject result = new JSONObject();
        
        if (action.equals("add")) {
            trip = TripDAO.createTrip(user, title, description, startTime, endTime, startLocation, endLocation, false);
            if (trip != null) {
                result = trip.toJSON();
                boolean createTripdays = TripdayDAO.createDaysForTrip(trip);
            }
        } else if (action.equals("update")) {
            int tripid = (Integer) request.getSession().getAttribute("tripid");
            trip = TripDAO.updateTrip(user, title, description, startTime, endTime, startLocation, endLocation, false, tripid);
            if(trip != null){
                result = trip.toJSON();
                boolean success = TripdayDAO.updateTripday(trip);
            }
        }
        response.getWriter().println(result);

    }
}
