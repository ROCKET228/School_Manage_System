package com.rocket.marks;


import com.rocket.user.UserResponse;
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
public class ClassMarksResponse {
    private String classesName;
    private String subjectName;
    private UserResponse teacher;
    private Map<UserResponse, Map<LocalDate, Integer>> marks = new HashMap();
}
