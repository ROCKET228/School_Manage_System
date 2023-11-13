package com.rocket.subject;

import com.rocket.user.User;
import com.rocket.user.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubjectResponse {
    private String name;
    private Set<UserResponse> enrolledTeachers = new HashSet<>();
}
