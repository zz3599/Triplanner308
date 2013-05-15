/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.triplanner.servlet;

import com.triplanner.entities.Hotel;
import com.triplanner.entities.Tripday;
import com.triplanner.model.HotelDAO;
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
public class HotelController {

    public static void doHotelsGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        int tripid = (Integer)request.getSession().getAttribute("tripid");
        int tripdayid = ((Tripday)(request.getSession().getAttribute("tripday"))).getId();
        List<Hotel> hotels = null;
        if (action.equals("trip")) {
            hotels = HotelDAO.getAllHotelsByTrip(tripid);
        } else {
            hotels = HotelDAO.getAllHotelsByDay(tripid, tripdayid);
        }
        JSONArray o = new JSONArray();
        if (hotels != null && !hotels.isEmpty()) {
            for (Hotel h : hotels) {
                o.put(h.toJSON());
            }
        }
        response.setContentType("application/json");
        response.getWriter().println(o);
    }

    public static void doHotelsPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int tripid = (Integer)request.getSession().getAttribute("tripid");
        int tripdayid = ((Tripday)(request.getSession().getAttribute("tripday"))).getId();
        
        String location = request.getParameter("location");
        Hotel hotel = HotelDAO.createHotel(tripid, tripdayid, location);
        JSONObject o = new JSONObject();
        if (hotel != null) {
            o = hotel.toJSON();
        }
        response.setContentType("application/json");
        response.getWriter().println(o);
    }
}
