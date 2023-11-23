package com.rocket.teacher;

import com.rocket.classes.Class;
import com.rocket.classes.ClassRepository;
import com.rocket.config.JwtService;
import com.rocket.marks.ClassMarksResponse;
import com.rocket.marks.Marks;
import com.rocket.marks.MarksRepository;
import com.rocket.marks.StudentMarksResponse;
import com.rocket.student.StudentService;
import com.rocket.subject.Subject;
import com.rocket.subject.SubjectRepository;
import com.rocket.user.User;
import com.rocket.user.UserRepository;
import com.rocket.user.UserRole;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

class TeacherServiceTest {

    @InjectMocks
    private TeacherService teacherService;

    @Mock
    private MarksRepository marksRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SubjectRepository subjectRepository;

    @Mock
    private ClassRepository classRepository;

    @Mock
    private JwtService jwtService;

    private String subjectName;
    private String authorizationHeader;
    private String studentEmail;
    private String teacherEmail;
    private String className;
    private User student;
    private User teacher;
    private Subject subject;
    private Marks marks;
    private Class classes;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        subjectName = "Math";
        authorizationHeader = "Bearer mockToken";
        studentEmail = "student@example.com";
        teacherEmail = "teacher@example.com";
        className = "11-B";

        student = new User();
        student.setRole(UserRole.STUDENT);

        teacher = new User();
        teacher.setRole(UserRole.TEACHER);

        subject = new Subject();
        subject.enrolledTeacher(teacher);

        classes = new Class();
        classes.enrolledStudent(student);

        marks = new Marks();
        marks.setClasses(classes);
        marks.setTeacher(teacher);
        marks.setStudent(student);
        Map<LocalDate, Integer> map = new HashMap<>();
        map.put(LocalDate.now(), 10);
        marks.setMarks(map);
    }

    @Test
    void setMarksToStudent() {
        when(userRepository.findByEmail(studentEmail)).thenReturn(Optional.of(student));

        when(jwtService.extractUsername("mockToken")).thenReturn(teacherEmail);
        when(userRepository.findByEmail(teacherEmail)).thenReturn(Optional.of(teacher));

        when(subjectRepository.findByName(subjectName)).thenReturn(Optional.of(subject));

        when(marksRepository.findByStudentAndSubject(student, subject)).thenReturn(Optional.of(marks));


        StudentMarksResponse result = teacherService.setMarksToStudent(subjectName, studentEmail, 10, authorizationHeader);


        assertNotNull(result);
        verify(marksRepository, times(1)).save(marks);
    }

    @Test
    void getClassMarksInSubject() {
        when(jwtService.extractUsername("mockToken")).thenReturn(teacherEmail);
        when(userRepository.findByEmail(teacherEmail)).thenReturn(Optional.of(teacher));

        when(classRepository.findByName(className)).thenReturn(Optional.of(classes));

        when(subjectRepository.findByName(subjectName)).thenReturn(Optional.of(subject));

        List<Marks> marksList = new ArrayList<>();
        marksList.add(marks);
        when(marksRepository.findAllByClassesAndSubject(classes, subject)).thenReturn(marksList);


        ClassMarksResponse result = teacherService.getClassMarksInSubject(className, subjectName, authorizationHeader);

        assertNotNull(result);
    }

    @Test
    void createMarksTable() {
        when(jwtService.extractUsername("mockToken")).thenReturn(teacherEmail);
        when(userRepository.findByEmail(teacherEmail)).thenReturn(Optional.of(teacher));

        when(classRepository.findByName(className)).thenReturn(Optional.of(classes));

        when(subjectRepository.findByName(subjectName)).thenReturn(Optional.of(subject));

        when(marksRepository.findByClassesAndSubject(classes, subject)).thenReturn(Optional.empty());


        String result = teacherService.createMarksTable(className, subjectName, authorizationHeader);

        Assertions.assertThat(result).isEqualTo("Marks table successfully created");

    }

    @Test
    void unsetMarksFromStudent() {

        when(userRepository.findByEmail(studentEmail)).thenReturn(Optional.of(student));

        when(jwtService.extractUsername("mockToken")).thenReturn(teacherEmail);
        when(userRepository.findByEmail(teacherEmail)).thenReturn(Optional.of(teacher));

        when(subjectRepository.findByName(subjectName)).thenReturn(Optional.of(subject));

        when(marksRepository.findByStudentAndSubject(student, subject)).thenReturn(Optional.of(marks));

        StudentMarksResponse result = teacherService.unsetMarksFromStudent(subjectName, studentEmail, LocalDate.now(), 10, authorizationHeader);

        assertNotNull(result);
        verify(marksRepository, times(1)).save(marks);
    }


    @Test
    void changeStudentMark() {
        when(userRepository.findByEmail(studentEmail)).thenReturn(Optional.of(student));

        when(jwtService.extractUsername("mockToken")).thenReturn(teacherEmail);
        when(userRepository.findByEmail(teacherEmail)).thenReturn(Optional.of(teacher));

        when(subjectRepository.findByName(subjectName)).thenReturn(Optional.of(subject));

        when(marksRepository.findByStudentAndSubject(student, subject)).thenReturn(Optional.of(marks));

        StudentMarksResponse result = teacherService.changeStudentMark(subjectName, studentEmail, LocalDate.now(), 10, authorizationHeader);

        assertNotNull(result);
        verify(marksRepository, times(1)).save(marks);
    }
}