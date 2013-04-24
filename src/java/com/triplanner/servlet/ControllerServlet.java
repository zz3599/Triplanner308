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
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    public static final SimpleDateFormat jsDateFormat = new SimpleDateFormat("M-d-yyyy");
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
            TripController.doTripsGet(request, response);
        } else if (resource.contains("/events")) {
            EventController.doTripsEventsGet(request, response);
        } else if (resource.contains("/tripday")) {
            TripdayController.doTripdaysGet(request, response);
        }
    }
    
    /**
     * All post requests to the url patterns
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        String resource = getResource(request);
        if (resource.equals("/login")) {
            UserController.doLoginPost(request, response);
        } else if (resource.equals("/register")) {
            UserController.doRegisterPost(request, response);
        } else if (resource.equals("/trip")) {
            TripController.doCreateTripPost(request, response);
        } else if (resource.equals("/events")) {
            EventController.doCreateEventPost(request, response);
        } else if (resource.contains("/tripday")) {
            TripdayController.doCreateTripdayPost(request, response);
        }
    }
    
    private String getResource(HttpServletRequest request) {
        String requestString = request.getRequestURI();
        return requestString.substring(requestString.lastIndexOf("/"));
    }
}
