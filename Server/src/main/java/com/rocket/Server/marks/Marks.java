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
    @GeneratedValue
    private Integer id;

    @ManyToOne
    private Class classes;


    @ManyToOne
    private Subject subject;


    @ManyToOne
    private User teacher;

    @OneToOne
    private User student;

    @ElementCollection
    @Column(name = "mark")
    private List<Integer> list = new ArrayList<>();
}

