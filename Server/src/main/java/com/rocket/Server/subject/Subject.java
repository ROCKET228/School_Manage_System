package com.rocket.server.subject;

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
public class Subject {
    @Id
    @GeneratedValue
    private Integer id;
    private String name;

    @OneToMany
    @JoinTable(name = "teacher_enrolled",
            joinColumns = @JoinColumn(name = "subject_id"),
            inverseJoinColumns = @JoinColumn(name =  "user_id")
    )
    private Set<User> enrolledTeachers = new HashSet<>();

    public void enrolledTeacher(User user){
        enrolledTeachers.add(user);
    }
}
