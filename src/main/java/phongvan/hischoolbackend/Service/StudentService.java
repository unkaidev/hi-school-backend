package phongvan.hischoolbackend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import phongvan.hischoolbackend.Repository.SchoolClassRepository;
import phongvan.hischoolbackend.Repository.StudentRepository;
import phongvan.hischoolbackend.Repository.TranscriptRepository;
import phongvan.hischoolbackend.Repository.UserRepository;
import phongvan.hischoolbackend.entity.*;

import java.util.*;

@Service
public class StudentService {
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    SchoolClassRepository schoolClassRepository;
    @Autowired
    TranscriptRepository transcriptRepository;

    public Student aStudent(String lastName) {
        Optional<Student> student = studentRepository.findByLastName(lastName);
        return student.orElse(null);
    }

    public List<Student> allStudents() {
        return studentRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    public Page<Student> findPaginated(Integer schoolId, Pageable pageable) {
        List<User> userList = userRepository.findAllByRoles_NameAndSchool_Id(ERole.ROLE_USER, schoolId, Sort.by(Sort.Direction.DESC, "id"));
        List<Student> studentList = new ArrayList<>();
        for (User user : userList) {
            Student studentIndex = studentRepository.findByUser(user);
            if (studentIndex != null) {
                studentList.add(studentIndex);
            }
        }

        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Student> list;
        int totalStudents = studentList.size();

        if (totalStudents < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, totalStudents);
            list = studentList.subList(startItem, toIndex);
        }

        PageRequest pageRequest = PageRequest.of(currentPage, pageSize, Sort.by(Sort.Direction.DESC, "id"));

        return new PageImpl<>(list, pageRequest, totalStudents);
    }

    public Page<Student> findPaginatedInClass(Integer schoolClassId, Pageable pageable) {
        SchoolClass schoolClass = schoolClassRepository.getById(schoolClassId);
        List<Student> studentsInClass = schoolClass.getStudents().stream().toList();
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Student> list;
        int totalStudents = studentsInClass.size();

        if (totalStudents < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, totalStudents);
            list = studentsInClass.subList(startItem, toIndex);
        }

        PageRequest pageRequest = PageRequest.of(currentPage, pageSize, Sort.by(Sort.Direction.DESC, "id"));

        return new PageImpl<>(list, pageRequest, totalStudents);
    }

    public void deleteStudent(int id) {
        studentRepository.deleteById(id);
    }

    public void updateStudent(Student student) {
        studentRepository.save(student);
    }

    public boolean existsByFirstName(String lastName) {
        return studentRepository.existsByLastName(lastName);
    }

    public Student findById(Integer id) {
        Optional<Student> student = studentRepository.findById(id);
        return student.orElse(null);
    }

    public void removeStudentFromClass(int schoolClassId, int studentId) {
        SchoolClass schoolClass = schoolClassRepository.getById(schoolClassId);
        List<Student> studentList = new ArrayList<>(schoolClass.getStudents().stream().toList());
        Student studentToRemove = studentRepository.getById(studentId);
        studentList.remove(studentToRemove);
        schoolClass.setStudents(studentList);
        Transcript transcript = transcriptRepository.findByStudentAndSchoolYear(studentToRemove, schoolClass.getSchoolYear());
        transcriptRepository.delete(transcript);
        schoolClassRepository.save(schoolClass);
    }

    public void removeManyStudentsFromClass(int schoolClassId, List<Integer> students) {
        SchoolClass schoolClass = schoolClassRepository.getById(schoolClassId);
        List<Student> studentList = new ArrayList<>(schoolClass.getStudents().stream().toList());
        for (Integer studentId : students) {
            Student studentToRemove = studentRepository.getById(studentId);
            studentList.remove(studentToRemove);
            Transcript transcript = transcriptRepository.findByStudentAndSchoolYear(studentToRemove, schoolClass.getSchoolYear());
            transcriptRepository.delete(transcript);
        }
        schoolClass.setStudents(studentList);
        schoolClassRepository.save(schoolClass);
    }

    public void addStudentFromClass(int schoolClassId, int studentId) {
        SchoolClass schoolClass = schoolClassRepository.getById(schoolClassId);
        List<Student> studentList = new ArrayList<>(schoolClass.getStudents().stream().toList());
        Student studentToAdd = studentRepository.getById(studentId);
        studentList.add(studentToAdd);
        schoolClass.setStudents(studentList);
        Transcript newTranscript = Transcript.builder()
                .student(studentToAdd)
                .schoolYear(schoolClass.getSchoolYear())
                .build();
        transcriptRepository.save(newTranscript);
        schoolClassRepository.save(schoolClass);
    }

    public void addManyStudentsFromClass(int schoolClassId, List<Integer> students) {
        SchoolClass schoolClass = schoolClassRepository.getById(schoolClassId);
        List<Student> studentList = new ArrayList<>(schoolClass.getStudents().stream().toList());

        for (Integer studentId : students) {
            Student studentToAdd = studentRepository.getById(studentId);
            studentList.add(studentToAdd);
            Transcript newTranscript = Transcript.builder()
                    .student(studentToAdd)
                    .schoolYear(schoolClass.getSchoolYear())
                    .build();
            transcriptRepository.save(newTranscript);
        }
        schoolClass.setStudents(studentList);
        schoolClassRepository.save(schoolClass);
    }

    public Page<Student> findPaginatedNotInClass(Integer schoolClassId, Pageable pageable) {
        SchoolClass schoolClass = schoolClassRepository.getById(schoolClassId);
        List<Student> studentsInClass = schoolClass.getStudents().stream().toList();
        List<Student> allStudentList = studentRepository.findAll();
        List<Student> studentNotInClass = new ArrayList<>();

        for (Student student : allStudentList
        ) {
            if (!studentsInClass.contains(student) && student.getClasses().isEmpty()) {
                studentNotInClass.add(student);
            }
        }


        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Student> list;
        int totalStudents = studentNotInClass.size();

        if (totalStudents < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, totalStudents);
            list = studentNotInClass.subList(startItem, toIndex);
        }

        PageRequest pageRequest = PageRequest.of(currentPage, pageSize, Sort.by(Sort.Direction.DESC, "id"));

        return new PageImpl<>(list, pageRequest, totalStudents);
    }


}
