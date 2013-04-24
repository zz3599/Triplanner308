/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.triplanner.entities;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import org.json.JSONObject;

/**
 *
 * @author brook
 */
public class Event {
    public int id;
    public int tripid;
    public int tripdayid;
    public Timestamp startTime;
    public Timestamp endTime;
    public String startLocation;
    public String endLocation; 
    public int eventType;
    public String comment;
    private static final SimpleDateFormat formatter = new SimpleDateFormat("M-dd-yyyy hh:mm");
    
    public JSONObject toJSON(){
        JSONObject o = new JSONObject();
        o.put("id", id);
        o.put("tripid", tripid);
        o.put("tripdayid", tripdayid);
        o.put("startTime", formatter.format(startTime));
        o.put("endTime", formatter.format(endTime));
        o.put("startLocation", startLocation);
        o.put("endLocation", endLocation);
        o.put("eventType", eventType);
        o.put("comment", comment);
        return o;
    }
    
    public Event(int id, int tripid, int tripdayid, Timestamp startTime, Timestamp endTime, String startLocation, String endLocation, int eventType, String comment) {
        this.id = id;
        this.tripid = tripid;
        this.tripdayid = tripdayid;
        this.startTime = startTime;
        this.endTime = endTime;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.eventType = eventType;
        this.comment = comment;
    }   
       
    public String getStartLocation() {
        return startLocation;
    }

    public String getEndLocation() {
        return endLocation;
    }

    public int getTripid() {
        return tripid;
    }
    
    public int getTripdayid() {
        return tripdayid;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public int getEventType() {
        return eventType;
    }

    public String getComment() {
        return comment;
    }
    
}
