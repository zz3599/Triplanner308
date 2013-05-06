/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.triplanner.entities;

import java.io.Serializable;
import org.json.JSONObject;

/**
 *
 * @author Brook
 */
public abstract class AbstractEntity implements Serializable{
    public int id;
    
    public int getId(){
        return id;
    }
    
    public abstract JSONObject toJSON();
}
