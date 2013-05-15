/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.triplanner.servlet;

import com.triplanner.entities.Friendrequest;
import com.triplanner.entities.User;
import com.triplanner.model.FriendrequestDAO;
import com.triplanner.poll.Friendpoll;
import com.triplanner.service.PollService;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author brook
 */
public class FriendpollController {
    /**
     * Will asynchronously fetch all friend requests for this user
     * @param request
     * @param response
     * @param pollService
     * @throws ServletException
     * @throws IOException 
     */
    public static void doPollGet(HttpServletRequest request, HttpServletResponse response, PollService pollService)
            throws ServletException, IOException {
        User user = (User)request.getSession().getAttribute("user");
        int userid = user.id;
        Friendpoll friendpoll = new Friendpoll(request, response, userid);
        pollService.addFriendpoll(friendpoll);         
    }
    
    public static void doPollPost(HttpServletRequest request, HttpServletResponse response, PollService pollService)
            throws ServletException, IOException {
        User user = (User)request.getSession().getAttribute("user");
        int userid = user.id;
        int user2 = Integer.parseInt(request.getParameter("userid"));
        Friendrequest friendrequest = FriendrequestDAO.createFriendrequest(userid, user2);
        pollService.addFriendrequest(friendrequest);
    }
}
