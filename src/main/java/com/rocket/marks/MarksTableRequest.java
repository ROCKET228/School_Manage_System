package com.rocket.marks;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MarksTableRequest {
    private String className;
    private String subjectName;
    private String teacherEmail;
}
