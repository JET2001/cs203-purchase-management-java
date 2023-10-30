package com.ticketpurchaseapp.purchase.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestHeader;

import com.ticketpurchaseapp.purchase.common.exception.InvalidArgsException;
import com.ticketpurchaseapp.purchase.dto.User;
import com.ticketpurchaseapp.purchase.service.UserService;
import com.ticketpurchaseapp.purchase.util.JwtUtil;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@CrossOrigin
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateAndLoginUser(@RequestHeader("email") String email, @RequestHeader("mobile") String mobile, @RequestHeader("password") String password, @RequestHeader("ip_address") String ipAddress) {
        try {
            userService.removeExpiredLoginRecords();
            if (userService.isLoginLocked(ipAddress)) {
                return ResponseEntity.ok().body(false);
            }
            if (userService.authenticateUser(email, mobile , password)) {
                String jwt = JwtUtil.generateToken(mobile);
                return ResponseEntity.ok().body(jwt);
            } else {
                userService.recordLoginFailed(ipAddress);
            }
            return ResponseEntity.ok().body(false);
        } catch (InvalidArgsException e) {
            log.error("Verify multiple error: ", e);
            return ResponseEntity.unprocessableEntity().body(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
            return ResponseEntity.internalServerError().body("Server Error: " + e.getMessage());
        }
    }
}
