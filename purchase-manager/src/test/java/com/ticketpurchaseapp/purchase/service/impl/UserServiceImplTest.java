package com.ticketpurchaseapp.purchase.service.impl;

import com.ticketpurchaseapp.purchase.common.exception.InvalidArgsException;
import com.ticketpurchaseapp.purchase.common.exception.UserException;
import com.ticketpurchaseapp.purchase.dto.User;
import com.ticketpurchaseapp.purchase.repository.UserRepository;
import com.ticketpurchaseapp.purchase.service.UserService;
import com.ticketpurchaseapp.purchase.util.Utility;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private Utility utility;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void testGetUser_ValidInput() {
        User user = new User();
        when(userRepository.findUserByEmailAndMobile(anyString(), anyString())).thenReturn(user);

        User result = userService.getUser("test@example.com", "1234567890");

        assertNotNull(result);
    }

    @Test
    public void testGetUser_InvalidInput() {

        assertThrows(InvalidArgsException.class, () -> {
            userService.getUser("invalid_email", "invalid_mobile");
        });
    }

    @Test
    public void testGetUser_UserNotFound() {
        when(userRepository.findUserByEmailAndMobile(anyString(), anyString())).thenReturn(null);

        UserException exception = assertThrows(UserException.class, () -> {
            userService.getUser("nonexistent@example.com", "1234567890");
        });

        assertEquals("user does not exist", exception.getMessage());
    }

    @Test
    public void testGetUserByEmail_ValidInput() {

        User user = new User();
        when(userRepository.findUserByEmail(anyString())).thenReturn(user);

        User result = userService.getUser("test@example.com");

        assertNotNull(result);
    }

    @Test
    public void testGetUserByEmail_InvalidInput() {

        assertThrows(InvalidArgsException.class, () -> {
            userService.getUser("invalid_email");
        });
    }

    @Test
    public void testGetUserByEmail_UserNotFound() {

        UserException exception = assertThrows(UserException.class, () -> {
            userService.getUser("nonexistent@example.com");
        });

        assertEquals("user does not exist", exception.getMessage());
    }

    @Test
    public void testIsUserVerified_UserVerified() {

        User user = new User();
        user.setVerified(true);
        when(userRepository.findUserByEmailAndMobile(anyString(), anyString())).thenReturn(user);

        boolean result = userService.isUserVerified("test@example.com", "1234567890");

        assertTrue(result);
    }

    @Test
    public void testIsUserVerified_UserNotVerified() {

        User user = new User();
        user.setVerified(false);
        when(userRepository.findUserByEmailAndMobile(anyString(), anyString())).thenReturn(user);

        boolean result = userService.isUserVerified("test@example.com", "1234567890");

        assertFalse(result);
    }

    @Test
    public void testIsUserVerified_InvalidInput() {

        assertThrows(InvalidArgsException.class, () -> {
            userService.isUserVerified("invalid_email", "invalid_mobile");
        });
    }

    @Test
    public void testIsUserEmailVerified_UserVerified() {

        User user = new User();
        user.setVerified(true);
        when(userRepository.findUserByEmail(anyString())).thenReturn(user);

        // Act
        boolean result = userService.isUserEmailVerified("test@example.com");

        // Assert
        assertTrue(result);
    }

    @Test
    public void testIsUserEmailVerified_UserNotVerified() {

        User user = new User();
        user.setVerified(false);
        when(userRepository.findUserByEmail(anyString())).thenReturn(user);

        boolean result = userService.isUserEmailVerified("test@example.com");

        assertFalse(result);
    }

    @Test
    public void testIsUserEmailVerified_InvalidInput() {

        assertThrows(InvalidArgsException.class, () -> {
            userService.isUserEmailVerified("invalid_email");
        });
    }

    @Test
    public void testAuthenticateUser_ValidInput() {

        when(userRepository.retrieveUserForAuth(anyString(), anyString())).thenReturn("hashed_password");

        boolean result = userService.authenticateUser("test@example.com", "1234567890", "hashed_password");

        assertTrue(result);
    }

    @Test
    public void testAuthenticateUser_InvalidInput() {

        assertThrows(InvalidArgsException.class, () -> {
            userService.authenticateUser("invalid_email", "invalid_mobile", "hashed_password");
        });
    }

    @Test
    public void testAuthenticateUser_PasswordMismatch() {

        when(userRepository.retrieveUserForAuth(anyString(), anyString())).thenReturn("hashed_password");

        boolean result = userService.authenticateUser("test@example.com", "1234567890", "wrong_password");

        assertFalse(result);
    }
}
