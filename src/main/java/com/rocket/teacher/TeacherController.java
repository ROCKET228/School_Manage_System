package com.rocket.teacher;

import com.rocket.marks.ClassMarksResponse;
import com.rocket.marks.Marks;
import com.rocket.marks.MarksRequest;
import com.rocket.marks.StudentMarksResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/SchoolManageSystem/teacher")
@RequiredArgsConstructor
public class TeacherController {
    private final TeacherService teacherService;

    @GetMapping("getClassMarks/{subjectName}/{className}")
    @ResponseStatus(HttpStatus.OK)
    public ClassMarksResponse getClassMarksInSubject(@PathVariable String className, @PathVariable String subjectName, @RequestHeader("Authorization") String authorizationHeader){
        return teacherService.getClassMarksInSubject(className, subjectName, authorizationHeader);
    }

    @PutMapping("setMarksToStudent")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public StudentMarksResponse setMarksToStudent(@RequestParam String subjectName, @RequestParam String studentMail, @RequestParam Integer mark, @RequestHeader("Authorization") String authorizationHeader){
        return teacherService.setMarksToStudent(subjectName, studentMail, mark, authorizationHeader);
    }

    @PostMapping("createMarksTable")
    @ResponseStatus(HttpStatus.CREATED)
    public String createMarksTable(@RequestParam String className, @RequestParam String subjectName, @RequestHeader("Authorization") String authorizationHeader){
        return teacherService.createMarksTable(className, subjectName, authorizationHeader);
    }

    @DeleteMapping("unsetMarksFromStudent")
    @ResponseStatus(HttpStatus.OK)
    public StudentMarksResponse unsetMarksFromStudent(@RequestBody MarksRequest marksRequest, @RequestHeader("Authorization") String authorizationHeader){
        return teacherService.unsetMarksFromStudent(marksRequest.getSubjectName(), marksRequest.getStudentMail(), marksRequest.getDate(), marksRequest.getMark(), authorizationHeader);
    }

    @PutMapping("changeStudentMark")
    @ResponseStatus(HttpStatus.OK)
    public StudentMarksResponse changeStudentMark(@RequestBody MarksRequest marksRequest, @RequestHeader("Authorization") String authorizationHeader){
        return teacherService.changeStudentMark(marksRequest.getSubjectName(), marksRequest.getStudentMail(), marksRequest.getDate(), marksRequest.getMark(), authorizationHeader);
    }
}
