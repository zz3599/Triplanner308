/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.triplanner.servlet;

import com.triplanner.entities.Event;
import com.triplanner.entities.Trip;
import com.triplanner.entities.Tripday;
import com.triplanner.entities.User;
import com.triplanner.model.EventDAO;
import com.triplanner.model.TripDAO;
import com.triplanner.model.TripdayDAO;
import com.triplanner.model.UserDAO;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * This controller servlet provides interface between the web page and the
 * server side model and entities.
 *
 * @author brook
 */
@WebServlet(
        urlPatterns = {
    "/login",
    "/register",
    "/app/home",
    "/app/trip",
    "/app/events",
    "/app/tripday"
},
        asyncSupported = true)
public class ControllerServlet extends HttpServlet {
    
    /**
     * All get requests for the url patterns
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String resource = getResource(request);
        if (resource.equals("/home")) {
            request.getRequestDispatcher("home.jsp").forward(request, response);
        } else if (resource.equals("/trip")) {
            doTripsGet(request, response);
        } else if (resource.contains("/events")) {
            doTripsEventsGet(request, response);
        } else if (resource.contains("/tripday")) {
            doTripdaysGet(request, response);
        }
    }

    protected void doTripdaysGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Long timemillis = Long.parseLong(request.getParameter("date"));
        Timestamp tripdaystart = new Timestamp(timemillis);
        int tripid = Integer.parseInt(request.getParameter("tripid"));
        Tripday tripday = TripdayDAO.getDay(tripid, tripdaystart);
        JSONObject o = new JSONObject();
        if(tripday != null){
            o = tripday.toJSON();
        } 
        response.getWriter().println(o);
    }
    

    protected void doTripsEventsGet(HttpServletRequest request, HttpServletResponse response)
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

    protected void doTripsGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        List<Trip> trips = TripDAO.getUserTrips(user);
        JSONArray result = new JSONArray();
        for (Trip trip : trips) {
            result.put(trip.toJSON());
        }
        response.getWriter().println(result);
    }

    /**
     * All post requests to the url patterns
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String resource = getResource(request);
        if (resource.equals("/login")) {
            doLoginPost(request, response);
        } else if (resource.equals("/register")) {
            doRegisterPost(request, response);
        } else if (resource.equals("/trip")) {
            doCreateTripPost(request, response);
        } else if (resource.equals("/events")) {
            doCreateEventPost(request, response);
        } else if (resource.equals("/tripday")) {
            doCreateTripdayPost(request, response);
        }
    }

    private void doCreateTripdayPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
    }

    private void doCreateEventPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int tripid = Integer.parseInt(request.getParameter("tripid"));
        int tripdayid = Integer.parseInt(request.getParameter("tripdayid"));
        String startLocation = request.getParameter("startLocation");
        String endLocation = request.getParameter("endLocation");
        Timestamp startTime = Timestamp.valueOf(request.getParameter("startTime"));
        Timestamp endTime = Timestamp.valueOf(request.getParameter("endTime"));
        String comment = request.getParameter("comment");
        Event newEvent = EventDAO.createEvent(tripid, tripdayid, startTime, endTime, tripdayid, comment, startLocation, endLocation);
        JSONObject o = new JSONObject();
        if (newEvent != null) {
            o = newEvent.toJSON();
        }
        response.getWriter().println(o);
    }

    /**
     * Handler for logging in
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void doLoginPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("loginemail");
        String password = request.getParameter("loginpassword");
        User user = UserDAO.loginUser(email, password);
        if (user == null) {
            request.setAttribute("errorMessage", "Invalid username/password");
            request.getRequestDispatcher("index.jsp").forward(request, response);
        } else {
            request.getSession().setAttribute("user", user);
            response.sendRedirect("app/home");
        }
    }

    /**
     * Handler for registration
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void doRegisterPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String first = request.getParameter("firstname");
        String last = request.getParameter("lastname");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        if (first.isEmpty() || last.isEmpty() || email.isEmpty() || password.isEmpty()) {
            request.setAttribute("errorMessage", "Missing required fields");
            request.getRequestDispatcher("index.jsp").forward(request, response);
            return;
        }
        User user = UserDAO.createUser(email, first, last, password);
        if (user == null) {
            request.setAttribute("errorMessage", "Invalid registration");
            request.getRequestDispatcher("index.jsp").forward(request, response);
        } else {
            request.getSession().setAttribute("user", user);
            response.sendRedirect("app/home.jsp");
        }
    }

    private void doCreateTripPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String startLocation = request.getParameter("startLocation");
        String endLocation = request.getParameter("endLocation");
        String s = request.getParameter("startTime");
        String e = request.getParameter("endTime");
        if (startLocation.isEmpty() || s.isEmpty() || e.isEmpty()) {
            return;
        }
        Timestamp startTime = Timestamp.valueOf(s);
        Timestamp endTime = Timestamp.valueOf(e);
        User user = (User) request.getSession().getAttribute("user");
        Trip trip = TripDAO.createTrip(user, title, description, startTime, endTime, startLocation, endLocation, false);
        JSONObject result = new JSONObject();
        if (trip != null) {
            result = trip.toJSON();
        }
        response.getWriter().println(result);

    }

    private String getResource(HttpServletRequest request) {
        String requestString = request.getRequestURI();
        return requestString.substring(requestString.lastIndexOf("/"));
    }
}
