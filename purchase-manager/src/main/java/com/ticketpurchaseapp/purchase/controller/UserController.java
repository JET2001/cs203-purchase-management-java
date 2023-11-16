package com.ticketpurchaseapp.purchase.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ticketpurchaseapp.purchase.common.exception.InvalidArgsException;
import com.ticketpurchaseapp.purchase.dto.User;
import com.ticketpurchaseapp.purchase.service.UserService;
import com.ticketpurchaseapp.purchase.util.JwtUtil;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@CrossOrigin(allowedHeaders = {"Authorization", "Content-Type"}, exposedHeaders = {"Access-Control-Allow-Origin", "Access-Control-Allow-Credentials"})
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    public ResponseEntity<?> getUserByEmailAndMobile(@RequestParam String email, @RequestParam String mobile) {
        try {
            User user = userService.getUser(email, mobile);
            return ResponseEntity.ok().body(user);
        } catch (InvalidArgsException e) {
            log.error("Get-user error: ", e);
            return ResponseEntity.unprocessableEntity().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Server Error: " + e.getMessage());
        }
    }

    @GetMapping("/{email}")
    public ResponseEntity<?> getUserByEmail(@PathVariable String email) {
        try {
            User user = userService.getUser(email);
            return ResponseEntity.ok().body(user);
        } catch (InvalidArgsException e) {
            log.error("Get-user error: ", e);
            return ResponseEntity.unprocessableEntity().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Server Error: " + e.getMessage());
        }
    }

    @GetMapping("/is-verified")
    public ResponseEntity<?> isUserVerified(@RequestParam String email, @RequestParam String mobile) {
        try {
            boolean success = userService.isUserVerified(email, mobile);
            return ResponseEntity.ok().body(success);
        } catch (InvalidArgsException e) {
            log.error("User verification error: ", e);
            return ResponseEntity.unprocessableEntity().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Server Error: " + e.getMessage());
        }
    }

    // @PostMapping("/auth/login")
    // public ResponseEntity<?> authenticateAndLoginUser(@RequestBody User user) {
    //     try {
    //         if (userService.authenticateUser(user.getEmail(), user.getMobile(), user.getPassword())) {
    //             String jwt = JwtUtil.generateToken(user.getMobile()); // I'm lazy
    //             return ResponseEntity.ok().body(jwt);
    //         }
    //         return ResponseEntity.ok().body(false);
    //     } catch (InvalidArgsException e) {
    //         log.error("Verify multiple error: ", e);
    //         return ResponseEntity.unprocessableEntity().body(e.getMessage());
    //     } catch (Exception e) {
    //         System.out.println(e.getStackTrace());
    //         return ResponseEntity.internalServerError().body("Server Error: " + e.getMessage());
    //     }
    // }
}
