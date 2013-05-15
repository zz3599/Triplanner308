package com.triplanner.service;

import com.triplanner.entities.Friendrequest;
import com.triplanner.poll.Friendpoll;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Manages all long polls for friend requests, comments, etc.
 *
 * @author brook
 */
public class PollService extends TimerTask {

    private static final long SLEEP_DURATION = 1000l;
    private Queue<Friendrequest> friendrequestQueue;
    private Queue<Friendpoll> friendpollQueue;

    public PollService() {
        friendrequestQueue = new ConcurrentLinkedQueue();
        friendpollQueue = new ConcurrentLinkedQueue();
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(this, SLEEP_DURATION, SLEEP_DURATION);
    }

    public void addFriendrequest(Friendrequest request) {
        friendrequestQueue.add(request);
    }

    public void addFriendpoll(Friendpoll poll) {
        friendpollQueue.add(poll);
    }

    @Override
    public void run() {
        if (friendrequestQueue.size() > 0) {
            for (Friendpoll poll : friendpollQueue) {
                if (poll.isOpen()) {
                    List<Friendrequest> friendrequests = getRelatedFriendRequests(poll);
                    poll.respond(friendrequests);
                }
            }
            friendpollQueue.clear();
            //consume the requests 
            friendrequestQueue.clear();//always fetch the friendrequests directly first, then poll
        }
        
    }

    /**
     * Get the friend requests of the particular user
     *
     * @param poll The long poll for friend requests
     * @return All the friend requests
     */
    private List<Friendrequest> getRelatedFriendRequests(Friendpoll poll) {
        int userid = poll.getUserid();
        List<Friendrequest> result = new ArrayList<Friendrequest>();
        for (Friendrequest request : friendrequestQueue) {
            if (request.involvesUser(userid)) {
                result.add(request);
            }
        }
        return result;
    }
}
