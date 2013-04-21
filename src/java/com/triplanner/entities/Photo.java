/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.triplanner.entities;

/**
 *
 * @author brook
 */
public class Photo {
    public int id;
    public String url;
    public int userid;
    public int tripid;
    public int tripdayid;
    public int eventid;
    public String comment;
    
    public Photo(int id, String url, int userid, int tripid, int tripdayid, int eventid, String comment) {
        this.id = id;
        this.url = url;
        this.userid = userid;
        this.tripid = tripid;
        this.tripdayid = tripdayid;
        this.eventid = eventid;
        this.comment = comment;
    }

    public int getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public int getUserid() {
        return userid;
    }

    public int getTripid() {
        return tripid;
    }

    public int getTripdayid() {
        return tripdayid;
    }

    public int getEventid() {
        return eventid;
    }

    public String getComment() {
        return comment;
    }
}
