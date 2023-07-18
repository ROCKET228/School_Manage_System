package com.rocket.server.marks;

import com.rocket.server.classes.Class;
import com.rocket.server.subject.Subject;
import com.rocket.server.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Marks {
    @Id
    private Integer id;

    //TODO: create normal join
    @ManyToOne
    private Class classes;

    //TODO: create normal join
    @ManyToOne
    private Subject subject;

    //TODO: create normal join
    @ManyToOne
    private User teacher;

    //TODO: create normal join
    @ManyToOne
    private User student;

    @ElementCollection
    private List<Integer> list = new ArrayList<>();
}

