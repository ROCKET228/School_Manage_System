package com.rocket.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rocket.auth.RegisterRequest;
import com.rocket.classes.Class;
import com.rocket.user.UserResponse;
import com.rocket.user.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(controllers = AdminController.class)
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class AdminControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminService adminService;

    @Autowired
    private ObjectMapper objectMapper;

    private Class classes;

    private List<UserResponse> userResponseList;

    @BeforeEach
    public void init(){
        userResponseList = new ArrayList<>();
        classes = Class.builder().name("11-B").build();
        userResponseList.add(new UserResponse("Username", "UserLastName", "user@mail.com", UserRole.USER));
    }

    @Test
    void getAllUsers() throws Exception{
        given(adminService.getAllUsers()).willReturn(userResponseList);

        ResultActions response = mockMvc.perform(get("SchoolManageSystem/admin/getAllUsers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userResponseList)));

        response.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void getAllClass() {
    }

    @Test
    void getAllSubject() {
    }

    @Test
    void getAllMarks() {
    }

    @Test
    void setTeacherRole() {
    }

    @Test
    void setStudentRole() {
    }

    @Test
    void setStudentToClass() {
    }

    @Test
    void setTeacherToSubject() {
    }

    @Test
    void unsetStudentFromClass() {
    }

    @Test
    void unsetTeacherFromSubject() {
    }

    @Test
    void changeTeacherInMarksTable() {
    }

    @Test
    void createStudent() {
    }

    @Test
    void createTeacher() {
    }

    @Test
    void createClasses() {
    }

    @Test
    void createSubject() {
    }

    @Test
    void createMarks() {
    }

    @Test
    void deleteUser() {
    }

    @Test
    void deleteClasses() {
    }

    @Test
    void deleteSubject() {
    }

    @Test
    void deleteMarks() {
    }
}