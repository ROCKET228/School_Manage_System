package com.rocket.marks;

import com.rocket.classes.Class;
import com.rocket.subject.Subject;
import com.rocket.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class MarksTest {
    @Test
    public void testAddMark() {
        Marks marks = new Marks();
        marks.addMark(90);

        assertEquals(1, marks.getMarks().size());
    }

    @Test
    public void testRemoveMark() {
        Marks marks = new Marks();
        marks.addMark(90);
        LocalDate currentDate = LocalDate.now();
        marks.removeMark(90, currentDate);

        assertEquals(0, marks.getMarks().size());
    }

    @Test
    public void testChangeMark() {
        Marks marks = new Marks();
        marks.addMark(90);
        LocalDate currentDate = LocalDate.now();
        marks.changeMark(95, currentDate);

        assertEquals(1, marks.getMarks().size());
        assertEquals(95, marks.getMarks().get(currentDate));
    }
}