package com.rocket.admin;


import com.rocket.auth.AuthenticationResponse;
import com.rocket.auth.RegisterRequest;
import com.rocket.classes.Class;
import com.rocket.classes.ClassRepository;
import com.rocket.config.JwtService;
import com.rocket.user.User;

import com.rocket.user.UserRepository;
import com.rocket.user.UserRole;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;



@ExtendWith(MockitoExtension.class)
class AdminServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private ClassRepository classRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AdminService adminService;

    private RegisterRequest registerRequest;
    private Class classes;

    @BeforeEach
    public void init(){
        registerRequest = new RegisterRequest("John", "Doe", "john.doe@example.com", "password123");
        classes = Class.builder().name("11-B").build();
    }


    @Test
    void setTeacherRole() {

    }

    @Test
    void setStudentRole() {
    }

    @Test
    void getAllUsers() {
    }

    @Test
    void createStudent() {
    }

    @Test
    void createTeacher() {

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");

        when(jwtService.generateToken(any(User.class))).thenReturn("mockedToken");

        // Act
        AuthenticationResponse authenticationResponse = adminService.createTeacher(registerRequest);

        // Assert
        assertNotNull(authenticationResponse);
        assertEquals("mockedToken", authenticationResponse.getToken());


        verify(userRepository, times(1)).save(argThat(user ->
                user.getFirstName().equals("John") &&
                        user.getLastName().equals("Doe") &&
                        user.getEmail().equals("john.doe@example.com") &&
                        user.getPassword().equals("hashedPassword") &&
                        user.getRole().equals(UserRole.TEACHER)
        ));

        verify(userRepository, times(1)).findByEmail("john.doe@example.com");
    }

    @Test
    void createClasses() {

        when(classRepository.save(Mockito.any(Class.class))).thenReturn(classes);

        String savedClass = adminService.createClasses("11-B");

        Assertions.assertThat(savedClass).isEqualTo("Class 11-B successfully created");
    }

    @Test
    void setStudentToClass() {
    }

    @Test
    void createSubject() {
    }

    @Test
    void setTeacherToSubject() {
    }

    @Test
    void createMarks() {
    }

    @Test
    void deleteUser() {
    }

    @Test
    void deleteClasses() {
    }

    @Test
    void deleteSubject() {
    }

    @Test
    void deleteMarks() {
    }

    @Test
    void unsetTeacherFromSubject() {
    }

    @Test
    void unsetStudentFromClass() {
    }

    @Test
    void changeTeacherInMarksTable() {
    }

    @Test
    void getAllClass() {
    }

    @Test
    void getAllSubject() {
    }

    @Test
    void getAllMarks() {
    }
}