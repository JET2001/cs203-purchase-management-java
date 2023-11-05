package com.ticketpurchaseapp.purchase.service;

import java.util.List;

import com.ticketpurchaseapp.purchase.dto.User;
import com.ticketpurchaseapp.purchase.dto.UserAuth;

public interface UserService {
    public boolean isUserEmailVerified(String email);

    public boolean isUserVerified(String email, String mobile);
    
    public User getUser(String email, String mobile);

    public User getUser(String email);
    
    public boolean authenticateUser(String email, String mobile, String password, String groupId, String eventId, String queueId);

    public int recordLoginFailed(String ipAddress);

    public boolean isLoginLocked(String ipAddress);

    public void removeExpiredLoginRecords();
}
