package com.rocket.server.classes;

import com.rocket.server.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Class {
    @Id
    @GeneratedValue
    private Integer id;
    private String name;

    @OneToMany
    @JoinTable(name = "student_enrolled",
            joinColumns = @JoinColumn(name = "class_id"),
            inverseJoinColumns = @JoinColumn(name =  "user_id")
    )
    private Set<User> enrolledStudents = new HashSet<>();

    public void enrolledStudent(User user){
        enrolledStudents.add(user);
    }

    public void unrolledStudent(User user){
        enrolledStudents.remove(user);
    }
}

