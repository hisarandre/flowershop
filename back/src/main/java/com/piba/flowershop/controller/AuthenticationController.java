package com.piba.flowershop.controller;

import com.piba.flowershop.dto.AuthenticationRequest;
import com.piba.flowershop.dto.AuthenticationResponse;
import com.piba.flowershop.dto.RegistrationRequest;
import com.piba.flowershop.service.UserManagementService;
import org.springframework.web.bind.MethodArgumentNotValidException;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;


@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private static Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    @Autowired
    private UserManagementService userManagementService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Registration successful", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = AuthenticationResponse.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden",content = @Content)
    })
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegistrationRequest req, BindingResult bindingResult) throws MethodArgumentNotValidException {

        if (bindingResult.hasErrors()) {
            throw new MethodArgumentNotValidException(null, bindingResult);
        }

        logger.info("Creating new user: " + req.getLastName() + " " + req.getFirstName() );
        return ResponseEntity.ok(userManagementService.register(req));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = AuthenticationResponse.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden",content = @Content)
    })
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@Valid @RequestBody AuthenticationRequest req, BindingResult bindingResult) throws MethodArgumentNotValidException {

        if (bindingResult.hasErrors()) {
            throw new MethodArgumentNotValidException(null, bindingResult);
        }

        logger.info("User: " + req.getEmail() + " is logging in");
        return ResponseEntity.ok(userManagementService.login(req));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Refresh token successful", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = AuthenticationResponse.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden",content = @Content)
    })
    @PostMapping("/refresh")
    public ResponseEntity<AuthenticationResponse> refreshToken(@RequestBody AuthenticationResponse req) {
        return ResponseEntity.ok(userManagementService.refreshToken(req));
    }
}
