package com.rocket.admin;

import com.rocket.classes.Class;
import com.rocket.classes.ClassResponse;
import com.rocket.marks.*;
import com.rocket.subject.SubjectResponse;
import com.rocket.user.UserRepository;
import com.rocket.user.UserResponse;
import com.rocket.user.UserRole;
import com.rocket.auth.AuthenticationResponse;
import com.rocket.auth.RegisterRequest;
import com.rocket.classes.ClassRepository;
import com.rocket.config.JwtService;
import com.rocket.subject.Subject;
import com.rocket.subject.SubjectRepository;
import com.rocket.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final ClassRepository classRepository;
    private final SubjectRepository subjectRepository;
    private final MarksRepository marksRepository;

    public UserResponse setTeacherRole(String userEmail){
        User teacher = userRepository.findByEmail(userEmail).orElseThrow(() -> new IllegalArgumentException("User with this email "+ userEmail + " is not exist"));
        teacher.setRole(UserRole.TEACHER);
        userRepository.save(teacher);
        return new UserResponse(teacher.getFirstName(), teacher.getLastName(), teacher.getEmail(), teacher.getRole());
    }

    public UserResponse setStudentRole(String userEmail){
        User student = userRepository.findByEmail(userEmail).orElseThrow(() -> new IllegalArgumentException("User with this email "+ userEmail + " is not exist"));
        student.setRole(UserRole.STUDENT);
        userRepository.save(student);
        return new UserResponse(student.getFirstName(), student.getLastName(), student.getEmail(), student.getRole());
    }

    public List<UserResponse> getAllUsers() {
        List<UserResponse> userResponsesList = new ArrayList<>();
        List<User> userList = userRepository.findAll();
        for(int i = 0; i < userList.size(); i++){
            UserResponse user = new UserResponse(userList.get(i).getFirstName(), userList.get(i).getLastName(), userList.get(i).getEmail(), userList.get(i).getRole());
            userResponsesList.add(user);
        }
        return userResponsesList;
    }


    public AuthenticationResponse createStudent(RegisterRequest request) {
        if(userRepository.findByEmail(request.getEmail()).isPresent()){
            throw new IllegalArgumentException("User with email " + request.getEmail() + " is already exists");
        }
        var student = User.builder()
                .firstName(request.getFirstname())
                .lastName(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(UserRole.STUDENT)
                .build();
        userRepository.save(student);
        var jwtToken = jwtService.generateToken(student);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();

    }


    public AuthenticationResponse createTeacher(RegisterRequest request) {
        if(userRepository.findByEmail(request.getEmail()).isPresent()){
            throw new IllegalArgumentException("User with email " + request.getEmail() + " is already exists");
        }
        var teacher = User.builder()
                .firstName(request.getFirstname())
                .lastName(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(UserRole.TEACHER)
                .build();
        userRepository.save(teacher);
        var jwtToken = jwtService.generateToken(teacher);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();

    }


    public String createClasses(String className) {
        if(classRepository.findByName(className).isPresent()){
            throw new IllegalArgumentException("Class with name" + className + "already exist");
        }
        var classes = Class.builder().name(className).build();
        classRepository.save(classes);
        return "Class " + className + " successfully created";
    }


    public ClassResponse setStudentToClass(String userEmail, String className) {
        Class classes = classRepository.findByName(className).orElseThrow(() -> new IllegalArgumentException("Class with this name " + className + "  is not exist"));
        User student = userRepository.findByEmail(userEmail).orElseThrow(() -> new IllegalArgumentException("User with this email "+ userEmail + " is not exist"));
        if(!student.getRole().toString().equals(UserRole.STUDENT.toString())){
           throw new IllegalArgumentException("User with this email "+ userEmail + " has no role student");
        }
        classes.enrolledStudent(student);
        if(marksRepository.findAllByClasses(classes).isPresent()){
            Marks marks = marksRepository.findAllByClasses(classes).orElseThrow(() -> new IllegalArgumentException("Marks table not found"));
            Subject subject = marks.getSubject();
            User teacher = marks.getTeacher();
            Marks newMarks =  Marks.builder().classes(classes)
                    .subject(subject)
                    .teacher(teacher)
                    .student(student)
                    .build();

            marksRepository.save(newMarks);
        }
        classRepository.save(classes);
        ClassResponse classResponse  = new ClassResponse();
        classResponse.setClassName(classes.getName());
        Set<UserResponse> userResponseSet = new HashSet<>();
        for(User user: classes.getEnrolledStudents()){
            userResponseSet.add(new UserResponse(user.getFirstName(), user.getLastName(), user.getEmail(), user.getRole()));
        }
        classResponse.setEnrolledStudents(userResponseSet);
        return classResponse;
    }


    public String createSubject(String subjectName) {
        if(subjectRepository.findByName(subjectName).isPresent()){
            throw new IllegalArgumentException("Subject with name" + subjectName + " already exist");
        }
        var subject = Subject.builder().name(subjectName).build();
        subjectRepository.save(subject);
        return "Subject " + subjectName + " successfully created";
    }


    public SubjectResponse setTeacherToSubject(String userEmail, String subjectName) {
        Subject subject = subjectRepository.findByName(subjectName).orElseThrow(() -> new IllegalArgumentException("Subject with this name " + subjectName + "  is not exist"));
        User teacher = userRepository.findByEmail(userEmail).orElseThrow(() -> new IllegalArgumentException("User with this email "+ userEmail + " is not exist"));
        if(!teacher.getRole().toString().equals(UserRole.TEACHER.toString())){
            throw new IllegalArgumentException("User with this email "+ userEmail + " has no role teacher");
        }
        subject.enrolledTeacher(teacher);
        subjectRepository.save(subject);
        SubjectResponse subjectResponse = new SubjectResponse();
        Set<UserResponse> userResponseSet = new HashSet<>();
        subjectResponse.setSubjectName(subject.getName());
        for(User user : subject.getEnrolledTeachers()){
            userResponseSet.add(new UserResponse(user.getFirstName(), user.getLastName(), user.getEmail(), user.getRole()));
        }
        subjectResponse.setEnrolledTeachers(userResponseSet);
        return subjectResponse;
    }


    public String createMarksTable(MarksTableRequest request) {
        Class classEntity = classRepository.findByName(request.getClassName()).orElseThrow(() -> new IllegalArgumentException("Class not found"));
        Subject subject = subjectRepository.findByName(request.getSubjectName()).orElseThrow(() -> new IllegalArgumentException("Subject not found"));
        User teacher = userRepository.findByEmail(request.getTeacherEmail()).orElseThrow(() -> new IllegalArgumentException("Teacher not found"));
        if(!teacher.getRole().equals(UserRole.TEACHER)){
            throw new IllegalArgumentException("User has no role teacher");
        }
        if(!subject.getEnrolledTeachers().contains(teacher)){
            throw new IllegalArgumentException("That teacher can not teach that subject");
        }
        if(marksRepository.findByClassesAndSubject(classEntity, subject).isPresent()){
            throw new IllegalArgumentException("Marks table for class " + classEntity.getName() + " in subject " + subject.getName() + " is already exists");
        }
        for (User student : classEntity.getEnrolledStudents()) {
            Marks marks =  Marks.builder().classes(classEntity)
                    .subject(subject)
                    .teacher(teacher)
                    .student(student)
                    .build();

            marksRepository.save(marks);
        }
        return "Marks table successfully created";
    }

    public UserResponse deleteUser(String userEmail) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new IllegalArgumentException("User not found"));
        if(user.getRole().equals(UserRole.STUDENT) && classRepository.findByEnrolledStudents(user).isPresent()){
            Class classEntity = classRepository.findByEnrolledStudents(user).orElseThrow( () -> new IllegalArgumentException("Class not found"));
            unsetStudentFromClass(user.getEmail(), classEntity.getName());
        }
        if(user.getRole().equals(UserRole.TEACHER) && subjectRepository.findAllByEnrolledTeachers(user).isPresent()){
            Subject subject = subjectRepository.findAllByEnrolledTeachers(user).orElseThrow( () -> new IllegalArgumentException("Subject not found"));
            unsetTeacherFromSubject(user.getEmail(), subject.getName());
        }
        userRepository.delete(user);
        return new UserResponse(user.getFirstName(), user.getLastName(), user.getEmail(), user.getRole());
    }

    public String deleteClasses(String className) {
        Class classEntity = classRepository.findByName(className).orElseThrow( () -> new IllegalArgumentException("Class not found"));
        if(!marksRepository.removeAllByClasses(classEntity).isPresent()){
            marksRepository.removeAllByClasses(classEntity).orElseThrow( () -> new IllegalArgumentException("Class not found"));
        }
        classRepository.delete(classEntity);
        return "Successfully deleted class " + className;
    }

    public String deleteSubject(String subjectName) {
        Subject subject = subjectRepository.findByName(subjectName).orElseThrow( () -> new IllegalArgumentException("Subject not found"));
        if(!marksRepository.removeAllBySubject(subject).isPresent()){
            marksRepository.removeAllBySubject(subject).orElseThrow( () -> new IllegalArgumentException("Class not found"));
        }
        subjectRepository.delete(subject);
        return "Successfully deleted subject " + subjectName;
    }


    public String deleteMarksTable(String className, String subjectName) {
        Class classEntity = classRepository.findByName(className).orElseThrow( () -> new IllegalArgumentException("Class not found"));
        Subject subject = subjectRepository.findByName(subjectName).orElseThrow( () -> new IllegalArgumentException("Subject not found"));
        marksRepository.deleteAllByClassesAndSubject(classEntity, subject).orElseThrow(() -> new IllegalArgumentException("Marks table not found"));
        return "Successfully deleted class "+ className +" marks table, in subject " + subjectName;
    }


    public UserResponse unsetTeacherFromSubject(String userEmail, String subjectName) {
        User teacher = userRepository.findByEmail(userEmail).orElseThrow(() -> new IllegalArgumentException("Teacher not found"));
        if(!teacher.getRole().equals(UserRole.TEACHER)){
            throw new IllegalArgumentException("User has no role teacher");
        }
        Subject subject = subjectRepository.findByName(subjectName).orElseThrow( () -> new IllegalArgumentException("Subject not found"));
        subject.unrolledTeacher(teacher);
        subjectRepository.save(subject);
        if(!marksRepository.findAllByTeacherAndSubject(teacher, subject).isEmpty()){
            for(Marks mark : marksRepository.findAllByTeacherAndSubject(teacher, subject)){
                User user = new User();
                userRepository.save(user);
                mark.setTeacher(user);
                marksRepository.save(mark);
            }
        }
        return new UserResponse(teacher.getFirstName(), teacher.getLastName(), teacher.getEmail(), teacher.getRole());
    }

    public UserResponse unsetStudentFromClass(String userEmail, String className) {
        User student = userRepository.findByEmail(userEmail).orElseThrow(() -> new IllegalArgumentException("User with this email "+ userEmail + " is not exist"));
        if(!student.getRole().toString().equals(UserRole.STUDENT.toString())){
            throw new IllegalArgumentException("User with this email "+ userEmail + " has no role student");
        }
        Class classEntity = classRepository.findByName(className).orElseThrow( () -> new IllegalArgumentException("Class not found"));
        classEntity.unrolledStudent(student);
        classRepository.save(classEntity);
        if(!marksRepository.removeAllByStudentAndClasses(student, classEntity).isPresent()){
            marksRepository.removeAllByStudentAndClasses(student, classEntity).orElseThrow( () -> new IllegalArgumentException("Class not found"));
        }
        return new UserResponse(student.getFirstName(), student.getLastName(), student.getEmail(), student.getRole());
    }

    public UserResponse changeTeacherInMarksTable(MarksTableRequest request) {
        User teacher = userRepository.findByEmail(request.getTeacherEmail()).orElseThrow(() -> new IllegalArgumentException("Teacher "+ request.getTeacherEmail() + " not found"));
        if(!teacher.getRole().equals(UserRole.TEACHER)){
            throw new IllegalArgumentException("User has no role teacher");
        }
        Class classEntity = classRepository.findByName(request.getClassName()).orElseThrow( () -> new IllegalArgumentException("Class "+ request.getClassName() + " not found"));
        Subject subject = subjectRepository.findByName(request.getSubjectName()).orElseThrow( () -> new IllegalArgumentException("Subject not found"));
        Marks marks = marksRepository.findByClassesAndSubject(classEntity, subject).orElseThrow(() -> new IllegalArgumentException("Marks table not found"));
        marks.setTeacher(teacher);
        marksRepository.save(marks);
        return new UserResponse(teacher.getFirstName(), teacher.getLastName(), teacher.getEmail(), teacher.getRole());
    }


    public List<ClassResponse> getAllClass() {
        List<Class> classList = classRepository.findAll();

        List<ClassResponse> classResponseList  = new ArrayList<>();
        for(Class clas : classList){
            ClassResponse classResponse  = new ClassResponse();
            classResponse.setClassName(clas.getName());
            Set<UserResponse> userResponseSet = new HashSet<>();
            for(User user: clas.getEnrolledStudents()){
                userResponseSet.add(new UserResponse(user.getFirstName(), user.getLastName(), user.getEmail(), user.getRole()));
            }
            classResponse.setEnrolledStudents(userResponseSet);
            classResponseList.add(classResponse);
        }
        return classResponseList;
    }

    public List<SubjectResponse> getAllSubject() {
        List<Subject> subjects = subjectRepository.findAll();
        List<SubjectResponse> subjectResponseList = new ArrayList<>();
        for (var subject : subjects){
            SubjectResponse subjectResponse = new SubjectResponse();
            Set<UserResponse> userResponse = new HashSet<>();
            subjectResponse.setSubjectName(subject.getName());
            for(User user : subject.getEnrolledTeachers()){
                userResponse.add(new UserResponse(user.getFirstName(), user.getLastName(), user.getEmail(), user.getRole()));
            }
            subjectResponse.setEnrolledTeachers(userResponse);
            subjectResponseList.add(subjectResponse);
        }
        return subjectResponseList;
    }

    public List<ClassMarksResponse> getAllMarks() {

        List<Marks> marksList =  marksRepository.findAll();
        Map<String, List<Marks>> classMap = new HashMap<>();
        for(Marks marks : marksList){
            String className = marks.getClasses().getName();
            if (classMap.containsKey(className)) {
                classMap.get(className).add(marks);
            } else {
                List<Marks> newMarksList = new ArrayList<>();
                newMarksList.add(marks);
                classMap.put(className, newMarksList);
            }
        }

        List<ClassMarksResponse> classMarksResponse = new ArrayList<>();
        for (Map.Entry<String, List<Marks>> entry : classMap.entrySet()) {
            String className = entry.getKey();
            List<Marks> marksForClass = entry.getValue();

            Map<UserResponse, Map<LocalDate, Integer>> marksMap = new HashMap<>();

            for (Marks mark : marksForClass) {
                marksMap.put(
                        new UserResponse(mark.getStudent().getFirstName(),
                                mark.getStudent().getLastName(),
                                mark.getStudent().getEmail(),
                                mark.getStudent().getRole()),
                        mark.getMarks()
                );
            }

            classMarksResponse.add(new ClassMarksResponse(
                    className,
                    marksForClass.get(0).getSubject().getName(),
                    new UserResponse(marksForClass.get(0).getTeacher().getFirstName(), marksForClass.get(0).getTeacher().getLastName(), marksForClass.get(0).getTeacher().getEmail(), marksForClass.get(0).getTeacher().getRole()),
                    marksMap
            ));
        }
        return classMarksResponse;
    }

    public ClassMarksResponse setTeacherToMarksTable(MarksTableRequest request) {
        Class classEntity = classRepository.findByName(request.getClassName()).orElseThrow(() -> new IllegalArgumentException("Class not found"));
        Subject subject = subjectRepository.findByName(request.getSubjectName()).orElseThrow(() -> new IllegalArgumentException("Subject not found"));
        User teacher = userRepository.findByEmail(request.getTeacherEmail()).orElseThrow(() -> new IllegalArgumentException("Teacher not found"));
        if(!teacher.getRole().equals(UserRole.TEACHER)){
            throw new IllegalArgumentException("User has no role teacher");
        }
        if(!subject.getEnrolledTeachers().contains(teacher)){
            throw new IllegalArgumentException("That teacher can not teach that subject");
        }
        List<Marks> marks = marksRepository.findAllByClassesAndSubject(classEntity, subject);

        for(Marks mark: marksRepository.findAllByClassesAndSubject(classEntity, subject)){
            mark.setTeacher(teacher);
            marksRepository.save(mark);
        }
        Map<UserResponse, Map<LocalDate, Integer>> marksMap = new HashMap();
        for(Marks mark : marks){
            marksMap.put(new UserResponse(mark.getStudent().getFirstName(), mark.getStudent().getLastName(), mark.getStudent().getEmail(), mark.getStudent().getRole()), mark.getMarks());
        }
        return new ClassMarksResponse(
                classEntity.getName(),
                subject.getName(),
                new UserResponse(teacher.getFirstName(), teacher.getLastName(), teacher.getEmail(), teacher.getRole()),
                marksMap
        );
    }
}
