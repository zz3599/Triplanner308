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
public class Photo {

    private static final SimpleDateFormat formatter = new SimpleDateFormat("M-d-yyyy hh:mm");
    public int id;
    public String url;
    public int userid;
    public int tripid;
    public Integer tripdayid;
    public Integer eventid;
    public String comment;
    public Timestamp uploadtime;

    public JSONObject toJSON() {
        JSONObject o = new JSONObject();
        o.put("id", id);
        o.put("url", url);
        o.put("userid", userid);
        o.put("tripid", tripdayid);
        o.put("eventid", eventid);
        o.put("comment", comment);
        o.put("uploadtime", formatter.format(uploadtime));
        return o;
    }

    public Photo(int id, String url, int userid, int tripid, Integer tripdayid, Integer eventid, String comment, Timestamp uploadtime) {
        this.id = id;
        this.url = url;
        this.userid = userid;
        this.tripid = tripid;
        this.tripdayid = tripdayid;
        this.eventid = eventid;
        this.comment = comment;
        this.uploadtime = uploadtime;
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

    public Integer getTripdayid() {
        return tripdayid;
    }

    public Integer getEventid() {
        return eventid;
    }

    public String getComment() {
        return comment;
    }

    public Timestamp getUploadtime() {
        return uploadtime;
    }
}
