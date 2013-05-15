/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.triplanner.entities;

import org.json.JSONObject;

/**
 * A friend request poll request that is sent to the PollFriend service when 
 * a user wants to add a friend.
 * @author brook
 */
public class Friendrequest extends AbstractEntity {
    public int user1;
    public int user2;
    public int status;
    
    @Override
    public JSONObject toJSON() {
        JSONObject o = new JSONObject();
        o.put("id", id);
        o.put("user1", user1);
        o.put("user2", user2);
        o.put("status", status);
        return o;
    }

    public int getUser1() {
        return user1;
    }

    public int getUser2() {
        return user2;
    }
    
    public int getStatus(){
        return status;
    }

    public Friendrequest(int id, int user1, int user2, int status) {
        this.id = id;
        this.user1 = user1;
        this.user2 = user2;
        this.status = status;
    }
    
    /**
     * Returns true if the user is the receiver of this friend request
     * @param userid
     * @return 
     */
    public boolean involvesUser(int userid){
        return user2 == userid;
    }
    
    
    
}
