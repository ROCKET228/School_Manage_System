package com.rocket.student;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.rocket.classes.Class;
import com.rocket.config.JwtService;
import com.rocket.marks.Marks;
import com.rocket.marks.MarksRepository;
import com.rocket.marks.StudentMarksResponse;
import com.rocket.subject.Subject;
import com.rocket.subject.SubjectRepository;
import com.rocket.user.User;
import com.rocket.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

public class StudentServiceTest {

    @InjectMocks
    private StudentService studentService;

    @Mock
    private MarksRepository marksRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SubjectRepository subjectRepository;

    @Mock
    private JwtService jwtService;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetStudentMarksInSubject() {
        String subjectName = "Math";
        String authorizationHeader = "Bearer mockToken";
        String studentEmail = "student@example.com";

        User student = new User(); // Create a mock user
        when(jwtService.extractUsername("mockToken")).thenReturn(studentEmail);
        when(userRepository.findByEmail(studentEmail)).thenReturn(Optional.of(student));

        Subject subject = new Subject(); // Create a mock subject
        when(subjectRepository.findByName(subjectName)).thenReturn(Optional.of(subject));

        Marks marks = new Marks(); // Create a mock marks
        Class classes = new Class();
        User teacher = new User();
        marks.setClasses(classes);
        marks.setTeacher(teacher);
        marks.setStudent(student);
        when(marksRepository.findByStudentAndSubject(student, subject)).thenReturn(Optional.of(marks));


        StudentMarksResponse result = studentService.getStudentMarksInSubject(subjectName, authorizationHeader);


        assertNotNull(result);
        verify(marksRepository, times(1)).save(marks);
    }
}
