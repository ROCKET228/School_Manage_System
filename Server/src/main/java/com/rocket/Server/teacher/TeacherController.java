package com.rocket.server.teacher;

import com.rocket.server.marks.Marks;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequestMapping("/SchoolManageSystem/teacher")
@RequiredArgsConstructor
public class TeacherController {
    private final TeacherService teacherService;


    @GetMapping("getClassMarks/{subjectName}/{className}")
    public List<Marks> getClassMarksInSubject(@PathVariable String className, @PathVariable String subjectName, @RequestHeader("Authorization") String authorizationHeader){
        return teacherService.getClassMarksInSubject(className, subjectName, authorizationHeader);
    }


    @PutMapping("setMarksToStudent/{subjectName}/{studentMail}/{mark}")
    public Marks setMarksToStudent(@PathVariable String subjectName, @PathVariable String studentMail, @PathVariable Integer mark, @RequestHeader("Authorization") String authorizationHeader){
        return teacherService.setMarksToStudent(subjectName, studentMail, mark, authorizationHeader);
    }


    @PostMapping("createMarksTable/{subjectName}/{className}")
    public String createMarksTable(@PathVariable String className, @PathVariable String subjectName, @RequestHeader("Authorization") String authorizationHeader){
        return teacherService.createMarksTable(className, subjectName, authorizationHeader);
    }

    //TODO: check in postman
    @DeleteMapping("unsetMarksFromStudent/{subjectName}/{studentMail}/{date}/{mark}")
    public Marks unsetMarksFromStudent(@PathVariable String subjectName, @PathVariable String studentMail, @PathVariable LocalDateTime date, @PathVariable Integer mark, @RequestHeader("Authorization") String authorizationHeader){
        return teacherService.unsetMarksFromStudent(subjectName, studentMail,date, mark, authorizationHeader);
    }
}
