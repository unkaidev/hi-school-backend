package phongvan.hischoolbackend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import phongvan.hischoolbackend.Repository.*;
import phongvan.hischoolbackend.entity.*;

import java.util.*;

@Service
public class SchoolClassService {
    @Autowired
    SchoolClassRepository schoolClassRepository;
    @Autowired
    YearRepository yearRepository;
    @Autowired
    ScoreRepository scoreRepository;
    @Autowired
    SemesterRepository semesterRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    TeacherRepository teacherRepository;
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    TeacherAssignmentRepository teacherAssignmentRepository;


    public SchoolClass findById(Integer id) {
        Optional<SchoolClass> schoolClass = schoolClassRepository.findById(id);
        return schoolClass.orElse(null);
    }

    public Optional<SchoolClass> aSchoolClass(String name) {
        return schoolClassRepository.findByName(name);
    }

    public List<SchoolClass> allSchoolClasses() {
        return schoolClassRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    public Page<SchoolClass> findPaginated(Integer schoolId, Pageable pageable) {

        List<SchoolYear> yearList = yearRepository.findAllBySchool_Id(schoolId, Sort.by(Sort.Direction.DESC, "id"));
        List<SchoolClass> schoolClassList = new ArrayList<>();

        for (SchoolYear schoolYear : yearList
        ) {
            schoolClassList.addAll(schoolClassRepository.findAllBySchoolYear(schoolYear, Sort.by(Sort.Direction.DESC, "id")));
        }

        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<SchoolClass> list;

        if (schoolClassList.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, schoolClassList.size());
            list = schoolClassList.subList(startItem, toIndex);
        }

        PageRequest pageRequest = PageRequest.of(currentPage, pageSize, Sort.by(Sort.Direction.DESC, "id"));

        Page<SchoolClass> schoolClassPage = new PageImpl<>(list, pageRequest, schoolClassList.size());
        return schoolClassPage;
    }

    public Page<SchoolClass> findPaginatedForHeadTeacher(String username, Integer schoolId, Pageable pageable) {

        List<SchoolClass> schoolClasses = new ArrayList<>();
        List<SchoolYear> schoolYears = yearRepository.findAllBySchool_Id(schoolId, Sort.by(Sort.Direction.DESC, "id"));
        for (SchoolYear schoolYear : schoolYears
        ) {
            List<SchoolClass> schoolClassList = schoolClassRepository.findAllBySchoolYear_Id(schoolYear.getId(), Sort.by(Sort.Direction.DESC, "id"));
            for (SchoolClass schoolClass : schoolClassList
            ) {
                if (schoolClass.getTeacher().getUser().getUsername().equals(username)) {
                    schoolClasses.add(schoolClass);
                }
            }
        }

        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<SchoolClass> list;

        if (schoolClasses.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, schoolClasses.size());
            list = schoolClasses.subList(startItem, toIndex);
        }

        PageRequest pageRequest = PageRequest.of(currentPage, pageSize, Sort.by(Sort.Direction.DESC, "id"));

        Page<SchoolClass> schoolClassPage = new PageImpl<>(list, pageRequest, schoolClasses.size());
        return schoolClassPage;
    }

    public void deleteSchoolClass(int id) {
        schoolClassRepository.deleteById(id);
    }

    public void updateSchoolClass(SchoolClass schoolClass) {
        schoolClassRepository.save(schoolClass);
    }

    public boolean existsByName(String name) {
        return schoolClassRepository.existsByName(name);
    }

    public List<SchoolClass> findAllByName(String schoolClassName) {
        return schoolClassRepository.findAllByName(schoolClassName);
    }

    public boolean existsByNameAndSchoolYearId(String schoolClassName, Integer id) {
        return schoolClassRepository.existsByNameAndSchoolYear_Id(schoolClassName, id);
    }

    public List<SchoolClass> findAllBySchoolYear(SchoolYear schoolYear) {
        return schoolClassRepository.findAllBySchoolYear(schoolYear, Sort.by(Sort.Direction.DESC, "id"));
    }

    public List<SchoolClass> findAllBySchoolYear_Id(int yearId) {
        return schoolClassRepository.findAllBySchoolYear_Id(yearId, Sort.by(Sort.Direction.DESC, "id"));
    }

    public List<SchoolClass> findAllBySchoolYear_IdAndGradeForHeadteacher(int yearId, String username, String grade) {
        List<SchoolClass> schoolClasses = new ArrayList<>();
        List<SchoolClass> schoolClassList = schoolClassRepository.findAllBySchoolYear_Id(yearId, Sort.by(Sort.Direction.DESC, "id"));
        for (SchoolClass schoolClass : schoolClassList
        ) {
            if (schoolClass.getTeacher().getUser().getUsername().equals(username) && schoolClass.getGrade().equals(grade)) {
                schoolClasses.add(schoolClass);
            }
        }
        return schoolClasses;
    }
    public List<SchoolClass> findAllBySchoolYear_IdAndGradeForStudent(int yearId, String username, String grade) {
        List<SchoolClass> schoolClasses = new ArrayList<>();
        List<SchoolClass> schoolClassList = schoolClassRepository.findAllBySchoolYear_Id(yearId, Sort.by(Sort.Direction.DESC, "id"));
        for (SchoolClass schoolClass : schoolClassList
        ) {
            Collection<Student> students = schoolClass.getStudents();
            for (Student student: students
                 ) {
                if (student.getUser().getUsername().equals(username) && schoolClass.getGrade().equals(grade)) {
                    schoolClasses.add(schoolClass);
                }
            }
        }
        return schoolClasses;
    }

    public List<SchoolClass> findAllBySemester_IdAndTeacher(int semesterId, String username) {
        List<SchoolClass> schoolClasses = new ArrayList<>();
        List<TeacherAssignment> assignmentList = teacherAssignmentRepository.findAllBySemester_Id(semesterId);
        for (TeacherAssignment assignment : assignmentList
        ) {
            if (assignment.getTeacher().getUser().getUsername().equals(username)) {
                schoolClasses.addAll(assignment.getClasses());
            }
        }
        return schoolClasses;
    }

    public Set<SchoolClass> findAllBySemester_IdAndStudent(int semesterId, String username) {
        Set<SchoolClass> schoolClasses = new HashSet<>();

        List<TeacherAssignment> assignmentList = teacherAssignmentRepository.findAllBySemester_Id(semesterId);
        User user = userRepository.findByUsername(username).orElse(null);
        if (user != null) {
            Student student = studentRepository.findByUser(user);
            Collection<SchoolClass> classes = student.getClasses();

            for (TeacherAssignment assignment : assignmentList
            ) {
                if (assignment.getClasses().containsAll(classes)) {
                    schoolClasses.addAll(assignment.getClasses());
                }
            }
            return schoolClasses;
        }
        return schoolClasses;
    }

    public List<SchoolClass> findAllBySchoolYear_IdAndGrade(int yearId, String grade) {
        return schoolClassRepository.findAllBySchoolYear_IdAndGrade(yearId, grade, Sort.by(Sort.Direction.DESC, "id"));
    }

    public List<SchoolClass> findAllBySchool(int schoolId) {
        List<SchoolYear> yearList = yearRepository.findAllBySchool_Id(schoolId, Sort.by(Sort.Direction.DESC, "id"));
        List<SchoolClass> schoolClassList = new ArrayList<>();

        for (SchoolYear schoolYear : yearList
        ) {
            schoolClassList.addAll(schoolClassRepository.findAllBySchoolYear(schoolYear, Sort.by(Sort.Direction.DESC, "id")));
        }
        return schoolClassList;
    }

    public Set<SchoolClass> findAllBySemesterIdAndHeadTeacher(int semesterId, String username) {
        Set<SchoolClass> schoolClasses = new HashSet<>();

        List<TeacherAssignment> assignmentList = teacherAssignmentRepository.findAllBySemester_Id(semesterId);
        for (TeacherAssignment assignment : assignmentList
        ) {
            if (assignment.getTeacher().getUser().getUsername().equals(username)) {
                schoolClasses.addAll(assignment.getClasses());
            }
        }
        Semester semester = semesterRepository.findById(semesterId).orElse(null);
        if(semester!=null){
            List<SchoolClass> schoolClassList = schoolClassRepository.findAllBySchoolYear_Id(semester.getSchoolYear().getId(), Sort.by(Sort.Direction.DESC, "id"));
            for (SchoolClass schoolClass : schoolClassList
            ) {
                if (schoolClass.getTeacher().getUser().getUsername().equals(username)) {
                    schoolClasses.add(schoolClass);
                }
            }
        }
        return schoolClasses;
    }

    public int countStudentsInClass(int classId) {
        SchoolClass schoolClass = schoolClassRepository.findById(classId).orElse(null);
        if (schoolClass != null) {
            return schoolClass.classSize();
        }
        return 0;
    }
}
