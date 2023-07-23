package com.rocket.server.classes;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClassRepository extends JpaRepository<Class, Integer> {
    Optional<Class> findByName(String name);
    Optional<Class> deleteByName(String name);
}
