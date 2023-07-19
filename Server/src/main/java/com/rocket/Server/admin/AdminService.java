package com.rocket.server.admin;

import com.rocket.server.auth.AuthenticationResponse;
import com.rocket.server.auth.RegisterRequest;
import com.rocket.server.classes.Class;
import com.rocket.server.classes.ClassRepository;
import com.rocket.server.config.JwtService;
import com.rocket.server.subject.Subject;
import com.rocket.server.subject.SubjectRepository;
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
    private final SubjectRepository subjectRepository;

    public User setTeacherRole(String userEmail){
        var user = userRepository.findByEmail(userEmail).orElseThrow();
        user.setRole(UserRole.TEACHER);
        return userRepository.save(user);
    }

    public User setStudentRole(String userEmail){
        var user = userRepository.findByEmail(userEmail).orElseThrow();
        user.setRole(UserRole.STUDENT);
        return userRepository.save(user);
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

    public Class createClasses(String className) {
        if(classRepository.findByName(className).isPresent()){
            throw new IllegalArgumentException("Class with name" + className + "already exist");
        }
        var classes = Class.builder().name(className).build();
        return classRepository.save(classes);
    }

    public Class setStudentToClass(String userEmail, String className) {
        if(userRepository.findByEmail(userEmail).isEmpty()){
            throw new IllegalArgumentException("User with this email "+ userEmail + " is not exist");
        }
        if(classRepository.findByName(className).isEmpty()){
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

    public Subject createSubject(String subjectName) {
        if(subjectRepository.findByName(subjectName).isPresent()){
            throw new IllegalArgumentException("Subject with name" + subjectName + "already exist");
        }
        var subject = Subject.builder().name(subjectName).build();
        return subjectRepository.save(subject);
    }

    public Subject setTeacherToSubject(String userEmail, String subjectName) {
        if(userRepository.findByEmail(userEmail).isEmpty()){
            throw new IllegalArgumentException("User with this email "+ userEmail + " is not exist");
        }
        if(subjectRepository.findByName(subjectName).isEmpty()){
            throw new IllegalArgumentException("Subject with this name " + subjectName + "  is not exist");
        }
        if(!userRepository.findByEmail(userEmail).orElseThrow().getRole().toString().equals(UserRole.TEACHER.toString())){
            throw new IllegalArgumentException("User with this email "+ userEmail + " has no role teacher");
        }
        Subject subject = subjectRepository.findByName(subjectName).get();
        User user = userRepository.findByEmail(userEmail).get();
        subject.enrolledTeacher(user);
        return subjectRepository.save(subject);
    }
}
