package com.ticketpurchaseapp.purchase.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ticketpurchaseapp.purchase.common.exception.InvalidArgsException;
import com.ticketpurchaseapp.purchase.controller.AuthController;
import com.ticketpurchaseapp.purchase.service.UserService;
import com.ticketpurchaseapp.purchase.util.JwtUtil;

public class AuthControllerTest {

    private AuthController authController;
    private UserService userService;
    private JwtUtil jwtUtil;

    @BeforeEach
    public void setUp() {
        userService = mock(UserService.class);
        jwtUtil = mock(JwtUtil.class);
        authController = new AuthController(userService);
    }

    // @Test
    // public void AuthenticateAndLoginUser_SuccessfulAuthentication() {
    //     // Arrange
    //     String email = "test@example.com";
    //     String mobile = "1234567890";
    //     String password = "password";
    //     String expectedJwt = "dummyJwtToken";

    //     when(userService.authenticateUser(email, mobile, password)).thenReturn(true);
    //     when(JwtUtil.generateToken(mobile)).thenReturn(expectedJwt);

    //     // Act
    //     ResponseEntity<?> response = authController.authenticateAndLoginUser(email, mobile, password);

    //     // Assert
    //     assertEquals(200, response.getStatusCode());
    //     assertEquals(expectedJwt, response.getBody());
    // }

    @Test
    public void AuthenticateAndLoginUser_FailedAuthentication() {
        // Arrange
        String email = "test@example.com";
        String mobile = "1234567890";
        String password = "password";

        when(userService.authenticateUser(email, mobile, password)).thenReturn(false);

        // Act
        ResponseEntity<?> response = authController.authenticateAndLoginUser(email, mobile, password);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(false, response.getBody());
    }

    @Test
    public void AuthenticateAndLoginUser_InvalidArgsException() {
        // Arrange
        String email = "test@example.com";
        String mobile = "1234567890";
        String password = "password";
        String errorMessage = "Invalid arguments";

        when(userService.authenticateUser(email, mobile, password)).thenThrow(new InvalidArgsException(errorMessage));

        // Act
        ResponseEntity<?> response = authController.authenticateAndLoginUser(email, mobile, password);

        // Assert
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
        assertEquals(errorMessage, response.getBody());
    }

    @Test
    public void AuthenticateAndLoginUser_Exception() {
        // Arrange
        String email = "test@example.com";
        String mobile = "1234567890";
        String password = "password";
        String errorMessage = "Internal server error";

        when(userService.authenticateUser(email, mobile, password)).thenThrow(new RuntimeException(errorMessage));

        // Act
        ResponseEntity<?> response = authController.authenticateAndLoginUser(email, mobile, password);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Server Error: " + errorMessage, response.getBody());
    }
}