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
    "/app/trip"
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
        } else if(resource.equals("/trip")){
            doTripsGet(request, response);
        }
    }
    
    protected void doTripsGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        List<Trip> trips = TripDAO.getUserTrips(user);
        JSONArray result = new JSONArray();
        for(Trip trip : trips){
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
        } else if (resource.equals("/trip")){
            doCreateTripPost(request, response);
        }
    }
    
    /**
     * Handler for logging in
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
            request.getSession(true).setAttribute("user", user);
            response.sendRedirect("app/home");
        }
    }

    /**
     * Handler for registration
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
            request.getSession(true).setAttribute("user", user);
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
        if(startLocation.isEmpty() || s.isEmpty() || e.isEmpty()){
            return;
        }        
        Timestamp startTime = Timestamp.valueOf(s);
        Timestamp endTime = Timestamp.valueOf(e);
        User user = (User)request.getSession().getAttribute("user");
        Trip trip = TripDAO.createTrip(user, title, description, startTime, endTime, startLocation, endLocation, false);
        JSONObject result = new JSONObject();
        if(trip != null){
            result = trip.toJSON();
        }
        response.getWriter().println(result);
        
    }

    private String getResource(HttpServletRequest request) {
        String requestString = request.getRequestURI();
        return requestString.substring(requestString.lastIndexOf("/"));
    }
}
