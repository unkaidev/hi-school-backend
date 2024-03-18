package phongvan.hischoolbackend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import phongvan.hischoolbackend.Repository.*;
import phongvan.hischoolbackend.entity.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
public class ScoreService {
    @Autowired
    ScoreRepository scoreRepository;
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    SemesterRepository semesterRepository;
    @Autowired
    TeacherRepository teacherRepository;
    @Autowired
    UserRepository userRepository;

    public Page<Score> findPaginated(int semesterId, int studentId, Pageable pageable) {
        List<Score> scores = new ArrayList<>();
        Semester semester = semesterRepository.findById(semesterId).orElse(null);
        Student student = studentRepository.findById(studentId).orElse(null);
        if (semester != null && student != null) {
            scores = scoreRepository.findAllBySemesterAndStudent(semester,student);
        }

        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Score> list;

        if (scores.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, scores.size());
            list = scores.subList(startItem, toIndex);
        }

        PageRequest pageRequest = PageRequest.of(currentPage, pageSize, Sort.by(Sort.Direction.DESC, "id"));

        Page<Score> scorePage = new PageImpl<>(list, pageRequest, scores.size());
        return scorePage;
    }
    public Page<Score> findPaginatedForTeacher(int semesterId, int studentId, String username, Pageable pageable) {
        User user = userRepository.findByUsername(username).orElse(null);
        Teacher teacher = teacherRepository.findByUser(user);
        List<Score> scores = new ArrayList<>();
        Semester semester = semesterRepository.findById(semesterId).orElse(null);
        Student student = studentRepository.findById(studentId).orElse(null);
        if (semester != null && student != null) {
            scores = scoreRepository.findAllBySemesterAndStudentAndTeacher(semester,student,teacher);
        }

        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Score> list;

        if (scores.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, scores.size());
            list = scores.subList(startItem, toIndex);
        }

        PageRequest pageRequest = PageRequest.of(currentPage, pageSize, Sort.by(Sort.Direction.DESC, "id"));

        Page<Score> scorePage = new PageImpl<>(list, pageRequest, scores.size());
        return scorePage;
    }

    public double calSubjectScore(double dailyScore, double midtermScore, double finalScore) {
        return (dailyScore * 0.1 + midtermScore * 0.3 + finalScore * 0.6);
    }

    public Score findById(Integer id) {
        return scoreRepository.findById(id).orElse(null);
    }

    public void save(Score score) {
        scoreRepository.save(score);
    }

    public String getSemesterEvaluation(int semesterId, int studentId) {
        List<Score> scores = new ArrayList<>();
        Semester semester = semesterRepository.findById(semesterId).orElse(null);
        Student student = studentRepository.findById(studentId).orElse(null);
        if (semester != null && student != null) {
            scores = scoreRepository.findAllBySemesterAndStudent(semester, student);
        }
        List<Double> subjectScores = new ArrayList<>();
        for (Score score : scores) {
            subjectScores.add(score.getSubjectScore());
        }
        double semesterEvaluationValue = 0.0;
        if (!subjectScores.isEmpty()) {
            semesterEvaluationValue = subjectScores.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        }
        String subjectEvaluation = "";
        if (semesterEvaluationValue >= 8.5) {
            subjectEvaluation = "Giỏi";
        } else if (semesterEvaluationValue >= 6.5) {
            subjectEvaluation = "Khá";
        } else if (semesterEvaluationValue >= 5) {
            subjectEvaluation = "Trung bình";
        } else {
            subjectEvaluation = "Yếu";
        }

        return subjectEvaluation;
    }

}
