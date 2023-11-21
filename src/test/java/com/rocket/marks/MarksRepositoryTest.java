package com.rocket.marks;

import com.rocket.classes.Class;
import com.rocket.classes.ClassRepository;
import com.rocket.subject.Subject;
import com.rocket.subject.SubjectRepository;
import com.rocket.user.User;
import com.rocket.user.UserRepository;
import com.rocket.user.UserRole;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class MarksRepositoryTest {
    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ClassRepository classRepository;

    @Autowired
    private MarksRepository marksRepository;

    private Subject subject;

    private User teacher;

    private String subjectName = "Math";

    private Class classes;

    private User student;

    private String className = "11-B";

    private Marks marks;


    @BeforeEach
    public void init(){
        subject = Subject.builder().name(subjectName).build();
        subjectRepository.save(subject);
        teacher = User.builder().firstName("TeacherName").lastName("lastName").email("teacher@mail.com").password("12345").role(UserRole.TEACHER).build();
        userRepository.save(teacher);
        classes = Class.builder().name(className).build();
        classRepository.save(classes);
        student = User.builder().firstName("StudentName").lastName("lastName").email("student@mail.com").password("12345").role(UserRole.STUDENT).build();
        userRepository.save(student);
        marks = Marks.builder().classes(classes).subject(subject).teacher(teacher).student(student).build();
    }

    @Test
    void removeAllByStudentAndClasses() {
        marksRepository.save(marks);
        marksRepository.removeAllByStudentAndClasses(student, classes);

        Optional<Marks> returnMarks = marksRepository.findByStudentAndSubject(student, subject);

        Assertions.assertThat(returnMarks).isEmpty();
    }

    @Test
    void findAllByTeacherAndSubject() {
        marksRepository.save(marks);
        User student1 = User.builder().firstName("StudentName2").lastName("lastName").email("student2@mail.com").password("12345").role(UserRole.STUDENT).build();
        userRepository.save(student1);
        Marks marks1 = Marks.builder().classes(classes).subject(subject).teacher(teacher).student(student1).build();
        marksRepository.save(marks1);
        List<Marks> expectedMarks = new ArrayList<>();
        expectedMarks.add(marks);
        expectedMarks.add(marks1);


        List<Marks> actualMarks = marksRepository.findAllByTeacherAndSubject(teacher, subject);

        assertEquals(expectedMarks.size(), actualMarks.size());

    }

    @Test
    void findAllByClasses() {
        marksRepository.save(marks);

        Marks returnMarks = marksRepository.findAllByClasses(classes).orElseThrow();

        Assertions.assertThat(returnMarks).isEqualTo(marks);
    }

    @Test
    void removeAllByClasses() {
        marksRepository.save(marks);
        marksRepository.removeAllByClasses(classes);

        Optional<Marks> returnMarks = marksRepository.findByStudentAndSubject(student, subject);

        Assertions.assertThat(returnMarks).isEmpty();
    }

    @Test
    void removeAllBySubject() {
        marksRepository.save(marks);
        marksRepository.removeAllBySubject(subject);

        Optional<Marks> returnMarks = marksRepository.findByStudentAndSubject(student, subject);

        Assertions.assertThat(returnMarks).isEmpty();
    }

    @Test
    void deleteAllByClassesAndSubject() {
        marksRepository.save(marks);
        marksRepository.deleteAllByClassesAndSubject(classes, subject);

        Optional<Marks> returnMarks = marksRepository.findByStudentAndSubject(student, subject);

        Assertions.assertThat(returnMarks).isEmpty();
    }

    @Test
    void findByClassesAndSubject() {
        marksRepository.save(marks);

        Marks returnMarks = marksRepository.findByClassesAndSubject(classes, subject).orElseThrow();

        Assertions.assertThat(returnMarks).isEqualTo(marks);
    }

    @Test
    void findByStudentAndSubject() {
        marksRepository.save(marks);

        Marks returnMarks = marksRepository.findByStudentAndSubject(student, subject).orElseThrow();

        Assertions.assertThat(returnMarks).isEqualTo(marks);
    }

    @Test
    void findAllByClassesAndSubject() {
        marksRepository.save(marks);
        User student1 = User.builder().firstName("StudentName2").lastName("lastName").email("student2@mail.com").password("12345").role(UserRole.STUDENT).build();
        userRepository.save(student1);
        Marks marks1 = Marks.builder().classes(classes).subject(subject).teacher(teacher).student(student1).build();
        marksRepository.save(marks1);
        List<Marks> expectedMarks = new ArrayList<>();
        expectedMarks.add(marks);
        expectedMarks.add(marks1);

        List<Marks> actualMarks = marksRepository.findAllByClassesAndSubject(classes, subject);

        assertEquals(expectedMarks.size(), actualMarks.size());
    }
}