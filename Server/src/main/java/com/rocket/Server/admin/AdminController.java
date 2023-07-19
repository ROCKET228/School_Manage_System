package com.rocket.server.admin;

import com.rocket.server.auth.AuthenticationResponse;
import com.rocket.server.auth.RegisterRequest;
import com.rocket.server.classes.Class;
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


    @PutMapping("/setTeacher")
    public User setTeacherRole(@RequestParam String userEmail){
        return adminService.setTeacherRole(userEmail);
    }


    @PutMapping("/setStudent")
    public User setStudentRole(@RequestParam String userEmail){
        return adminService.setStudentRole(userEmail);
    }

    @GetMapping("/getAllUsers")
    public List<User> getAllUsers(){
        return adminService.getAllUsers();
    }


    @PostMapping("/createStudent")
    public ResponseEntity<AuthenticationResponse> createUser(@RequestBody RegisterRequest request){
        return ResponseEntity.ok(adminService.createStudent(request));
    }

    @PostMapping("/createTeacher")
    public ResponseEntity<AuthenticationResponse> createTeacher(@RequestBody RegisterRequest request){
        return ResponseEntity.ok(adminService.createTeacher(request));
    }

    @PostMapping("/createClasses")
    public Class createClasses(@RequestParam String className){
        return adminService.createClasses(className);
    }

    @PutMapping("/setStudentToClass")
    public Class setStudentToClass(@RequestParam String userEmail, @RequestParam String className){
        return adminService.setStudentToClass(userEmail, className);
    }


    @PostMapping("/createSubject")
    public Subject createSubject(@RequestParam String subjectName){
        return adminService.createSubject(subjectName);
    }


    @PutMapping("/setTeacherToSubject")
    public Subject setTeacherToSubject(@RequestParam String userEmail, @RequestParam String subjectName){
        return adminService.setTeacherToSubject(userEmail, subjectName);
    }

    //TODO: make controller to create marks
    //TODO: make controller to set marks
    //TODO: make controller to set class in marks
    //TODO: make controller to set subject in marks
    //TODO: make controller to set teacher in marks
    //TODO: make controller to set student in marks
}
