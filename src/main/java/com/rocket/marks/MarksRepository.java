package com.rocket.marks;

import com.rocket.classes.Class;
import com.rocket.subject.Subject;
import com.rocket.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MarksRepository extends JpaRepository<Marks, Integer> {
    Optional<Marks> deleteByClassesAndSubject(Class classEntity, Subject subject);
    Optional<Marks> findByTeacher(User teacher);
    Optional<Marks> findAllByClasses(Class classEntity);
    Optional<Marks> removeByTeacher(User teacher);
    Optional<Marks> removeByMarks(Marks marks);
    Optional<Marks> findByClassesAndSubject(Class classEntity, Subject subject);
    Optional<Marks> findByStudentAndSubject(User student, Subject subject);
    List<Marks> findAllByClassesAndSubject(Class classEntity, Subject subject);
}
