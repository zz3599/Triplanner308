/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.triplanner.poll;

import com.triplanner.entities.Friendrequest;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;

/**
 *
 * @author brook
 */
public class Friendpoll implements AsyncListener {

    private static final long REQUEST_TIMEOUT = 10000;  // Timeout after
    private AsyncContext asyncContext;  // The async context object
    private boolean open;               // Whether the poll is still open
    private int userid; //this is what we will try to find in the friendrequests

    public Friendpoll(HttpServletRequest request, HttpServletResponse response, int userid) {
        asyncContext = request.startAsync(request, response);
        this.userid = userid;
        open = true;
        asyncContext.setTimeout(REQUEST_TIMEOUT);
        asyncContext.addListener(this);
    }

    /**
     * The poll request responds and closes 
     */
    public void respond(List<Friendrequest> friendrequests){
        try {
            ServletResponse response = asyncContext.getResponse();
            PrintWriter writer = response.getWriter();
            JSONArray a = new JSONArray();
            for(Friendrequest request : friendrequests){
                a.put(request.toJSON());
            }
            response.setContentType("application/json");
            writer.println(a);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            asyncContext.complete();
        }
    }
    
    @Override
    public void onComplete(AsyncEvent event) throws IOException {
        open = false;
    }

    @Override
    public void onTimeout(AsyncEvent event) throws IOException {
        HttpServletResponse response = (HttpServletResponse)asyncContext.getResponse();
        response.sendError(408);
        open = false;
        asyncContext.complete();
    }

    @Override
    public void onError(AsyncEvent event) throws IOException {
        HttpServletResponse response = (HttpServletResponse)asyncContext.getResponse();
        response.sendError(500);
        open = false;
        asyncContext.complete();
    }

    @Override
    public void onStartAsync(AsyncEvent event) throws IOException {
        
    }

    public AsyncContext getAsyncContext() {
        return asyncContext;
    }

    public boolean isOpen() {
        return open;
    }

    public int getUserid() {
        return userid;
    }
    
    
}
