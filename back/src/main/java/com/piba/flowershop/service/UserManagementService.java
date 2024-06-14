package com.piba.flowershop.service;
import com.piba.flowershop.exception.UserAlreadyExistsException;
import com.piba.flowershop.common.UserRole;
import com.piba.flowershop.dto.AuthenticationRequest;
import com.piba.flowershop.dto.RegistrationRequest;
import com.piba.flowershop.dto.AuthenticationResponse;
import com.piba.flowershop.model.User;
import com.piba.flowershop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserManagementService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;


    public AuthenticationResponse register(RegistrationRequest registrationRequest) {
        AuthenticationResponse resp = new AuthenticationResponse();

        Optional<User> existingUser = this.userRepository.findByEmail(registrationRequest.getEmail());

        if (existingUser.isPresent()){
            throw new UserAlreadyExistsException("User with email " + registrationRequest.getEmail() + " already exists");
        }

        try {
            User user = new User();
            user.setEmail(registrationRequest.getEmail());
            user.setRole(UserRole.USER);
            user.setLastName(registrationRequest.getLastName());
            user.setFirstName(registrationRequest.getFirstName());
            user.setGender(registrationRequest.getGender());
            user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
            User userResult = userRepository.save(user);

            if (userResult.getId() == null) {
                resp.setUser((userResult));
                resp.setMessage("User Saved Successfully");
                resp.setStatusCode(200);
            }

        } catch (Exception e) {
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }

        return resp;
    }


    public AuthenticationResponse login(AuthenticationRequest req) {
        AuthenticationResponse response = new AuthenticationResponse();
        try {
            authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(req.getEmail(),
                            req.getPassword()));

            var user = userRepository.findByEmail(req.getEmail()).orElseThrow();
            var jwt = jwtService.generateToken(user);
            var refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);

            response.setStatusCode(200);
            response.setToken(jwt);
            response.setRole(user.getRole());
            response.setRefreshToken(refreshToken);
            response.setExpirationTime("24Hrs");
            response.setMessage("Successfully Logged In");

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
        }

        return response;
    }


    public AuthenticationResponse refreshToken(AuthenticationResponse refreshTokenRequest) {
        AuthenticationResponse response = new AuthenticationResponse();

        try {
            String ourEmail = jwtService.extractUsername(refreshTokenRequest.getToken());
            User users = userRepository.findByEmail(ourEmail).orElseThrow();

            if (jwtService.isTokenValid(refreshTokenRequest.getToken(), users)) {
                var jwt = jwtService.generateToken(users);
                response.setStatusCode(200);
                response.setToken(jwt);
                response.setRefreshToken(refreshTokenRequest.getToken());
                response.setExpirationTime("24Hr");
                response.setMessage("Successfully Refreshed Token");
            }

            response.setStatusCode(200);
            return response;

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
            return response;
        }
    }


    public AuthenticationResponse getAllUsers() {
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();

        try {
            List<User> result = userRepository.findAll();

            if (!result.isEmpty()) {
                //requestResponse.setUserList(result);
                authenticationResponse.setStatusCode(200);
                authenticationResponse.setMessage("Successful");
            } else {
                authenticationResponse.setStatusCode(404);
                authenticationResponse.setMessage("No users found");
            }

            return authenticationResponse;
        } catch (Exception e) {
            authenticationResponse.setStatusCode(500);
            authenticationResponse.setMessage("Error occurred: " + e.getMessage());
            return authenticationResponse;
        }
    }


    public AuthenticationResponse getUserById(UUID id) {
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();

        try {
            User userById = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User Not found"));
            authenticationResponse.setUser(userById);
            authenticationResponse.setStatusCode(200);
            authenticationResponse.setMessage("User with id '" + id + "' found successfully");
        } catch (Exception e) {
            authenticationResponse.setStatusCode(500);
            authenticationResponse.setMessage("Error occurred: " + e.getMessage());
        }

        return authenticationResponse;
    }


    public AuthenticationResponse deleteUser(UUID userId) {
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();

        try {
            Optional<User> userOptional = userRepository.findById(userId);

            if (userOptional.isPresent()) {
                userRepository.deleteById(userId);
                authenticationResponse.setStatusCode(200);
                authenticationResponse.setMessage("User deleted successfully");
            } else {
                authenticationResponse.setStatusCode(404);
                authenticationResponse.setMessage("User not found for deletion");
            }

        } catch (Exception e) {
            authenticationResponse.setStatusCode(500);
            authenticationResponse.setMessage("Error occurred while deleting user: " + e.getMessage());
        }
        return authenticationResponse;
    }

    public AuthenticationResponse updateUser(UUID userId, User updatedUser) {
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();

        try {
            Optional<User> userOptional = userRepository.findById(userId);

            if (userOptional.isPresent()) {
                User existingUser = userOptional.get();
                existingUser.setEmail(updatedUser.getEmail());
                existingUser.setRole(updatedUser.getRole());

                // Check if password is present in the request
                if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                    // Encode the password and update it
                    existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
                }

                User savedUser = userRepository.save(existingUser);
                authenticationResponse.setUser(savedUser);
                authenticationResponse.setStatusCode(200);
                authenticationResponse.setMessage("User updated successfully");
            } else {
                authenticationResponse.setStatusCode(404);
                authenticationResponse.setMessage("User not found for update");
            }

        } catch (Exception e) {
            authenticationResponse.setStatusCode(500);
            authenticationResponse.setMessage("Error occurred while updating user: " + e.getMessage());
        }
        return authenticationResponse;
    }


    public AuthenticationResponse getUserInfo(String email){
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();

        try {
            Optional<User> userOptional = userRepository.findByEmail(email);

            if (userOptional.isPresent()) {
                authenticationResponse.setUser(userOptional.get());
                authenticationResponse.setStatusCode(200);
                authenticationResponse.setMessage("successful");
            } else {
                authenticationResponse.setStatusCode(404);
                authenticationResponse.setMessage("User not found for update");
            }

        } catch (Exception e){
            authenticationResponse.setStatusCode(500);
            authenticationResponse.setMessage("Error occurred while getting user info: " + e.getMessage());
        }
        return authenticationResponse;
    }
}
