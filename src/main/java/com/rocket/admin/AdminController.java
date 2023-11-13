package com.rocket.admin;

import com.rocket.classes.ClassResponse;
import com.rocket.marks.ClassMarksResponse;
import com.rocket.marks.MarksTableRequest;
import com.rocket.auth.AuthenticationResponse;
import com.rocket.auth.RegisterRequest;
import com.rocket.subject.SubjectResponse;
import com.rocket.user.UserResponse;
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
    public List<UserResponse> getAllUsers(){
        return adminService.getAllUsers();
    }

    @GetMapping("/getAllClass")
    @ResponseStatus(HttpStatus.OK)
    public List<ClassResponse> getAllClass(){ return adminService.getAllClass(); }

    @GetMapping("/getAllSubject")
    @ResponseStatus(HttpStatus.OK)
    public List<SubjectResponse> getAllSubject(){ return adminService.getAllSubject(); }

    @GetMapping("/getAllMarks")
    @ResponseStatus(HttpStatus.OK)
    public List<ClassMarksResponse> getAllMarks(){ return adminService.getAllMarks(); }


    @PutMapping("/setTeacher")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public UserResponse setTeacherRole(@RequestParam String userEmail){
        return adminService.setTeacherRole(userEmail);
    }


    @PutMapping("/setStudent")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public UserResponse setStudentRole(@RequestParam String userEmail){
        return adminService.setStudentRole(userEmail);
    }


    @PutMapping("/setStudentToClass")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ClassResponse setStudentToClass(@RequestParam String userEmail, @RequestParam String className){
        return adminService.setStudentToClass(userEmail, className);
    }


    @PutMapping("/setTeacherToSubject")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public SubjectResponse setTeacherToSubject(@RequestParam String userEmail, @RequestParam String subjectName){
        return adminService.setTeacherToSubject(userEmail, subjectName);
    }


    @PutMapping("/unsetStudentFromClass")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public UserResponse unsetStudentFromClass(@RequestParam String userEmail, @RequestParam String className){
        return adminService.unsetStudentFromClass(userEmail, className);
    }


    @PutMapping("/unsetTeacherFromSubject")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public UserResponse unsetTeacherFromSubject(@RequestParam String userEmail, @RequestParam String subjectName){
        return adminService.unsetTeacherFromSubject(userEmail, subjectName);
    }

    @PutMapping("/changeTeacherInMarksTable")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public UserResponse changeTeacherInMarksTable(@RequestBody MarksTableRequest request){
        return adminService.changeTeacherInMarksTable(request);
    }

    @PostMapping("/createStudent")
    public ResponseEntity<AuthenticationResponse> createStudent(@RequestBody RegisterRequest request){
        return ResponseEntity.ok(adminService.createStudent(request));
    }

    @PostMapping("/createTeacher")
    public ResponseEntity<AuthenticationResponse> createTeacher(@RequestBody RegisterRequest request){
        return ResponseEntity.ok(adminService.createTeacher(request));
    }


    @PostMapping("/createClasses")
    @ResponseStatus(HttpStatus.CREATED)
    public String createClasses(@RequestParam String className){
        return adminService.createClasses(className);
    }


    @PostMapping("/createSubject")
    @ResponseStatus(HttpStatus.CREATED)
    public String createSubject(@RequestParam String subjectName){
        return adminService.createSubject(subjectName);
    }

    @PostMapping("/createMarksTable")
    @ResponseStatus(HttpStatus.CREATED)
    public String createMarksTable(@RequestBody MarksTableRequest request){
        return adminService.createMarksTable(request);
    }
    //TODO: check why not work
    @DeleteMapping("/deleteUser")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse deleteUser(@RequestParam String userEmail){
        return adminService.deleteUser(userEmail);
    }
    //TODO: check why not work
    @DeleteMapping("/deleteClasses")
    @ResponseStatus(HttpStatus.OK)
    public String deleteClasses(@RequestParam String className){
        return adminService.deleteClasses(className);
    }
    //TODO: check why not work
    @DeleteMapping("/deleteSubject")
    @ResponseStatus(HttpStatus.OK)
    public String deleteSubject(@RequestParam String subjectName){
        return adminService.deleteSubject(subjectName);
    }
    //TODO: check why not work
    @DeleteMapping("/deleteMarksTable")
    @ResponseStatus(HttpStatus.OK)
    public String deleteMarksTable(@RequestParam String className, @RequestParam String subjectName){
        return adminService.deleteMarksTable(className, subjectName);
    }

}
