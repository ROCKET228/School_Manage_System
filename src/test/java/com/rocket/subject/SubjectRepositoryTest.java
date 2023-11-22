package com.rocket.subject;


import com.rocket.user.User;
import com.rocket.user.UserRepository;
import com.rocket.user.UserRole;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class SubjectRepositoryTest {
    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private UserRepository userRepository;

    private Subject subject;

    private User teacher;

    private final String subjectName = "Math";

    @BeforeEach
    public void init(){
        subject = Subject.builder().name(subjectName).build();
        teacher = new User(1, "Name", "lastName", "user@mail.com", "12345", UserRole.TEACHER);
    }

    @Test
    void findByName() {
        subjectRepository.save(subject);
        Subject savedSubject = subjectRepository.findByName(subjectName).orElseThrow();
        assertEquals(savedSubject, subject);
    }

    @Test
    void deleteAllByName() {
        subjectRepository.save(subject);
        subjectRepository.deleteAllByName(subjectName).orElseThrow();
        Optional<Subject> returnSubject = subjectRepository.findByName(subjectName);

        Assertions.assertThat(returnSubject).isEmpty();
    }

    @Test
    void findAllByEnrolledTeachers() {
        userRepository.save(teacher);
        Set<User> enrolledTeacher = new HashSet<>();
        enrolledTeacher.add(teacher);
        subject.setEnrolledTeachers(enrolledTeacher);
        subjectRepository.save(subject);
        Subject savedSubject = subjectRepository.findByName(subjectName).orElseThrow();
        assertEquals(savedSubject, subject);
    }
}