/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.triplanner.servlet;

import com.triplanner.entities.Hotel;
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
        int tripid = Integer.parseInt(request.getParameter("tripid"));
        String tripdayid_str = request.getParameter("tripdayid");
        List<Hotel> hotels = null;
        if (tripdayid_str == null || tripdayid_str.equals("")) {
            hotels = HotelDAO.getAllHotelsByTrip(tripid);
        } else {
            int tripdayid = Integer.parseInt(request.getParameter("tripdayid"));
            hotels = HotelDAO.getAllHotelsByDay(tripid, tripdayid);
        }
        JSONArray o = new JSONArray();
        if (hotels != null && !hotels.isEmpty()) {
            for (Hotel h : hotels) {
                o.put(h.toJSON());
            }
        }
        response.getWriter().println(o);
    }

    public static void doHotelsPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int tripid = Integer.parseInt(request.getParameter("tripid"));
        int tripdayid = Integer.parseInt(request.getParameter("tripdayid"));
        String location = request.getParameter("location");
        Hotel hotel = HotelDAO.createHotel(tripid, tripdayid, location);
        JSONObject o = new JSONObject();
        if (hotel != null) {
            o = hotel.toJSON();
        }
        response.getWriter().println(o);
    }
}
