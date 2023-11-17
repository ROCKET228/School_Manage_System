package com.rocket.marks;

import com.rocket.classes.Class;
import com.rocket.subject.Subject;
import com.rocket.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface MarksRepository extends JpaRepository<Marks, Integer> {
    @Transactional
    Optional<Marks> removeAllByStudentAndClasses(User student, Class classEntity);
    @Transactional
    List<Marks> findAllByTeacherAndSubject(User teacher, Subject subject);
    Optional<Marks> findAllByClasses(Class classEntity);
    @Transactional
    Optional<Marks> removeAllByClasses(Class classEntity);
    @Transactional
    Optional<Marks> removeAllBySubject(Subject subject);
    @Transactional
    Optional<Marks> deleteAllByClassesAndSubject(Class classEntity, Subject subject);
    Optional<Marks> findByClassesAndSubject(Class classEntity, Subject subject);
    Optional<Marks> findByStudentAndSubject(User student, Subject subject);
    List<Marks> findAllByClassesAndSubject(Class classEntity, Subject subject);
}
