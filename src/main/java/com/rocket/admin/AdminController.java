package com.rocket.admin;

import com.rocket.marks.MarksRequest;
import com.rocket.auth.AuthenticationResponse;
import com.rocket.auth.RegisterRequest;
import com.rocket.classes.Class;
import com.rocket.marks.Marks;
import com.rocket.subject.Subject;
import com.rocket.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/SchoolManageSystem/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @GetMapping("/getAllUsers")
    @ResponseStatus(HttpStatus.OK)
    public List<User> getAllUsers(){
        return adminService.getAllUsers();
    }

    @GetMapping("/getAllClass")
    @ResponseStatus(HttpStatus.OK)
    public List<Class> getAllClass(){ return adminService.getAllClass(); }

    @GetMapping("/getAllSubject")
    @ResponseStatus(HttpStatus.OK)
    public List<Subject> getAllSubject(){ return adminService.getAllSubject(); }

    @GetMapping("/getAllMarks")
    @ResponseStatus(HttpStatus.OK)
    public List<Marks> getAllMarks(){ return adminService.getAllMarks(); }


    @PutMapping("/setTeacher")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public User setTeacherRole(@RequestParam String userEmail){
        return adminService.setTeacherRole(userEmail);
    }


    @PutMapping("/setStudent")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public User setStudentRole(@RequestParam String userEmail){
        return adminService.setStudentRole(userEmail);
    }


    @PutMapping("/setStudentToClass")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Class setStudentToClass(@RequestParam String userEmail, @RequestParam String className){
        return adminService.setStudentToClass(userEmail, className);
    }


    @PutMapping("/setTeacherToSubject")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Subject setTeacherToSubject(@RequestParam String userEmail, @RequestParam String subjectName){
        return adminService.setTeacherToSubject(userEmail, subjectName);
    }


    @PutMapping("/unsetStudentFromClass")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public User unsetStudentFromClass(@RequestParam String userEmail, @RequestParam String className){
        return adminService.unsetStudentFromClass(userEmail, className);
    }


    @PutMapping("/unsetTeacherFromSubject")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public User unsetTeacherFromSubject(@RequestParam String userEmail, @RequestParam String subjectName){
        return adminService.unsetTeacherFromSubject(userEmail, subjectName);
    }

    @PutMapping("/changeTeacherInMarksTable")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public User changeTeacherInMarksTable(@RequestBody MarksRequest request){
        return adminService.changeTeacherInMarksTable(request);
    }

    @PostMapping("/createStudent")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<AuthenticationResponse> createStudent(@RequestBody RegisterRequest request){
        return ResponseEntity.ok(adminService.createStudent(request));
    }

    @PostMapping("/createTeacher")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<AuthenticationResponse> createTeacher(@RequestBody RegisterRequest request){
        return ResponseEntity.ok(adminService.createTeacher(request));
    }


    @PostMapping("/createClasses")
    @ResponseStatus(HttpStatus.CREATED)
    public Class createClasses(@RequestParam String className){
        return adminService.createClasses(className);
    }


    @PostMapping("/createSubject")
    @ResponseStatus(HttpStatus.CREATED)
    public Subject createSubject(@RequestParam String subjectName){
        return adminService.createSubject(subjectName);
    }

    @PostMapping("/createMarks")
    @ResponseStatus(HttpStatus.CREATED)
    public String createMarks(@RequestBody MarksRequest request){
        return adminService.createMarks(request);
    }


    @DeleteMapping("/deleteUser")
    @ResponseStatus(HttpStatus.CREATED)
    public User deleteUser(@RequestParam String userEmail){
        return adminService.deleteUser(userEmail);
    }


    @DeleteMapping("/deleteClasses")
    @ResponseStatus(HttpStatus.OK)
    public String deleteClasses(@RequestParam String className){
        return adminService.deleteClasses(className);
    }

    @DeleteMapping("/deleteSubject")
    @ResponseStatus(HttpStatus.OK)
    public String deleteSubject(@RequestParam String subjectName){
        return adminService.deleteSubject(subjectName);
    }

    //TODO: check why its not work
    @DeleteMapping("/deleteMarksTable")
    @ResponseStatus(HttpStatus.OK)
    public String deleteMarks(@RequestParam String className, @RequestParam String subjectName){
        return adminService.deleteMarks(className, subjectName);
    }

}
