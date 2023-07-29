package com.rocket.server.marks;

import com.rocket.server.classes.Class;
import com.rocket.server.subject.Subject;
import com.rocket.server.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.*;

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

    @ManyToOne
    private User student;


    @ElementCollection
    @CollectionTable(name = "marks_list")
    private Map<LocalDateTime, Integer> marks = new HashMap();

    public void addMark(Integer mark){
        marks.put(LocalDateTime.now(), mark);
    }

    public void removeMark(Integer mark, LocalDateTime date){
        marks.remove(date, mark);
    }
}

