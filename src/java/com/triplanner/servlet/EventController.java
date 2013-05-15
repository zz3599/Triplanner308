/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.triplanner.servlet;

import com.triplanner.entities.Event;
import com.triplanner.entities.Tripday;
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

    /**
     * Side effect of getting all the trip's events: it sets the trip id into
     * the session for easier querying in the future
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public static void doTripsEventsGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        List<Event> events = null;
        if (action.equals("trip")) {
            int tripid = Integer.parseInt(request.getParameter("tripid"));
            request.getSession().setAttribute("tripid", tripid);
            events = EventDAO.getAllEventsByTrip(tripid);
        } else if(action.equals("day")){
            int dayid = Integer.parseInt(request.getParameter("tripdayid"));
            events = EventDAO.getAllEventsByDay(dayid);
        }
        JSONArray o = new JSONArray();
        if (events != null && !events.isEmpty()) {
            for (Event e : events) {
                o.put(e.toJSON());
            }
        }
        response.setContentType("application/json");
        response.getWriter().println(o);
    }

    public static void doEventPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        int tripid = (Integer) (request.getSession().getAttribute("tripid"));
        int tripdayid = ((Tripday) (request.getSession().getAttribute("tripday"))).getId();
        String startLocation = request.getParameter("eventstartlocation");
        String endLocation = request.getParameter("eventendlocation");
        Timestamp startTime = Timestamp.valueOf(request.getParameter("eventstarttime"));
        Timestamp endTime = Timestamp.valueOf(request.getParameter("eventendtime"));
        String comment = request.getParameter("eventdescription");
        Event newEvent = null;
        if (action.equals("add")) {
            newEvent = EventDAO.createEvent(tripid, tripdayid, startTime, endTime, 0, comment, startLocation, endLocation);
        } else if (action.equals("update")) {
            int eventid = Integer.parseInt(request.getParameter("eventid"));
            newEvent = EventDAO.updateEvent(eventid, tripid, tripdayid, startTime, endTime, startLocation, endLocation, comment);
        }
        JSONObject o = new JSONObject();
        if (newEvent != null) {
            o = newEvent.toJSON();
        }
        response.setContentType("application/json");
        response.getWriter().println(o);
    }
}
