package com.rocket.server.admin;

import com.rocket.server.auth.AuthenticationResponse;
import com.rocket.server.auth.RegisterRequest;
import com.rocket.server.classes.Class;
import com.rocket.server.marks.Marks;
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

    @GetMapping("/getAllClass")
    public List<Class> getAllClass(){ return adminService.getAllClass(); }

    @GetMapping("/getAllSubject")
    public List<Subject> getAllSubject(){ return adminService.getAllSubject(); }

    @GetMapping("/getAllMarks")
    public List<Marks> getAllMarks(){ return adminService.getAllMarks(); }

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

    @PutMapping("/unsetStudentFromClass")
    public User unsetStudentFromClass(@RequestParam String userEmail, @RequestParam String className){
        return adminService.unsetStudentFromClass(userEmail, className);
    }

    @PutMapping("/unsetTeacherFromSubject")
    public User unsetTeacherFromSubject(@RequestParam String userEmail, @RequestParam String subjectName){
        return adminService.unsetTeacherFromSubject(userEmail, subjectName);
    }

    @PutMapping("/setTeacherFromMarksTable")
    public User setTeacherFromMarksTable(@RequestBody MarksRequest request){
        return adminService.setTeacherFromMarksTable(request);
    }

    @PostMapping("/createStudent")
    public ResponseEntity<AuthenticationResponse> createStudent(@RequestBody RegisterRequest request){
        return ResponseEntity.ok(adminService.createStudent(request));
    }

    @PostMapping("/createTeacher")
    public ResponseEntity<AuthenticationResponse> createTeacher(@RequestBody RegisterRequest request){
        return ResponseEntity.ok(adminService.createTeacher(request));
    }

    @PostMapping("/createClasses/{className}")
    public Class createClasses(@PathVariable String className){
        return adminService.createClasses(className);
    }

    @PostMapping("/createSubject/{subjectName}")
    public Subject createSubject(@PathVariable String subjectName){
        return adminService.createSubject(subjectName);
    }

    @PostMapping("/createMarks")
    public String createMarks(@RequestBody MarksRequest request){
        return adminService.createMarks(request);
    }

    //TODO: check in postman
    @DeleteMapping("/deleteUser/{userEmail}")
    public User deleteUser(@PathVariable String userEmail){
        return adminService.deleteUser(userEmail);
    }

    //TODO: check in postman
    @DeleteMapping("/deleteClasses/{className}")
    public String deleteClasses(@PathVariable String className){
        return adminService.deleteClasses(className);
    }

    //TODO: check in postman
    @DeleteMapping("/deleteSubject/{subjectName}")
    public String deleteSubject(@PathVariable String subjectName){
        return adminService.deleteSubject(subjectName);
    }

    //TODO: check in postman
    @DeleteMapping("/deleteMarksTable/{className}/{subjectName}")
    public String deleteMarks(@PathVariable String className, @PathVariable String subjectName){
        return adminService.deleteMarks(className, subjectName);
    }

    //TODO: CHANGE REQUEST PARAM TO PARAM VARIABLE

    //TODO: CREATE TEACHER AND STUDENT VISION

}
