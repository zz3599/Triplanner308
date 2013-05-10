/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.triplanner.servlet;

import com.triplanner.entities.Tripday;
import com.triplanner.model.TripdayDAO;
import java.io.IOException;
import java.sql.Timestamp;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;

/**
 *
 * @author brook
 */
public class TripdayController {
    public static void doTripdaysGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Long timemillis = Long.parseLong(request.getParameter("date"));
        Timestamp tripdaystart = new Timestamp(timemillis);
        int tripid = Integer.parseInt(request.getParameter("tripid"));
        Tripday tripday = TripdayDAO.getDay(tripid, tripdaystart);
        request.getSession().setAttribute("tripday", tripday);
        JSONObject o = new JSONObject();
        if(tripday != null){
            o = tripday.toJSON();
        } 
        response.getWriter().println(o);
    }
    
    public static void doTripdayPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        int id = Integer.parseInt(request.getParameter("id"));
        int tripid = Integer.parseInt(request.getParameter("tripid"));
        int daynum = Integer.parseInt(request.getParameter("daynum"));
        String startLocation = request.getParameter("daystart");
        String endLocation = request.getParameter("dayend");
        String comment = request.getParameter("comment");
        String date = request.getParameter("date");
        Timestamp t = null; 
        try { 
            t = new Timestamp(ControllerServlet.jsDateFormat.parse(date).getTime());        
        }
        catch(Exception e){}
        JSONObject o = new JSONObject();
        if(action.equals("update") && t != null){
            Tripday updatedday = TripdayDAO.updateTripday(id, tripid, t, startLocation, endLocation, comment, daynum);
            if(updatedday != null)
                o = updatedday.toJSON();
        } else if(action.equals("create")){
        }
        response.getWriter().println(o);
    }
}
