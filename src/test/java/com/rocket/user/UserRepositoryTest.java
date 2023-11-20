package com.rocket.user;


import com.rocket.admin.AdminService;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Before
    public void setup(){

    }
    @Test
    void findByEmail() {
        User user = new User(1, "Name", "lastName", "user@mail.com", "12345", UserRole.USER);
        User savedUser = userRepository.save(user);
        User returnUser = userRepository.findByEmail("user@mail.com").orElseThrow(() -> new IllegalArgumentException("User with this email is not exist"));
        assertEquals(savedUser, returnUser);
    }

}