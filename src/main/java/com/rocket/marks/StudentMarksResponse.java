package com.rocket.marks;

import com.rocket.user.User;
import com.rocket.user.UserResponse;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentMarksResponse {
    private String classesName;
    private String subjectName;
    private UserResponse teacher;
    private UserResponse student;
    private Map<LocalDate, Integer> marks = new HashMap();
}
