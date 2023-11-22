package com.rocket.classes;


import com.rocket.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import static org.junit.jupiter.api.Assertions.*;

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