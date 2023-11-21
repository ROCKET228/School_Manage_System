package com.rocket.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByEmail() {
        User user = new User(1, "Name", "lastName", "user@mail.com", "12345", UserRole.USER);
        User savedUser = userRepository.save(user);
        User returnUser = userRepository.findByEmail("user@mail.com").orElseThrow(() -> new IllegalArgumentException("User with this email is not exist"));
        assertEquals(savedUser, returnUser);
    }

}