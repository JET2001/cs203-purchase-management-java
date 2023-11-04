package com.ticketpurchaseapp.purchase.controller;

import com.ticketpurchaseapp.purchase.controller.UserController;
import com.ticketpurchaseapp.purchase.common.exception.InvalidArgsException;
import com.ticketpurchaseapp.purchase.dto.User;
import com.ticketpurchaseapp.purchase.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Test
    public void getUserByEmailAndMobile_Success() {
        String email = "user@example.com";
        String mobile = "1234567890";
        User user = new User();

        when(userService.getUser(email, mobile)).thenReturn(user);

        ResponseEntity<?> response = userController.getUserByEmailAndMobile(email, mobile);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(user);
    }

    @Test
    public void getUserByEmailAndMobile_InvalidArgsException() {
        String email = "user@example.com";
        String mobile = "1234567890";
        String errorMessage = "Invalid arguments provided";

        when(userService.getUser(email, mobile)).thenThrow(new InvalidArgsException(errorMessage));

        ResponseEntity<?> response = userController.getUserByEmailAndMobile(email, mobile);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
        assertThat(response.getBody()).isEqualTo(errorMessage);
    }

    @Test
    public void getUserByEmailAndMobile_Exception() {
        String email = "user@example.com";
        String mobile = "1234567890";
        String errorMessage = "An unexpected error occurred";

        when(userService.getUser(email, mobile)).thenThrow(new RuntimeException(errorMessage));

        ResponseEntity<?> response = userController.getUserByEmailAndMobile(email, mobile);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isEqualTo("Server Error: " + errorMessage);
    }

    @Test
    public void getUserByEmail_Success() {
        String email = "user@example.com";
        User user = new User();

        when(userService.getUser(email)).thenReturn(user);

        ResponseEntity<?> response = userController.getUserByEmail(email);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(user);
    }

    @Test
    public void getUserByEmail_InvalidArgsException() {
        String email = "user@example.com";
        String errorMessage = "Invalid arguments provided";

        when(userService.getUser(email)).thenThrow(new InvalidArgsException(errorMessage));

        ResponseEntity<?> response = userController.getUserByEmail(email);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
        assertThat(response.getBody()).isEqualTo(errorMessage);
    }

    @Test
    public void getUserByEmail_Exception() {
        String email = "user@example.com";
        String errorMessage = "An unexpected error occurred";

        when(userService.getUser(email)).thenThrow(new RuntimeException(errorMessage));

        ResponseEntity<?> response = userController.getUserByEmail(email);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isEqualTo("Server Error: " + errorMessage);
    }

    @Test
    public void isUserVerified_Success() {
        String email = "user@example.com";
        String mobile = "1234567890";

        when(userService.isUserVerified(email, mobile)).thenReturn(true);

        ResponseEntity<?> response = userController.isUserVerified(email, mobile);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(true);
    }

    @Test
    public void isUserVerified_InvalidArgsException() {
        String email = "user@example.com";
        String mobile = "1234567890";
        String errorMessage = "Invalid arguments provided";

        when(userService.isUserVerified(email, mobile)).thenThrow(new InvalidArgsException(errorMessage));

        ResponseEntity<?> response = userController.isUserVerified(email, mobile);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
        assertThat(response.getBody()).isEqualTo(errorMessage);
    }

    @Test
    public void isUserVerified_Exception() {
        String email = "user@example.com";
        String mobile = "1234567890";
        String errorMessage = "An unexpected error occurred";

        when(userService.isUserVerified(email, mobile)).thenThrow(new RuntimeException(errorMessage));

        ResponseEntity<?> response = userController.isUserVerified(email, mobile);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isEqualTo("Server Error: " + errorMessage);
    }
}
