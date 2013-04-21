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
    public int eventType;
    public String comment;
    
    public Event(int id, int tripdayid, Timestamp startTime, Timestamp endTime, int eventType, String comment) {
        this.id = id;
        this.tripdayid = tripdayid;
        this.startTime = startTime;
        this.endTime = endTime;
        this.eventType = eventType;
        this.comment = comment;
    }

    public int getID(){
        return id;
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
