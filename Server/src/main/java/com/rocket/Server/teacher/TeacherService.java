package com.rocket.server.teacher;

import com.rocket.server.classes.Class;
import com.rocket.server.classes.ClassRepository;
import com.rocket.server.config.JwtService;
import com.rocket.server.marks.Marks;
import com.rocket.server.marks.MarksRepository;
import com.rocket.server.subject.Subject;
import com.rocket.server.subject.SubjectRepository;
import com.rocket.server.user.User;
import com.rocket.server.user.UserRepository;
import com.rocket.server.user.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
public class TeacherService {
    private final UserRepository userRepository;
    private final SubjectRepository subjectRepository;
    private final MarksRepository marksRepository;
    private final ClassRepository classRepository;
    private final JwtService jwtService;

    public Marks setMarksToStudent(String subjectName, String studentMail, Integer mark, String authorizationHeader) {
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
        return marksRepository.save(marks);
    }

    //TODO: fix problem
    public List<Marks> getClassMarksInSubject(String className, String subjectName, String authorizationHeader) {
        Class classEntity = classRepository.findByName(className).orElseThrow(() -> new IllegalArgumentException("Class with this name " + className + "  is not exist"));
        Subject subject = subjectRepository.findByName(subjectName).orElseThrow(() -> new IllegalArgumentException("Subject with this name " + subjectName + "  is not exist"));
        if(marksRepository.findByClassesAndSubject(classEntity, subject).isEmpty()){
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
        return marks;
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


    public Marks unsetMarksFromStudent(String subjectName, String studentMail, LocalDateTime date, Integer mark, String authorizationHeader) {
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
        return marksRepository.save(marks);
    }
}
