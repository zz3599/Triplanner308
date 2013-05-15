/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.triplanner.servlet;

import com.triplanner.entities.Friendrequest;
import com.triplanner.entities.User;
import com.triplanner.model.FriendrequestDAO;
import com.triplanner.service.PollService;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;

/**
 * This controller servlet provides interface between the web page and the
 * server side model and entities.
 *
 * @author brook
 */
@WebServlet(
        urlPatterns = {
    "/login",
    "/app/logout",
    "/register",
    "/app/home",
    "/app/trip",
    "/app/events",
    "/app/tripday",
    "/app/photo",
    "/app/waypoint",
    "/app/hotel",
    "/app/daydetails",
    "/app/search",
    "/app/friendpoll",
    "/app/friendrequests"
},
        asyncSupported = true)
@MultipartConfig

public class ControllerServlet extends HttpServlet {
    private PollService pollService;
    
    @Override
    public void init() throws ServletException{
        //manages all long poll requests
        pollService = new PollService();
    }
    
    
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
        } else if(resource.contains("daydetails")){
            TripdayController.doTripdaysGet(request, response);
        } else if(resource.contains("search")){
            SearchController.doSearchGet(request, response);
        } else if(resource.contains("logout")){
            request.getSession().invalidate();
            response.sendRedirect("../");
        } else if(resource.contains("friendpoll")){
            //fetch all the friend requests for with this user as the receiver
            FriendpollController.doPollGet(request, response, pollService);
        } else if(resource.contains("friendrequests")){
            //get all friend requests from db
            int userid = ((User)request.getSession().getAttribute("user")).id;
            List<Friendrequest> requests = FriendrequestDAO.getAllFriendrequests(userid);
            JSONArray a = new JSONArray();
            for(Friendrequest r : requests){
                a.put(r.toJSON());
            }
            response.setContentType("application/json");
            response.getWriter().println(a);
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
        } else if(resource.contains("friendpoll")){
            //submit a friend request with this user as the sender
            FriendpollController.doPollPost(request, response, pollService);
        }
    }
    
    private String getResource(HttpServletRequest request) {
        String requestString = request.getRequestURI();
        return requestString.substring(requestString.lastIndexOf("/"));
    }
}
