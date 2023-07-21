package com.rocket.server.marks;

import com.rocket.server.classes.Class;
import com.rocket.server.subject.Subject;
import com.rocket.server.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MarksRepository extends JpaRepository<Marks, Integer> {
    Optional<Marks> deleteAllByClassesAndSubject(Class classEntity, Subject subject);
    Optional<Marks> findByTeacher(User teacher);
    Optional<Marks> removeByTeacher(User teacher);
    Optional<Marks> findByClassesAndSubject(Class classEntity, Subject subject);
}
