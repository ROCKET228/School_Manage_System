package com.rocket.marks;

import com.rocket.classes.Class;
import com.rocket.subject.Subject;
import com.rocket.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
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
    private Map<LocalDate, Integer> marks = new HashMap();

    public void addMark(Integer mark){
        marks.put(LocalDate.now(), mark);
    }

    public void removeMark(Integer mark, LocalDate date){
        marks.remove(date, mark);
    }

    public void changeMark( Integer mark, LocalDate date){
        marks.replace(date, mark);
    }
}

