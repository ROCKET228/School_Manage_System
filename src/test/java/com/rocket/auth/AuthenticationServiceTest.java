package com.rocket.auth;

import com.rocket.config.JwtService;
import com.rocket.user.User;
import com.rocket.user.UserRepository;
import com.rocket.user.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;
    @Mock
    private AuthenticationManager authenticationManager;
    @InjectMocks
    private AuthenticationService authenticationService;

    private RegisterRequest registerRequest;
    private User user;
    private AuthenticationRequest authenticationRequest;

    @BeforeEach
    public void init(){
        registerRequest = new RegisterRequest("FirstName", "LastName", "email@mail.com", "password123");
        authenticationRequest = new AuthenticationRequest("email@mail.com", "password123");
        user = User.builder()
                .firstName("FirstName")
                .lastName("LastName")
                .email("email@mail.com")
                .password(passwordEncoder.encode("password123"))
                .build();
    }

    @Test
    void register() {
        when(userRepository.findByEmail(registerRequest.getEmail())).thenReturn(Optional.empty());
        when(jwtService.generateToken(any(User.class))).thenReturn("mockedToken");

        AuthenticationResponse authenticationResponse = authenticationService.register(registerRequest);

        assertNotNull(authenticationResponse);
        assertEquals("mockedToken", authenticationResponse.getToken());
        user.setRole(UserRole.USER);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void authenticate() {
        when( authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword())))
                .thenReturn(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword()));
        when(userRepository.findByEmail(authenticationRequest.getEmail())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(any(User.class))).thenReturn("mockedToken");

        AuthenticationResponse authenticationResponse = authenticationService.authenticate(authenticationRequest);

        assertNotNull(authenticationResponse);
        assertEquals("mockedToken", authenticationResponse.getToken());
    }
}