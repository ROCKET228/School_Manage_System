package com.rocket.student;

import com.rocket.marks.Marks;
import com.rocket.marks.StudentMarksResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/SchoolManageSystem/student")
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;


    @GetMapping("getStudentMarks/{subjectName}")
    @ResponseStatus(HttpStatus.OK)
    public StudentMarksResponse getStudentMarksInSubject(@PathVariable String subjectName, @RequestHeader("Authorization") String authorizationHeader){
        return studentService.getStudentMarksInSubject(subjectName, authorizationHeader);
    }
}
