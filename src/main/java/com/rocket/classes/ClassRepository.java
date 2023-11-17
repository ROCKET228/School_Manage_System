package com.rocket.classes;

import com.rocket.marks.Marks;
import com.rocket.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClassRepository extends JpaRepository<Class, Integer> {
    Optional<Class> findByName(String name);
    Optional<Class> deleteByName(String name);
    Optional<Class> findByEnrolledStudents(User student);
}
