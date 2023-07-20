package com.rocket.server.marks;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MarksRequest {
    private String classname;
    private String subjectname;
    private String teacheremail;
}
