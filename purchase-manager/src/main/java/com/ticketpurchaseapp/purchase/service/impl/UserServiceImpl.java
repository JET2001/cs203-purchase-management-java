package com.ticketpurchaseapp.purchase.service.impl;

import java.security.DrbgParameters.Reseed;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ticketpurchaseapp.purchase.common.exception.InvalidArgsException;
import com.ticketpurchaseapp.purchase.common.exception.UserException;
import com.ticketpurchaseapp.purchase.dto.Queue;
import com.ticketpurchaseapp.purchase.dto.User;
import com.ticketpurchaseapp.purchase.dto.UserAuth;
import com.ticketpurchaseapp.purchase.repository.UserRepository;
import com.ticketpurchaseapp.purchase.service.EventRegisterService;
import com.ticketpurchaseapp.purchase.service.UserService;
import com.ticketpurchaseapp.purchase.util.Utility;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getUser(String email, String mobile) {
        Utility utility = new Utility();
        if (!utility.emailWhitelist(email) || !utility.isInputSafe(mobile)) {
            throw new InvalidArgsException("Invalid email and/or mobile");
        }

        User user = userRepository.findUserByEmailAndMobile(email, mobile);
        if (user == null)
            throw new UserException("user does not exist");
        return user;
    }

    @Override
    public User getUser(String email) {
        Utility utility = new Utility();
        if (!utility.emailWhitelist(email)) {
            throw new InvalidArgsException("Invalid email");
        }
        User user = userRepository.findUserByEmail(email);
        if (user == null)
            throw new UserException("user does not exist");
        return user;
    }

    @Override
    public boolean isUserVerified(String email, String mobile) {
        Utility utility = new Utility();
        if (!utility.emailWhitelist(email) || !utility.isInputSafe(mobile)) {
            throw new InvalidArgsException("Invalid email and/or mobile");
        }
        try {
            User user = getUser(email, mobile);
            return (user != null) && user.isVerified();
        } catch (UserException e) {
            return false;
        }
    }

    @Override
    public boolean isUserEmailVerified(String email) {
        Utility utility = new Utility();
        if (!utility.emailWhitelist(email)) {
            throw new InvalidArgsException("Invalid email");
        }
        User user = userRepository.findUserByEmail(email);
        return (user != null) && user.isVerified();
    }

    @Override
    public boolean authenticateUser(String email, String mobile, String password, String ipAddress, String groupId, String eventId, 
            String queueId) {
        Utility utility = new Utility();
        if (!utility.emailWhitelist(email) || !utility.isInputSafe(mobile) || password == null) {
            throw new InvalidArgsException("invalid login credentials - failed preliminary check");
        }

        UserAuth userAuth = userRepository.retrieveUserForAuth(email, mobile);

        if (!utility.checkPassword(password, userAuth.getPassword())) {
            throw new UserException("Email or password is incorrect");
        }
        if (!userAuth.isAllowLogin()) {
            throw new UserException("Email is not verified");
        }

        // Check if user is indeed part of this group
        System.out.println("userAuth.id = " + userAuth.getUserId() +" " + groupId);
        if (!userRepository.isUserInGroup(userAuth.getUserId(), groupId)) {
            throw new UserException("Invalid user");
        }

        // Check if user's group has registered for this queue.
        if (!userRepository.hasGroupRegisteredForThisQueue(groupId, queueId, eventId)) {
            throw new UserException("Invalid user group for event");
        }
        // Check if the queue has already closed
        if (!userRepository.isQueueStillOnGoing(queueId)){
            throw new UserException("The queue you have registered for has closed");
        }
        // Check if there is already one member logged in to the purchase app, if so, we should deny all other entry
        String queueingGroupMemberID = userRepository.getQueueingGroupMember(groupId);

        boolean isUserQueueing = userAuth.getUserId().equals(queueingGroupMemberID);
        
        if (queueingGroupMemberID != null && !isUserQueueing){
            throw new UserException("Another group member is queueing on your behalf");
        }

        // If all these checks pass, log the successful login and allow log in.
        userRepository.recordLoginSuccess(ipAddress, groupId, userAuth.getUserId(), LocalDateTime.now());
        return true;
    }

    @Override
    public int recordLoginFailed(String ipAddress) {
        LocalDateTime now = LocalDateTime.now();
        return userRepository.recordLoginFailed(ipAddress, now);
    }

    @Override
    public boolean isLoginLocked(String ipAddress) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime within15minutes = now.minusMinutes(15);
        // userRepository.deleteExpiredRecords(within15minutes);
        return userRepository.isLoginLocked(ipAddress, within15minutes);
    }

    public void removeExpiredLoginRecords() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime within15minutes = now.minusMinutes(15);
        userRepository.removeExpiredLoginRecords(within15minutes);
    }
}
