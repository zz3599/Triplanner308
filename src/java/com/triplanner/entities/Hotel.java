/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.triplanner.entities;

import org.json.JSONObject;

/**
 *
 * @author brook
 */
public class Hotel extends AbstractEntity{
    public int tripid;
    public int tripdayid;
    public String location; 

    public Hotel(int id, int tripid, int tripdayid, String location) {
        this.id = id;
        this.tripid = tripid;
        this.tripdayid = tripdayid;
        this.location = location;
    }

    @Override
    public JSONObject toJSON(){
        JSONObject o = new JSONObject();
        o.put("id", id);
        o.put("tripid", tripid);
        o.put("tripdayid", tripdayid);
        o.put("location", location);
        return o;
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
    
    
}
