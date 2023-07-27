package com.rocket.server.student;

import com.rocket.server.config.JwtService;
import com.rocket.server.marks.Marks;
import com.rocket.server.marks.MarksRepository;
import com.rocket.server.subject.Subject;
import com.rocket.server.subject.SubjectRepository;
import com.rocket.server.user.User;
import com.rocket.server.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final MarksRepository marksRepository;
    private final UserRepository userRepository;
    private final SubjectRepository subjectRepository;
    private final JwtService jwtService;

    public Marks getStudentMarksInSubject(String subjectName, String authorizationHeader) {
        String jwtToken = authorizationHeader.replace("Bearer ", "");
        String studentEmail = jwtService.extractUsername(jwtToken);
        User student = userRepository.findByEmail(studentEmail).orElseThrow(() -> new IllegalArgumentException("User with this email "+ studentEmail + " is not exist"));
        Subject subject = subjectRepository.findByName(subjectName).orElseThrow(() -> new IllegalArgumentException("Subject with this name " + subjectName + "  is not exist"));
        Marks marks = marksRepository.findByStudentAndSubject(student, subject).orElseThrow(() -> new IllegalArgumentException("There is no student " + studentEmail+ " in subject " + subjectName));
        return marksRepository.save(marks);
    }
}
