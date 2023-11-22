package com.rocket.classes;

import com.rocket.marks.Marks;
import com.rocket.subject.Subject;
import com.rocket.user.User;
import com.rocket.user.UserRepository;
import com.rocket.user.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@DataJpaTest
class ClassTest {

    @Autowired
    private TestEntityManager entityManager;

    private Class classEntity;

    private User user;

    @BeforeEach
    public void init(){
        user = new User();
        user.setFirstName("testUser");
        user.setPassword("password");

        entityManager.persist(user);

        classEntity = new Class();
        classEntity.setName("11-B");
    }

    @Test
    void enrolledStudent() {

        classEntity.enrolledStudent(user);
        entityManager.persistAndFlush(classEntity);

        Class retrievedClass = entityManager.find(Class.class, classEntity.getId());

        assertTrue(retrievedClass.getEnrolledStudents().contains(user));
    }

    @Test
    void unrolledStudent() {
        classEntity.enrolledStudent(user);
        entityManager.persistAndFlush(classEntity);

        Class retrievedClass = entityManager.find(Class.class, classEntity.getId());
        retrievedClass.unrolledStudent(user);
        entityManager.persistAndFlush(retrievedClass);

        Class updatedClass = entityManager.find(Class.class, classEntity.getId());

        assertFalse(updatedClass.getEnrolledStudents().contains(user));
    }
}