/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.triplanner.servlet;

import java.io.IOException;
import java.text.SimpleDateFormat;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
    "/app/tripday",
    "/app/photo",
    "/app/waypoint",
    "/app/hotel"
},
        asyncSupported = true)
@MultipartConfig

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
        } else if (resource.contains("/photo")){
            PhotoController.doPhotoGet(request, response);
        } else if (resource.contains("/waypoint")){
            WaypointController.doWaypointsGet(request, response);
        } else if(resource.contains("/hotel")){
            HotelController.doHotelsGet(request, response);
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
            TripController.doTripPost(request, response);
        } else if (resource.equals("/events")) {
            EventController.doEventPost(request, response);
        } else if (resource.contains("/tripday")) {
            TripdayController.doTripdayPost(request, response);
        } else if (resource.contains("/photo")){
            PhotoController.doUploadPost(request, response, getServletContext());
        } else if (resource.contains("/waypoint")){
            WaypointController.doWaypointsPost(request, response);
        } else if(resource.contains("/hotel")){
            HotelController.doHotelsPost(request, response);
        }
    }
    
    private String getResource(HttpServletRequest request) {
        String requestString = request.getRequestURI();
        return requestString.substring(requestString.lastIndexOf("/"));
    }
}
