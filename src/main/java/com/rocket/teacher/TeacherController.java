package com.rocket.teacher;

import com.rocket.marks.Marks;
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


    @PutMapping("setMarksToStudent")
    public Marks setMarksToStudent(@RequestParam String subjectName, @RequestParam String studentMail, @RequestParam Integer mark, @RequestHeader("Authorization") String authorizationHeader){
        return teacherService.setMarksToStudent(subjectName, studentMail, mark, authorizationHeader);
    }


    @PostMapping("createMarksTable")
    public String createMarksTable(@RequestParam String className, @RequestParam String subjectName, @RequestHeader("Authorization") String authorizationHeader){
        return teacherService.createMarksTable(className, subjectName, authorizationHeader);
    }

    //TODO: make it work
    @DeleteMapping("unsetMarksFromStudent")
    public Marks unsetMarksFromStudent(@RequestParam String subjectName, @RequestParam String studentMail, @RequestParam LocalDateTime date, @RequestParam Integer mark, @RequestHeader("Authorization") String authorizationHeader){
        return teacherService.unsetMarksFromStudent(subjectName, studentMail,date, mark, authorizationHeader);
    }

    //TODO: make controller to change student mark
}
