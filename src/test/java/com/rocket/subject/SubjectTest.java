package com.rocket.subject;

import com.rocket.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class SubjectTest {
    @Autowired
    private TestEntityManager entityManager;

    private Subject subjectEntity;

    private User user;

    @BeforeEach
    public void init(){
        user = new User();
        user.setFirstName("testUser");
        user.setPassword("password");

        entityManager.persist(user);

        subjectEntity = new Subject();
        subjectEntity.setName("Math");
    }

    @Test
    void enrolledTeacher() {
        subjectEntity.enrolledTeacher(user);
        entityManager.persistAndFlush(subjectEntity);

        Subject retrievedSubject = entityManager.find(Subject.class, subjectEntity.getId());

        assertTrue(retrievedSubject.getEnrolledTeachers().contains(user));
    }

    @Test
    void unrolledTeacher() {
        subjectEntity.enrolledTeacher(user);
        entityManager.persistAndFlush(subjectEntity);

        Subject retrievedSubject = entityManager.find(Subject.class, subjectEntity.getId());
        retrievedSubject.unrolledTeacher(user);
        entityManager.persistAndFlush(retrievedSubject);

        Subject updatedSubject = entityManager.find(Subject.class, subjectEntity.getId());

        assertFalse(updatedSubject.getEnrolledTeachers().contains(user));
    }
}