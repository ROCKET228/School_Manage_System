package com.rocket.classes;

import com.rocket.user.User;
import com.rocket.user.UserRepository;
import com.rocket.user.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class ClassRepositoryTest {

    @Autowired
    private ClassRepository classRepository;

    @Autowired
    private UserRepository userRepository;

    private Class classes;

    private User student;

    private final String className = "11-B";

    @BeforeEach
    public void init(){
        classes = Class.builder().name(className).build();
        student = new User(1, "Name", "lastName", "user@mail.com", "12345", UserRole.STUDENT);
    }

    @Test
    void findByName() {
        classRepository.save(classes);
        Class savedClass = classRepository.findByName(className).orElseThrow();
        assertEquals(savedClass, classes);
    }

    @Test
    void findByEnrolledStudents() {
        userRepository.save(student);
        Set<User> enrolledStudents = new HashSet<>();
        enrolledStudents.add(student);
        classes.setEnrolledStudents(enrolledStudents);
        classRepository.save(classes);
        Class savedClass = classRepository.findByEnrolledStudents(student).orElseThrow();
        assertEquals(savedClass, classes);
    }
}