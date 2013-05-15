/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.triplanner.servlet;

import com.triplanner.entities.Waypoint;
import com.triplanner.model.WaypointDAO;
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
public class WaypointController {
    /*
     * Need to cache the waypoints for quick batch updates
     */
    private static List<Waypoint> cachedWaypoints = null;
    
    public static void doWaypointsGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int tripid = Integer.parseInt(request.getParameter("tripid"));
        String tripdayid_str = request.getParameter("tripdayid");
        List<Waypoint> waypoints = null;
        if (tripdayid_str == null || tripdayid_str.equals("")) {
            waypoints = WaypointDAO.getWaypointsByTrip(tripid);
        } else {
            int tripdayid = Integer.parseInt(tripdayid_str);
            waypoints = WaypointDAO.getWaypointsByDay(tripid, tripdayid);
        }
        JSONArray a = new JSONArray();
        if (waypoints != null) {
            cachedWaypoints = waypoints;
            for (Waypoint point : waypoints) {
                a.put(point.toJSON());
            }
        }
        response.setContentType("application/json");
        response.getWriter().println(a);
    }

    public static void doWaypointsPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        int tripid = Integer.parseInt(request.getParameter("tripid"));
        int tripdayid = Integer.parseInt(request.getParameter("tripdayid"));
        JSONObject o = new JSONObject();
        if (action.equals("add")) {
            String location = request.getParameter("location");
            //we keep track of the ordering of the waypoints
            int numberwaypoints = WaypointDAO.getWaypointsCountByDay(tripid, tripdayid);
            int pointnum = numberwaypoints + 1;
            Waypoint waypoint = WaypointDAO.createWaypoint(tripid, tripdayid, location, pointnum);
            if (waypoint != null) {
                o = waypoint.toJSON();
            }
        } else if (action.equals("update")) {
            //add facility to update the waypoints in any arbitrary fashion - delete, switch, add
            String[] locations = request.getParameterValues("locations[]");
            int numberwaypoints = WaypointDAO.getWaypointsCountByDay(tripid, tripdayid);
            boolean success = WaypointDAO.updateAll(locations, tripid, tripdayid, numberwaypoints);
            System.out.println(success);
        }
        response.setContentType("application/json");
        response.getWriter().println(o);

    }
}
