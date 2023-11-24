package com.rocket.admin;


import com.rocket.auth.AuthenticationResponse;
import com.rocket.auth.RegisterRequest;
import com.rocket.classes.Class;
import com.rocket.classes.ClassRepository;
import com.rocket.classes.ClassResponse;
import com.rocket.config.JwtService;
import com.rocket.marks.ClassMarksResponse;
import com.rocket.marks.Marks;
import com.rocket.marks.MarksRepository;
import com.rocket.marks.MarksTableRequest;
import com.rocket.subject.Subject;
import com.rocket.subject.SubjectRepository;
import com.rocket.subject.SubjectResponse;
import com.rocket.user.User;

import com.rocket.user.UserRepository;
import com.rocket.user.UserResponse;
import com.rocket.user.UserRole;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
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
    private SubjectRepository subjectRepository;
    @Mock
    private MarksRepository marksRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AdminService adminService;

    private RegisterRequest registerRequest;
    private String subjectName;
    private String studentEmail;
    private String teacherEmail;
    private String className;
    private String userEmail;
    private User student;
    private User teacher;
    private User user;
    private Subject subject;
    private Marks marks;
    private Class classes;
    private  List<User> allUsersList;
    private List<Marks> marksList;
    private MarksTableRequest marksTableRequest;

    @BeforeEach
    public void init(){
        registerRequest = new RegisterRequest("John", "Doe", "john.doe@example.com", "password123");
        marksTableRequest = new MarksTableRequest(className, subjectName, teacherEmail);
        className = "11-B";
        subjectName = "Math";

        userEmail = "user@mail.com";
        user = new User();
        user.setEmail(userEmail);
        user.setRole(UserRole.USER);

        teacherEmail = "teacher@mail.com";
        teacher = new User();
        teacher.setEmail(teacherEmail);
        teacher.setRole(UserRole.TEACHER);

        studentEmail = "student@mail.com";
        student = new User();
        student.setEmail(studentEmail);
        student.setRole(UserRole.STUDENT);

        classes = new Class();
        classes.setName(className);
        classes.enrolledStudent(student);

        subject = new Subject();
        subject.setName(subjectName);
        subject.enrolledTeacher(teacher);

        marks = new Marks();
        marks.setClasses(classes);
        marks.setSubject(subject);
        marks.setTeacher(teacher);
        marks.setStudent(student);

        allUsersList = new ArrayList<>();
        allUsersList.add(teacher);
        allUsersList.add(student);

        marksList = new ArrayList<>();
        marksList.add(marks);
    }


    @Test
    void setTeacherRole() {
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));

        UserResponse response = adminService.setTeacherRole(userEmail);

        assertNotNull(response);
        assertEquals(response.getRole(), UserRole.TEACHER);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void setStudentRole() {
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));

        UserResponse response = adminService.setStudentRole(userEmail);

        assertNotNull(response);
        assertEquals(response.getRole(), UserRole.STUDENT);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void getAllUsers() {
        when(userRepository.findAll()).thenReturn(allUsersList);

        List<UserResponse> responseList = adminService.getAllUsers();

        assertNotNull(responseList);
        assertEquals(responseList.size(), 2);
    }

    @Test
    void createStudent() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");
        when(jwtService.generateToken(any(User.class))).thenReturn("mockedToken");

        AuthenticationResponse authenticationResponse = adminService.createStudent(registerRequest);

        assertNotNull(authenticationResponse);
        assertEquals("mockedToken", authenticationResponse.getToken());
        verify(userRepository, times(1)).save(argThat(user ->
                user.getFirstName().equals("John") &&
                        user.getLastName().equals("Doe") &&
                        user.getEmail().equals("john.doe@example.com") &&
                        user.getPassword().equals("hashedPassword") &&
                        user.getRole().equals(UserRole.STUDENT)
        ));
        verify(userRepository, times(1)).findByEmail("john.doe@example.com");
    }

    @Test
    void createTeacher() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");
        when(jwtService.generateToken(any(User.class))).thenReturn("mockedToken");

        AuthenticationResponse authenticationResponse = adminService.createTeacher(registerRequest);

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
        when(classRepository.findByName(anyString())).thenReturn(Optional.empty());

        String savedClass = adminService.createClasses(className);

        verify(classRepository, times(1)).save(Class.builder().name(className).build());
        Assertions.assertThat(savedClass).isEqualTo("Class " + className +" successfully created");
    }

    @Test
    void setStudentToClass() {
        when(classRepository.findByName(className)).thenReturn(Optional.of(classes));
        when(userRepository.findByEmail(studentEmail)).thenReturn(Optional.of(student));
        when(marksRepository.findAllByClasses(classes)).thenReturn(Optional.of(marks));

        ClassResponse response = adminService.setStudentToClass(studentEmail, className);

        assertNotNull(response);
        verify(marksRepository, times(1)).save(Marks.builder().classes(classes).subject(subject).teacher(teacher).student(student).build());
        verify(classRepository, times(1)).save(classes);
    }

    @Test
    void createSubject() {
        when(subjectRepository.findByName(anyString())).thenReturn(Optional.empty());

        String savedSubject = adminService.createSubject(subjectName);

        verify(subjectRepository, times(1)).save(Subject.builder().name(subjectName).build());
        Assertions.assertThat(savedSubject).isEqualTo("Subject " + subjectName + " successfully created");
    }

    @Test
    void setTeacherToSubject() {
        when(subjectRepository.findByName(subjectName)).thenReturn(Optional.of(subject));
        when(userRepository.findByEmail(teacherEmail)).thenReturn(Optional.of(teacher));

        SubjectResponse response = adminService.setTeacherToSubject(teacherEmail, subjectName);

        assertNotNull(response);
        verify(subjectRepository, times(1)).save(subject);
    }

    @Test
    void createMarks() {
        when(classRepository.findByName(marksTableRequest.getClassName())).thenReturn(Optional.of(classes));
        when(subjectRepository.findByName(marksTableRequest.getSubjectName())).thenReturn(Optional.of(subject));
        when(userRepository.findByEmail(marksTableRequest.getTeacherEmail())).thenReturn(Optional.of(teacher));
        when(marksRepository.findByClassesAndSubject(classes, subject)).thenReturn(Optional.empty());

        String result = adminService.createMarksTable(marksTableRequest);

        verify(marksRepository, times(1)).save(Marks.builder().classes(classes).subject(subject).teacher(teacher).student(student).build());
        Assertions.assertThat(result).isEqualTo("Marks table successfully created");
    }

    @Test
    void deleteUser() {
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));

        UserResponse response = adminService.deleteUser(userEmail);

        assertNotNull(response);
        verify(userRepository, times(1)).delete(user);
    }

    @Test
    void deleteClasses() {
        when(classRepository.findByName(className)).thenReturn(Optional.of(classes));
        when(marksRepository.removeAllByClasses(classes)).thenReturn(Optional.of(marks));

        String response = adminService.deleteClasses(className);

        verify(classRepository, times(1)).delete(classes);
        Assertions.assertThat(response).isEqualTo("Successfully deleted class " + className);
    }

    @Test
    void deleteSubject() {
        when(subjectRepository.findByName(subjectName)).thenReturn(Optional.of(subject));
        when(marksRepository.removeAllBySubject(subject)).thenReturn(Optional.of(marks));

        String response = adminService.deleteSubject(subjectName);

        verify(subjectRepository, times(1)).delete(subject);
        Assertions.assertThat(response).isEqualTo("Successfully deleted subject " + subjectName);
    }

    @Test
    void deleteMarksTable() {
        when(classRepository.findByName(className)).thenReturn(Optional.of(classes));
        when(subjectRepository.findByName(subjectName)).thenReturn(Optional.of(subject));
        when(marksRepository.deleteAllByClassesAndSubject(classes, subject)).thenReturn(Optional.of(marks));

        String response = adminService.deleteMarksTable(className, subjectName);

        verify(marksRepository, times(1)).deleteAllByClassesAndSubject(classes, subject);
        Assertions.assertThat(response).isEqualTo("Successfully deleted class "+ className +" marks table, in subject " + subjectName);
    }

    @Test
    void unsetTeacherFromSubject() {
        when(userRepository.findByEmail(teacherEmail)).thenReturn(Optional.of(teacher));
        when(subjectRepository.findByName(subjectName)).thenReturn(Optional.of(subject));
        when(marksRepository.findAllByTeacherAndSubject(teacher, subject)).thenReturn(marksList);

        UserResponse response = adminService.unsetTeacherFromSubject(teacherEmail, subjectName);

        assertNotNull(response);
        verify(subjectRepository, times(1)).save(subject);
        verify(userRepository, times(1)).save(new User());
        marks.setTeacher(new User());
        verify(marksRepository, times(1)).save(marks);
    }

    @Test
    void unsetStudentFromClass() {
        when(userRepository.findByEmail(studentEmail)).thenReturn(Optional.of(student));
        when(classRepository.findByName(className)).thenReturn(Optional.of(classes));
        when(marksRepository.removeAllByStudentAndClasses(student, classes)).thenReturn(Optional.of(marks));

        UserResponse response = adminService.unsetStudentFromClass(studentEmail, className);

        assertNotNull(response);
        verify(classRepository, times(1)).save(classes);
    }

    @Test
    void changeTeacherInMarksTable() {
        when(userRepository.findByEmail(marksTableRequest.getTeacherEmail())).thenReturn(Optional.of(teacher));
        when(classRepository.findByName(marksTableRequest.getClassName())).thenReturn(Optional.of(classes));
        when(subjectRepository.findByName(marksTableRequest.getSubjectName())).thenReturn(Optional.of(subject));
        when(marksRepository.findByClassesAndSubject(classes, subject)).thenReturn(Optional.of(marks));

        UserResponse response = adminService.changeTeacherInMarksTable(marksTableRequest);

        assertNotNull(response);
        verify(marksRepository, times(1)).save(marks);
    }

    @Test
    void getAllClass() {
        List<Class> classList = new ArrayList<>();
        classList.add(classes);
        when(classRepository.findAll()).thenReturn(classList);

        List<ClassResponse> response = adminService.getAllClass();

        assertNotNull(response);
        assertEquals(response.size(), 1);
    }

    @Test
    void getAllSubject() {
        List<Subject> subjectList = new ArrayList<>();
        subjectList.add(subject);
        when(subjectRepository.findAll()).thenReturn(subjectList);

        List<SubjectResponse> response = adminService.getAllSubject();

        assertNotNull(response);
        assertEquals(response.size(), 1);
    }

    @Test
    void getAllMarks() {
        when(marksRepository.findAll()).thenReturn(marksList);

        List<ClassMarksResponse> response = adminService.getAllMarks();

        assertNotNull(response);
        assertEquals(response.size(), 1);
    }

    @Test
    void setTeacherToMarksTable(){
        when(classRepository.findByName(marksTableRequest.getClassName())).thenReturn(Optional.of(classes));
        when(subjectRepository.findByName(marksTableRequest.getSubjectName())).thenReturn(Optional.of(subject));
        when(userRepository.findByEmail(marksTableRequest.getTeacherEmail())).thenReturn(Optional.of(teacher));
        when(marksRepository.findAllByClassesAndSubject(classes, subject)).thenReturn(marksList);

        ClassMarksResponse response = adminService.setTeacherToMarksTable(marksTableRequest);

        assertNotNull(response);
        verify(marksRepository, times(1)).save(marks);
    }
}