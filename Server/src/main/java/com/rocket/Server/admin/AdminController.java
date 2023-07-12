package com.rocket.server.admin;

import com.rocket.server.user.User;
import lombok.RequiredArgsConstructor;
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

    @GetMapping("/getAllUsers")
    public List<User> getAllUsers(){
        return adminService.getAllUsers();
    }


}
