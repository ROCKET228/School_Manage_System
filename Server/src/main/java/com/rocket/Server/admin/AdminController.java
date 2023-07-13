package com.rocket.server.admin;

import com.rocket.server.auth.AuthenticationResponse;
import com.rocket.server.auth.RegisterRequest;
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
    public void setTeacherRole(@RequestParam String userEmail){
        adminService.setTeacherRole(userEmail);
    }

    @PutMapping("/setStudent")
    public void setStudentRole(@RequestParam String userEmail){
        adminService.setStudentRole(userEmail);
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


}
