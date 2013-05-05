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
public abstract class AbstractEntity {
    public int id;
    public abstract JSONObject toJSON();
    public int getId(){
        return id;
    }
}
