package com.rocket.server.admin;

import com.rocket.server.auth.AuthenticationResponse;
import com.rocket.server.auth.RegisterRequest;
import com.rocket.server.classes.Class;
import com.rocket.server.marks.MarksRequest;
import com.rocket.server.subject.Subject;
import com.rocket.server.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/SchoolManageSystem/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @GetMapping("/getAllUsers")
    public List<User> getAllUsers(){
        return adminService.getAllUsers();
    }

    @PutMapping("/setTeacher")
    public User setTeacherRole(@RequestParam String userEmail){
        return adminService.setTeacherRole(userEmail);
    }

    @PutMapping("/setStudent")
    public User setStudentRole(@RequestParam String userEmail){
        return adminService.setStudentRole(userEmail);
    }

    @PutMapping("/setStudentToClass")
    public Class setStudentToClass(@RequestParam String userEmail, @RequestParam String className){
        return adminService.setStudentToClass(userEmail, className);
    }

    @PutMapping("/setTeacherToSubject")
    public Subject setTeacherToSubject(@RequestParam String userEmail, @RequestParam String subjectName){
        return adminService.setTeacherToSubject(userEmail, subjectName);
    }

    //TODO: check why i can create it without access token
    @PostMapping("/createStudent")
    public ResponseEntity<AuthenticationResponse> createStudent(@RequestBody RegisterRequest request){
        return ResponseEntity.ok(adminService.createStudent(request));
    }
    //TODO: check why i can create it without access token
    @PostMapping("/createTeacher")
    public ResponseEntity<AuthenticationResponse> createTeacher(@RequestBody RegisterRequest request){
        return ResponseEntity.ok(adminService.createTeacher(request));
    }
    //TODO: check why i can create it without access token
    //TODO: check in postman
    @PostMapping("/createClasses/{className}")
    public Class createClasses(@PathVariable String className){
        return adminService.createClasses(className);
    }


    //TODO: check why i can create it without access token
    //TODO: check in postman
    @PostMapping("/createSubject/{subjectName}")
    public Subject createSubject(@PathVariable String subjectName){
        return adminService.createSubject(subjectName);
    }

    //TODO: check why i can create it without access token
    @PostMapping("/createMarks")
    public String createMarks(@RequestBody MarksRequest request){
        return adminService.createMarks(request);
    }

    //TODO: make delete and unset for all controller

    //TODO: check in postman
    @DeleteMapping("/deleteUser/{userEmail}")
    public User deleteUser(@PathVariable String userEmail){
        return adminService.deleteUser(userEmail);
    }

    //TODO: check in postman
    @DeleteMapping("/deleteClasses/{className}")
    public Class deleteClasses(@PathVariable String className){
        return adminService.deleteClasses(className);
    }


    //TODO: check in postman
    @DeleteMapping("/deleteSubject/{subjectName}")
    public Subject deleteSubject(@PathVariable String subjectName){
        return adminService.deleteSubject(subjectName);
    }

    //TODO: check in postman
    @DeleteMapping("/deleteMarksTable/{className}/{subjectName}")
    public String deleteMarks(@PathVariable String className, @PathVariable String subjectName){
        return adminService.deleteMarks(className, subjectName);
    }

    //TODO: check in postman
    @PutMapping("/unsetStudentFromClass")
    public User unsetStudentFromClass(@RequestParam String userEmail, @RequestParam String className){
        return adminService.unsetStudentFromClass(userEmail, className);
    }

    //TODO: check in postman
    @PutMapping("/unsetTeacherFromSubject")
    public User unsetTeacherFromSubject(@RequestParam String userEmail, @RequestParam String subjectName){
        return adminService.unsetTeacherFromSubject(userEmail, subjectName);
    }

    //TODO: unset teacher  from class in marks
    //TODO: set teacher to marks



    //TODO: CREATE TEACHER AND STUDENT VISION

}
