package com.rocket.admin;

import com.rocket.classes.Class;
import com.rocket.marks.Marks;
import com.rocket.marks.MarksRepository;
import com.rocket.marks.MarksRequest;
import com.rocket.user.UserRepository;
import com.rocket.user.UserRole;
import com.rocket.auth.AuthenticationResponse;
import com.rocket.auth.RegisterRequest;
import com.rocket.classes.ClassRepository;
import com.rocket.config.JwtService;
import com.rocket.subject.Subject;
import com.rocket.subject.SubjectRepository;
import com.rocket.user.User;
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
    private final MarksRepository marksRepository;

    public User setTeacherRole(String userEmail){
        User teacher = userRepository.findByEmail(userEmail).orElseThrow(() -> new IllegalArgumentException("User with this email "+ userEmail + " is not exist"));
        teacher.setRole(UserRole.TEACHER);
        return userRepository.save(teacher);
    }

    public User setStudentRole(String userEmail){
        User student = userRepository.findByEmail(userEmail).orElseThrow(() -> new IllegalArgumentException("User with this email "+ userEmail + " is not exist"));
        student.setRole(UserRole.STUDENT);
        return userRepository.save(student);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public AuthenticationResponse createStudent(RegisterRequest request) {
        var student = User.builder()
                .firstName(request.getFirstname())
                .lastName(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(UserRole.STUDENT)
                .build();
        userRepository.save(student);
        var jwtToken = jwtService.generateToken(student);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();

    }

    public AuthenticationResponse createTeacher(RegisterRequest request) {
        var teacher = User.builder()
                .firstName(request.getFirstname())
                .lastName(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(UserRole.TEACHER)
                .build();
        userRepository.save(teacher);
        var jwtToken = jwtService.generateToken(teacher);
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
        Class classes = classRepository.findByName(className).orElseThrow(() -> new IllegalArgumentException("Class with this name " + className + "  is not exist"));
        User student = userRepository.findByEmail(userEmail).orElseThrow(() -> new IllegalArgumentException("User with this email "+ userEmail + " is not exist"));
        if(!student.getRole().toString().equals(UserRole.STUDENT.toString())){
           throw new IllegalArgumentException("User with this email "+ userEmail + " has no role student");
        }
        classes.enrolledStudent(student);
        if(marksRepository.findAllByClasses(classes).isPresent()){
            Marks marks = marksRepository.findAllByClasses(classes).orElseThrow(() -> new IllegalArgumentException("Marks table not found"));
            Subject subject = marks.getSubject();
            User teacher = marks.getTeacher();
            Marks newMarks =  Marks.builder().classes(classes)
                    .subject(subject)
                    .teacher(teacher)
                    .student(student)
                    .build();

            marksRepository.save(newMarks);
        }
        return classRepository.save(classes);
    }

    public Subject createSubject(String subjectName) {
        if(subjectRepository.findByName(subjectName).isPresent()){
            throw new IllegalArgumentException("Subject with nam e" + subjectName + " already exist");
        }
        var subject = Subject.builder().name(subjectName).build();
        return subjectRepository.save(subject);
    }

    public Subject setTeacherToSubject(String userEmail, String subjectName) {
        Subject subject = subjectRepository.findByName(subjectName).orElseThrow(() -> new IllegalArgumentException("Subject with this name " + subjectName + "  is not exist"));
        User teacher = userRepository.findByEmail(userEmail).orElseThrow(() -> new IllegalArgumentException("User with this email "+ userEmail + " is not exist"));
        if(!teacher.getRole().toString().equals(UserRole.TEACHER.toString())){
            throw new IllegalArgumentException("User with this email "+ userEmail + " has no role teacher");
        }
        subject.enrolledTeacher(teacher);
        return subjectRepository.save(subject);
    }

    public String createMarks(MarksRequest request) {
        Class classEntity = classRepository.findByName(request.getClassname()).orElseThrow(() -> new IllegalArgumentException("Class not found"));
        Subject subject = subjectRepository.findByName(request.getSubjectname()).orElseThrow(() -> new IllegalArgumentException("Subject not found"));
        User teacher = userRepository.findByEmail(request.getTeacheremail()).orElseThrow(() -> new IllegalArgumentException("Teacher not found"));
        if(!teacher.getRole().equals(UserRole.TEACHER)){
            throw new IllegalArgumentException("User has no role teacher");
        }
        if(!subject.getEnrolledTeachers().contains(teacher)){
            throw new IllegalArgumentException("That teacher can not teach that subject");
        }
        for (User student : classEntity.getEnrolledStudents()) {
            Marks marks =  Marks.builder().classes(classEntity)
                    .subject(subject)
                    .teacher(teacher)
                    .student(student)
                    .build();

            marksRepository.save(marks);
        }
        return "Marks table successfully created";
    }

    public User deleteUser(String userEmail) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new IllegalArgumentException("User not found"));
        userRepository.delete(user);
        return user;
    }

    public String deleteClasses(String className) {
        Class classEntity = classRepository.findByName(className).orElseThrow( () -> new IllegalArgumentException("Class not found"));
        classRepository.delete(classEntity);
        return "Successfully deleted class " + className;
    }

    public String deleteSubject(String subjectName) {
        Subject subject = subjectRepository.findByName(subjectName).orElseThrow( () -> new IllegalArgumentException("Subject not found"));
        subjectRepository.delete(subject);
        return "Successfully deleted subject " + subjectName;
    }

    public String deleteMarks(String className, String subjectName) {
        Class classEntity = classRepository.findByName(className).orElseThrow( () -> new IllegalArgumentException("Class not found"));
        Subject subject = subjectRepository.findByName(subjectName).orElseThrow( () -> new IllegalArgumentException("Subject not found"));
        marksRepository.deleteAllByClassesAndSubject(classEntity, subject);
        return "Successfully deleted class "+ className +" marks table, in subject " + subjectName;
    }

    public User unsetTeacherFromSubject(String userEmail, String subjectName) {
        User teacher = userRepository.findByEmail(userEmail).orElseThrow(() -> new IllegalArgumentException("Teacher not found"));
        if(!teacher.getRole().equals(UserRole.TEACHER)){
            throw new IllegalArgumentException("User has no role teacher");
        }
        Subject subject = subjectRepository.findByName(subjectName).orElseThrow( () -> new IllegalArgumentException("Subject not found"));
        subject.unrolledTeacher(teacher);
        subjectRepository.save(subject);
        return teacher;
    }

    public User unsetStudentFromClass(String userEmail, String className) {
        User student = userRepository.findByEmail(userEmail).orElseThrow(() -> new IllegalArgumentException("User with this email "+ userEmail + " is not exist"));
        if(!student.getRole().toString().equals(UserRole.STUDENT.toString())){
            throw new IllegalArgumentException("User with this email "+ userEmail + " has no role student");
        }
        Class classEntity = classRepository.findByName(className).orElseThrow( () -> new IllegalArgumentException("Class not found"));
        classEntity.unrolledStudent(student);
        classRepository.save(classEntity);
        return student;
    }

    public User changeTeacherInMarksTable(MarksRequest request) {
        User teacher = userRepository.findByEmail(request.getTeacheremail()).orElseThrow(() -> new IllegalArgumentException("Teacher "+ request.getTeacheremail() + " not found"));
        if(!teacher.getRole().equals(UserRole.TEACHER)){
            throw new IllegalArgumentException("User has no role teacher");
        }
        Class classEntity = classRepository.findByName(request.getClassname()).orElseThrow( () -> new IllegalArgumentException("Class "+ request.getClassname() + " not found"));
        Subject subject = subjectRepository.findByName(request.getSubjectname()).orElseThrow( () -> new IllegalArgumentException("Subject not found"));
        Marks marks = marksRepository.findByClassesAndSubject(classEntity, subject).orElseThrow(() -> new IllegalArgumentException("Marks table not found"));
        marks.setTeacher(teacher);
        marksRepository.save(marks);
        return teacher;
    }


    public List<Class> getAllClass() {
        return classRepository.findAll();
    }

    public List<Subject> getAllSubject() {
        return subjectRepository.findAll();
    }

    public List<Marks> getAllMarks() {
        return marksRepository.findAll();
    }
}
