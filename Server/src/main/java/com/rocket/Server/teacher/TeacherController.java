package com.rocket.server.teacher;

import com.rocket.server.marks.Marks;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/SchoolManageSystem/teacher")
@RequiredArgsConstructor
public class TeacherController {
    private final TeacherService teacherService;

    @GetMapping("getClassMarks/{subjectName}/{className}")
    public Marks getClassMarksInSubject(@PathVariable String className, @PathVariable String subjectName){
        return teacherService.getClassMarksInSubject(className, subjectName);
    }


    @PutMapping("setMarksToStudent/{subjectName}/{studentMail}/{mark}")
    public Marks setMarksToStudent(@PathVariable String subjectName, @PathVariable String studentMail, @PathVariable Integer mark){
        return teacherService.setMarksToStudent(subjectName, studentMail, mark);
    }

    @PostMapping("createMarksTable/{subjectName}/{className}")
    public String createMarksTable(@PathVariable String className, @PathVariable String subjectName, @RequestHeader("Authorization") String authorizationHeader){
        return teacherService.createMarksTable(className, subjectName, authorizationHeader);
    }

    //TODO: Delete mark controller
    //TODO: add date to marks
    //@DeleteMapping("unsetMarksFromStudent")
}
