package com.rocket.server.admin;

import com.rocket.server.user.Role;
import com.rocket.server.user.User;
import com.rocket.server.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserRepository userRepository;

    public void setTeacherRole(String userEmail){
        var user = userRepository.findByEmail(userEmail).orElseThrow();
        user.setRole(Role.TEACHER);
        userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
