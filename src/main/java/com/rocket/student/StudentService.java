package com.rocket.student;

import com.rocket.marks.Marks;
import com.rocket.marks.StudentMarksResponse;
import com.rocket.user.UserRepository;
import com.rocket.config.JwtService;
import com.rocket.marks.MarksRepository;
import com.rocket.subject.Subject;
import com.rocket.subject.SubjectRepository;
import com.rocket.user.User;
import com.rocket.user.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final MarksRepository marksRepository;
    private final UserRepository userRepository;
    private final SubjectRepository subjectRepository;
    private final JwtService jwtService;

    public StudentMarksResponse getStudentMarksInSubject(String subjectName, String authorizationHeader) {
        String jwtToken = authorizationHeader.replace("Bearer ", "");
        String studentEmail = jwtService.extractUsername(jwtToken);
        User student = userRepository.findByEmail(studentEmail).orElseThrow(() -> new IllegalArgumentException("User with this email "+ studentEmail + " is not exist"));
        Subject subject = subjectRepository.findByName(subjectName).orElseThrow(() -> new IllegalArgumentException("Subject with this name " + subjectName + "  is not exist"));
        Marks marks = marksRepository.findByStudentAndSubject(student, subject).orElseThrow(() -> new IllegalArgumentException("There is no student " + studentEmail+ " in subject " + subjectName));
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
