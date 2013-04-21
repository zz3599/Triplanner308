/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.triplanner.utils;

import java.io.Serializable;

/**
 *
 * @author brook
 */
public class ErrorMessage implements Serializable {
    public static final ErrorMessage FAILDATE = new ErrorMessage("Fail", "Could not create date");
    
    private String title;
    private String text;

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }
    
    
    public ErrorMessage(String title, String text){
        this.title = title;
        this.text = text;
    }
}
