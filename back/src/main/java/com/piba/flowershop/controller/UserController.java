package com.piba.flowershop.controller;

import com.piba.flowershop.dto.AuthenticationResponse;
import com.piba.flowershop.model.User;
import com.piba.flowershop.service.UserManagementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class UserController {

    private static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserManagementService userManagementService;


    @GetMapping("/admin/get-all-users")
    public ResponseEntity<AuthenticationResponse> getAllUsers() {
        return ResponseEntity.ok(userManagementService.getAllUsers());
    }

    @GetMapping("/admin/get-users/{userId}")
    public ResponseEntity<AuthenticationResponse> getUserByID(@PathVariable String userId) {
        return ResponseEntity.ok(userManagementService.getUserById(UUID.fromString(userId)));
    }

    @PutMapping("/admin/update/{userId}")
    public ResponseEntity<AuthenticationResponse> updateUser(@PathVariable String userId, @RequestBody User user) {
        return ResponseEntity.ok(userManagementService.updateUser(UUID.fromString(userId), user));
    }

    @DeleteMapping("/admin/delete/{userId}")
    public ResponseEntity<AuthenticationResponse> deleteUSer(@PathVariable String userId) {
        return ResponseEntity.ok(userManagementService.deleteUser(UUID.fromString(userId)));
    }

    @GetMapping("/adminuser/get-current-user")
    public ResponseEntity<AuthenticationResponse> getUserProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        AuthenticationResponse response = userManagementService.getUserInfo(email);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }


}