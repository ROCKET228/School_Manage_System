package com.rocket.server.teacher;

import com.rocket.server.subject.Subject;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
public class Teacher {
    //from other tables
    @Id
    private Integer id;
    private List<Subject> subjectsIdList;
}
