/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.triplanner.entities;

import org.json.JSONObject;

/**
 *
 * @author Brook
 */
public class Waypoint extends AbstractEntity{
    public int id;
    public int tripid;
    public int tripdayid;
    public String location;
    public int pointnum;
    
    @Override
    public JSONObject toJSON() {
        JSONObject o = new JSONObject();
        o.put("id", id);
        o.put("tripid", tripid);
        o.put("tripdayid", tripdayid);
        o.put("location", location);
        o.put("pointnum", pointnum);
        return o;
    }

    public Waypoint(int id, int tripid, int tripdayid, String location, int pointnum) {
        this.id = id;
        this.tripid = tripid;
        this.tripdayid = tripdayid;
        this.location = location;
        this.pointnum = pointnum;
    }

    
    public int getTripid() {
        return tripid;
    }

    public int getTripdayid() {
        return tripdayid;
    }

    public String getLocation() {
        return location;
    }

    public int getPointnum() {
        return pointnum;
    }
    
}
