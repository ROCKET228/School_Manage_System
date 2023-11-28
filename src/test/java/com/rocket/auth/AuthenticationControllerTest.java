package com.rocket.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rocket.config.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AuthenticationController.class)
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthenticationService authenticationService;

    @MockBean
    private JwtService jwtService;

    @Test
    void registerUser_shouldReturnOk() throws Exception {
        // Arrange
        RegisterRequest registerRequest = new RegisterRequest("FirstName", "LatName","Mail", "password");
        AuthenticationResponse expectedResponse = new AuthenticationResponse("token");

        given(authenticationService.register(registerRequest))
                .willReturn(expectedResponse);

        // Act
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders
                .post("/SchoolManageSystem/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)));

        // Assert
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(expectedResponse.getToken()));
    }

    @Test
    void registerAdmin_shouldReturnOk() throws Exception {
        // Arrange
        RegisterRequest registerRequest = new RegisterRequest("adminUsername", "adminLatName","adminMail", "adminPassword");
        AuthenticationResponse expectedResponse = new AuthenticationResponse("adminToken");
        String adminKey = "adminKey";

        given(authenticationService.register(registerRequest, adminKey))
                .willReturn(expectedResponse);

        // Act
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders
                .post("/SchoolManageSystem/auth/register/admin")
                .param("adminKey", adminKey)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)));

        // Assert
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(expectedResponse.getToken()));
    }

    @Test
    void authenticateUser_shouldReturnOk() throws Exception {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest("mail", "password");
        AuthenticationResponse expectedResponse = new AuthenticationResponse("token");

        given(authenticationService.authenticate(authenticationRequest))
                .willReturn(expectedResponse);

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders
                .post("/SchoolManageSystem/auth/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authenticationRequest)));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(expectedResponse.getToken()));
    }
}
