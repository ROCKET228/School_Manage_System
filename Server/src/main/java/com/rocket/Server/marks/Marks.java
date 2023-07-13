package com.rocket.server.marks;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Marks {
    //from other tables
    private Integer classesId;
    private Integer subjectId;
    private Integer teacherId;
    private Integer studentId;
    //
    private List<Integer> marksList;

}
