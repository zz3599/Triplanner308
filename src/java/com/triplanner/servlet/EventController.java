/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.triplanner.servlet;

import com.triplanner.entities.Event;
import com.triplanner.model.EventDAO;
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
public class EventController {
    public static void doTripsEventsGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int tripid = Integer.parseInt(request.getParameter("tripid"));
        List<Event> events = EventDAO.selectAllEventsByTrip(tripid);
        JSONArray o = new JSONArray();
        if (events != null || !events.isEmpty()) {
            for (Event e : events) {
                o.put(e.toJSON());
            }
        }
        response.getWriter().println(o);
    }
    
    public static void doCreateEventPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int tripid = Integer.parseInt(request.getParameter("tripid"));
        int tripdayid = Integer.parseInt(request.getParameter("tripdayid"));
        String startLocation = request.getParameter("eventstartLocation");
        String endLocation = request.getParameter("eventendLocation");
        Timestamp startTime = Timestamp.valueOf(request.getParameter("eventstartTime"));
        Timestamp endTime = Timestamp.valueOf(request.getParameter("eventendTime"));
        String comment = request.getParameter("eventdescription");
        Event newEvent = EventDAO.createEvent(tripid, tripdayid, startTime, endTime, tripdayid, comment, startLocation, endLocation);
        JSONObject o = new JSONObject();
        if (newEvent != null) {
            o = newEvent.toJSON();
        }
        response.getWriter().println(o);
    }
}
