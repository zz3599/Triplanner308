/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.triplanner.entities;

import java.sql.Timestamp;

/**
 *
 * @author brook
 */
public class Event {
    public int id;
    public int tripdayid;
    public Timestamp startTime;
    public Timestamp endTime;
    public String startLocation;
    public String endLocation; 
    public int eventType;
    public String comment;

    public Event(int id, int tripdayid, Timestamp startTime, Timestamp endTime, String startLocation, String endLocation, int eventType, String comment) {
        this.id = id;
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
