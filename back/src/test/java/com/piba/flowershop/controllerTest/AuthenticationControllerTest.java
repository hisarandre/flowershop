package com.piba.flowershop.controllerTest;

import com.piba.flowershop.controller.AuthenticationController;
import com.piba.flowershop.dto.AuthenticationRequest;
import com.piba.flowershop.dto.AuthenticationResponse;
import com.piba.flowershop.dto.RegistrationRequest;
import com.piba.flowershop.service.UserManagementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {

    @Mock
    private UserManagementService userManagementService;

    @InjectMocks
    private AuthenticationController authenticationController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authenticationController).build();
    }

    @Test
    void register_shouldReturnOk_whenValidRequest() throws Exception {
        RegistrationRequest req = new RegistrationRequest("John", "Doe", "M", "john.doe@example.com", "password");

        AuthenticationResponse response = new AuthenticationResponse();
        response.setRefreshToken("refreshToken");
        response.setToken("jwtToken");

        when(userManagementService.register(any(RegistrationRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"John\", \"lastName\":\"Doe\", \"gender\":\"M\", \"email\":\"john.doe@example.com\", \"password\":\"password\"}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"token\":\"jwtToken\", \"refreshToken\":\"refreshToken\"}"));
    }

    @Test
    void register_shouldReturnBadRequest_whenInvalidRequest() throws Exception {
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"\", \"lastName\":\"Doe\", \"email\":\"invalid-email\", \"password\":\"\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void login_shouldReturnOk_whenValidRequest() throws Exception {
        AuthenticationRequest req = new AuthenticationRequest("john.doe@example.com", "password");
        AuthenticationResponse response = new AuthenticationResponse();
        response.setRefreshToken("refreshToken");
        response.setToken("jwtToken");

        when(userManagementService.login(any(AuthenticationRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"john.doe@example.com\", \"password\":\"password\"}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"token\":\"jwtToken\", \"refreshToken\":\"refreshToken\"}"));
    }


    @Test
    void login_shouldReturnBadRequest_whenInvalidRequest() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"\", \"password\":\"\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void refreshToken_shouldReturnOk_whenValidRequest() throws Exception {
        AuthenticationResponse req = new AuthenticationResponse();
        req.setRefreshToken("refreshToken");
        req.setToken("jwtToken");
        AuthenticationResponse response = new AuthenticationResponse();
        response.setRefreshToken("newRefreshToken");
        response.setToken("newJwtToken");

        when(userManagementService.refreshToken(any(AuthenticationResponse.class))).thenReturn(response);

        mockMvc.perform(post("/api/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"jwtToken\":\"jwtToken\", \"refreshToken\":\"refreshToken\"}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"token\":\"newJwtToken\", \"refreshToken\":\"newRefreshToken\"}"));
    }

}