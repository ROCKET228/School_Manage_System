package com.rocket.server.admin;

import com.rocket.server.auth.AuthenticationResponse;
import com.rocket.server.auth.RegisterRequest;
import com.rocket.server.classes.Class;
import com.rocket.server.classes.ClassRepository;
import com.rocket.server.config.JwtService;
import com.rocket.server.user.UserRole;
import com.rocket.server.user.User;
import com.rocket.server.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final ClassRepository classRepository;

    public void setTeacherRole(String userEmail){
        var user = userRepository.findByEmail(userEmail).orElseThrow();
        user.setRole(UserRole.TEACHER);
        userRepository.save(user);
    }

    public void setStudentRole(String userEmail){
        var user = userRepository.findByEmail(userEmail).orElseThrow();
        user.setRole(UserRole.STUDENT);
        userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public AuthenticationResponse createStudent(RegisterRequest request) {
        var user = User.builder()
                .firstName(request.getFirstname())
                .lastName(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(UserRole.STUDENT)
                .build();
        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();

    }

    public AuthenticationResponse createTeacher(RegisterRequest request) {
        var user = User.builder()
                .firstName(request.getFirstname())
                .lastName(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(UserRole.TEACHER)
                .build();
        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();

    }

    public Class createClasses(String request) {
        if(classRepository.findByName(request).isPresent()){
            throw new IllegalArgumentException("Class with name" + request + "already exist");
        }
        var classes = Class.builder().name(request).build();
        return classRepository.save(classes);
    }

    public Class setStudentToClass(String userEmail, String className) {
        if(!userRepository.findByEmail(userEmail).isPresent()){
            throw new IllegalArgumentException("User with this email "+ userEmail + " is not exist");
        }
        if(!classRepository.findByName(className).isPresent()){
            throw new IllegalArgumentException("Class with this name " + className + "  is not exist");
        }
        if(!userRepository.findByEmail(userEmail).orElseThrow().getRole().toString().equals(UserRole.STUDENT.toString())){
           throw new IllegalArgumentException("User with this email "+ userEmail + " has no role student");
        }
        Class classes = classRepository.findByName(className).get();
        User user = userRepository.findByEmail(userEmail).get();
        classes.enrolledStudent(user);
        return classRepository.save(classes);
    }
}
