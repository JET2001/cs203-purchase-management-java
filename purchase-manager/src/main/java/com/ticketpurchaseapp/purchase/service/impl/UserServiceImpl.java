package com.ticketpurchaseapp.purchase.service.impl;

import java.security.DrbgParameters.Reseed;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ticketpurchaseapp.purchase.common.exception.InvalidArgsException;
import com.ticketpurchaseapp.purchase.common.exception.UserException;
import com.ticketpurchaseapp.purchase.dto.User;
import com.ticketpurchaseapp.purchase.repository.UserRepository;
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
        } catch(UserException e){
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
    public boolean authenticateUser(String email, String mobile, String password){
        Utility utility = new Utility();
        if (!utility.emailWhitelist(email) || !utility.isInputSafe(mobile) || password==null) {
            throw new InvalidArgsException("invalid login credentials - failed preliminary check");
        }
    
        String userPwd = userRepository.retrieveUserForAuth(email, mobile);
        return password.equals(userPwd);
    }
}
