package com.rocket.teacher;

import com.rocket.marks.ClassMarksResponse;
import com.rocket.marks.StudentMarksResponse;
import com.rocket.user.UserRepository;
import com.rocket.user.UserResponse;
import com.rocket.user.UserRole;
import com.rocket.classes.Class;
import com.rocket.classes.ClassRepository;
import com.rocket.config.JwtService;
import com.rocket.marks.Marks;
import com.rocket.marks.MarksRepository;
import com.rocket.subject.Subject;
import com.rocket.subject.SubjectRepository;
import com.rocket.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class TeacherService {
    private final UserRepository userRepository;
    private final SubjectRepository subjectRepository;
    private final MarksRepository marksRepository;
    private final ClassRepository classRepository;
    private final JwtService jwtService;

    public StudentMarksResponse setMarksToStudent(String subjectName, String studentMail, Integer mark, String authorizationHeader) {
        User student = userRepository.findByEmail(studentMail).orElseThrow(() -> new IllegalArgumentException("User with this email "+ studentMail + " is not exist"));
        Subject subject = subjectRepository.findByName(subjectName).orElseThrow(() -> new IllegalArgumentException("Subject with this name " + subjectName + "  is not exist"));
        Marks marks = marksRepository.findByStudentAndSubject(student, subject).orElseThrow(() -> new IllegalArgumentException("There is no student " + studentMail+ " in subject " + subjectName));
        String jwtToken = authorizationHeader.replace("Bearer ", "");
        String userEmail = jwtService.extractUsername(jwtToken);
        User teacher = userRepository.findByEmail(userEmail).orElseThrow(() -> new IllegalArgumentException("Teacher not found"));
        if (!teacher.getRole().equals(UserRole.TEACHER)) {
            throw new IllegalArgumentException("User has no role teacher");
        }
        if (!subject.getEnrolledTeachers().contains(teacher)) {
            throw new IllegalArgumentException("That teacher cannot teach that subject");
        }
        if (!marks.getTeacher().equals(teacher)) {
            throw new IllegalArgumentException("That teacher is not in this marks table");
        }
        marks.addMark(mark);
        marksRepository.save(marks);
        return new StudentMarksResponse(
                marks.getClasses().getName(),
                subject.getName(),
                new UserResponse(marks.getTeacher().getFirstName(), marks.getTeacher().getLastName(), marks.getTeacher().getEmail(), marks.getTeacher().getRole()),
                new UserResponse(student.getFirstName(), student.getLastName(), student.getEmail(), student.getRole()),
                marks.getMarks()
        );
    }


    public ClassMarksResponse getClassMarksInSubject(String className, String subjectName, String authorizationHeader) {
        Class classEntity = classRepository.findByName(className).orElseThrow(() -> new IllegalArgumentException("Class with this name " + className + "  is not exist"));
        Subject subject = subjectRepository.findByName(subjectName).orElseThrow(() -> new IllegalArgumentException("Subject with this name " + subjectName + "  is not exist"));
        if(marksRepository.findAllByClassesAndSubject(classEntity, subject).isEmpty()){
            throw new IllegalArgumentException("Marks table not found");
        }
        List<Marks> marks = marksRepository.findAllByClassesAndSubject(classEntity, subject);
        String jwtToken = authorizationHeader.replace("Bearer ", "");
        String userEmail = jwtService.extractUsername(jwtToken);
        User teacher = userRepository.findByEmail(userEmail).orElseThrow(() -> new IllegalArgumentException("Teacher not found"));
        if(marks.size() == 0){
            throw new IllegalArgumentException("Marks table are empty");
        }
        if (!teacher.getRole().equals(UserRole.TEACHER)) {
            throw new IllegalArgumentException("User has no role teacher");
        }
        if (!subject.getEnrolledTeachers().contains(teacher)) {
            throw new IllegalArgumentException("That teacher cannot teach that subject");
        }
        if (!marks.get(0).getTeacher().equals(teacher)) {
            throw new IllegalArgumentException("That teacher is not in this marks table");
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

    public String createMarksTable(String className, String subjectName, String authorizationHeader)
    {
        Class classEntity = classRepository.findByName(className).orElseThrow(() -> new IllegalArgumentException("Class not found"));
        Subject subject = subjectRepository.findByName(subjectName).orElseThrow(() -> new IllegalArgumentException("Subject not found"));
        String jwtToken = authorizationHeader.replace("Bearer ", "");
        String userEmail = jwtService.extractUsername(jwtToken);
        User teacher = userRepository.findByEmail(userEmail).orElseThrow(() -> new IllegalArgumentException("Teacher not found"));
        if (!teacher.getRole().equals(UserRole.TEACHER)) {
            throw new IllegalArgumentException("User has no role teacher");
        }
        if (!subject.getEnrolledTeachers().contains(teacher)) {
            throw new IllegalArgumentException("That teacher cannot teach that subject");
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

    public StudentMarksResponse unsetMarksFromStudent(String subjectName, String studentMail, LocalDate date, Integer mark, String authorizationHeader) {
        User student = userRepository.findByEmail(studentMail).orElseThrow(() -> new IllegalArgumentException("User with this email "+ studentMail + " is not exist"));
        Subject subject = subjectRepository.findByName(subjectName).orElseThrow(() -> new IllegalArgumentException("Subject with this name " + subjectName + "  is not exist"));
        Marks marks = marksRepository.findByStudentAndSubject(student, subject).orElseThrow(() -> new IllegalArgumentException("There is no student " + studentMail+ " in subject " + subjectName));
        String jwtToken = authorizationHeader.replace("Bearer ", "");
        String userEmail = jwtService.extractUsername(jwtToken);
        User teacher = userRepository.findByEmail(userEmail).orElseThrow(() -> new IllegalArgumentException("Teacher not found"));
        if (!teacher.getRole().equals(UserRole.TEACHER)) {
            throw new IllegalArgumentException("User has no role teacher");
        }
        if (!subject.getEnrolledTeachers().contains(teacher)) {
            throw new IllegalArgumentException("That teacher cannot teach that subject");
        }
        if (!marks.getTeacher().equals(teacher)) {
            throw new IllegalArgumentException("That teacher is not in this marks table");
        }

        marks.removeMark(mark, date);
        marksRepository.save(marks);
        return new StudentMarksResponse(
                marks.getClasses().getName(),
                subject.getName(),
                new UserResponse(marks.getTeacher().getFirstName(), marks.getTeacher().getLastName(), marks.getTeacher().getEmail(), marks.getTeacher().getRole()),
                new UserResponse(student.getFirstName(), student.getLastName(), student.getEmail(), student.getRole()),
                marks.getMarks()
        );
    }

    public StudentMarksResponse changeStudentMark(String subjectName, String studentMail, LocalDate date, Integer mark, String authorizationHeader) {
        User student = userRepository.findByEmail(studentMail).orElseThrow(() -> new IllegalArgumentException("User with this email "+ studentMail + " is not exist"));
        Subject subject = subjectRepository.findByName(subjectName).orElseThrow(() -> new IllegalArgumentException("Subject with this name " + subjectName + "  is not exist"));
        Marks marks = marksRepository.findByStudentAndSubject(student, subject).orElseThrow(() -> new IllegalArgumentException("There is no student " + studentMail+ " in subject " + subjectName));
        String jwtToken = authorizationHeader.replace("Bearer ", "");
        String userEmail = jwtService.extractUsername(jwtToken);
        User teacher = userRepository.findByEmail(userEmail).orElseThrow(() -> new IllegalArgumentException("Teacher not found"));
        if (!teacher.getRole().equals(UserRole.TEACHER)) {
            throw new IllegalArgumentException("User has no role teacher");
        }
        if (!subject.getEnrolledTeachers().contains(teacher)) {
            throw new IllegalArgumentException("That teacher cannot teach that subject");
        }
        if (!marks.getTeacher().equals(teacher)) {
            throw new IllegalArgumentException("That teacher is not in this marks table");
        }
        marks.changeMark(mark, date);
        marksRepository.save(marks);
        return new StudentMarksResponse(
                marks.getClasses().getName(),
                subject.getName(),
                new UserResponse(marks.getTeacher().getFirstName(), marks.getTeacher().getLastName(), marks.getTeacher().getEmail(), marks.getTeacher().getRole()),
                new UserResponse(student.getFirstName(), student.getLastName(), student.getEmail(), student.getRole()),
                marks.getMarks()
        );
    }
}
