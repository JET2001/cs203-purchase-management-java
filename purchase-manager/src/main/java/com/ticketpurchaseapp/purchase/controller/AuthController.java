package com.ticketpurchaseapp.purchase.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestHeader;

import com.ticketpurchaseapp.purchase.common.exception.InvalidArgsException;
import com.ticketpurchaseapp.purchase.common.exception.UserException;
import com.ticketpurchaseapp.purchase.dto.User;
import com.ticketpurchaseapp.purchase.service.UserService;
import com.ticketpurchaseapp.purchase.util.JwtUtil;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@CrossOrigin(allowedHeaders = { "Authorization", "Content-Type", "email", "mobile", "password", "ipAddress",
        "groupId", "eventId", "queueId" }, exposedHeaders = { "Access-Control-Allow-Origin",
                "Access-Control-Allow-Credentials", "Access-Control-Allow-Headers" })
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateAndLoginUser(@RequestHeader("email") String email,
            @RequestHeader("mobile") String mobile, @RequestHeader("password") String password,
            @RequestHeader("ipAddress") String ipAddress, @RequestHeader("groupId") String groupId,
            @RequestHeader("eventId") String eventId, @RequestHeader("queueId") String queueId) {
        userService.removeExpiredLoginRecords();
        if (userService.isLoginLocked(ipAddress)) {
            return ResponseEntity.status(403).body("Too many login attempts. Please try again in 15 minutes.");
        }
        try {
            if (userService.authenticateUser(email, mobile, password, ipAddress, groupId, eventId, queueId)) {
                String jwt = JwtUtil.generateToken(mobile);
                return ResponseEntity.ok().body(jwt);
            }
            userService.recordLoginFailed(ipAddress);

            return ResponseEntity.internalServerError()
                    .body("Internal server error at login: Please contact us regarding this issue.");
        } catch (InvalidArgsException e) {
            log.error("Verify multiple error: ", e);
            userService.recordLoginFailed(ipAddress);
            return ResponseEntity.unprocessableEntity().body(e.getMessage());

        } catch (UserException e) {
            log.error(e.getMessage());
            userService.recordLoginFailed(ipAddress);
            return ResponseEntity.status(403).body(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getStackTrace().toString());
            userService.recordLoginFailed(ipAddress);
            return ResponseEntity.internalServerError().body("Server Error: " + e.getMessage());
        }
    }
}
