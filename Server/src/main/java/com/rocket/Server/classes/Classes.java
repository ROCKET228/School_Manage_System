package com.rocket.server.classes;

import com.rocket.server.user.User;
import jakarta.persistence.*;
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
public class Classes {
    @Id
    @GeneratedValue
    private Integer id;
    private String name;
    //Make normal join
    @ManyToMany
    @JoinTable(
            name = "-user",
            joinColumns = @JoinColumn(name = "role"),
            inverseJoinColumns = @JoinColumn(name = "id")
    )
    private List<User> students;


}
