/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.triplanner.entities;

import java.sql.Timestamp;

/**
 * Object to represent a trip
 * @author brook
 */
public class Trip {
    public int id;
    public int userid;
    public String title;
    public String description;
    public Timestamp startTime;
    public Timestamp endTime;
    public String startLocation;
    public String endLocation;
    public boolean shared;

    public int getId() {
        return id;
    }

    public int getUserid() {
        return userid;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public String getStartLocation() {
        return startLocation;
    }

    public String getEndLocation() {
        return endLocation;
    }

    public boolean isShared() {
        return shared;
    }

    public Trip(int id, int userid, String title, String description, Timestamp startTime, Timestamp endTime, String startLocation, String endLocation, boolean shared) {
        this.id = id;
        this.userid = userid;
        this.title = title;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.shared = shared;
    }
    
}
