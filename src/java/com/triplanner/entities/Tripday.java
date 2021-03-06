/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.triplanner.entities;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import org.json.JSONObject;

/**
 *
 * @author brook
 */
public class Tripday extends AbstractEntity{
    public int tripid;
    public Timestamp date;
    public String startLocation;
    public String endLocation;
    public String comment;
    public int daynum;
    public List<Event> events;   
    
    @Override
    public JSONObject toJSON(){
        JSONObject o = new JSONObject();
        o.put("id", this.id);
        o.put("tripid", this.tripid);
        o.put("date", this.date);
        o.put("startLocation", this.startLocation);
        o.put("endLocation", this.endLocation);
        o.put("comment", this.comment);
        o.put("daynum", this.daynum);
        o.put("events", this.events);
        return o;
    }
    
    public Tripday(int id, int tripid, Timestamp date, String startLocation, String endLocation, String comment, int daynum) {
        this.id = id;
        this.tripid = tripid;
        this.date = date;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.comment = comment;
        this.daynum = daynum;
    }

    public int getTripid() {
        return tripid;
    }

    public String getStartLocation() {
        return startLocation;
    }

    public String getEndLocation() {
        return endLocation;
    }

    public String getComment() {
        return comment;
    }

    public Timestamp getDate() {
        return date;
    }

    public int getDaynum() {
        return daynum;
    }
    
    public List<Event> getEvents(){
        return events;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTripid(int tripid) {
        this.tripid = tripid;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public void setStartLocation(String startLocation) {
        this.startLocation = startLocation;
    }

    public void setEndLocation(String endLocation) {
        this.endLocation = endLocation;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setDaynum(int daynum) {
        this.daynum = daynum;
    }
}
