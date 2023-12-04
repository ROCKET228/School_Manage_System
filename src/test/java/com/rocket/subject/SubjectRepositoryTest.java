package com.rocket.subject;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class SubjectRepositoryTest {
    @Autowired
    private SubjectRepository subjectRepository;

    private Subject subject;


    private final String subjectName = "Math";

    @BeforeEach
    public void init(){
        subject = Subject.builder().name(subjectName).build();
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

}