package phongvan.hischoolbackend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import phongvan.hischoolbackend.Repository.*;
import phongvan.hischoolbackend.entity.*;

import java.util.*;

@Service
public class SchoolService {
    @Autowired
    SchoolRepository schoolRepository;
    @Autowired
    YearRepository yearRepository;
    @Autowired
    SubjectRepository subjectRepository;
    @Autowired
    ScheduleRepository scheduleRepository;
    @Autowired
    SemesterRepository semesterRepository;
    @Autowired
    SchoolClassRepository schoolClassRepository;
    @Autowired
    TranscriptRepository transcriptRepository;
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    UserRepository userRepository;

    public Optional<School> aSchool(String name) {
        return schoolRepository.findByName(name);
    }

    public List<School> allSchools() {
        return schoolRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    public Page<School> findPaginated(Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<School> list;
        List<School> allSchools = allSchools();

        if (allSchools.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, allSchools.size());
            list = allSchools.subList(startItem, toIndex);
        }

        PageRequest pageRequest = PageRequest.of(currentPage, pageSize, Sort.by(Sort.Direction.DESC, "id"));

        Page<School> schoolPage = new PageImpl<>(list, pageRequest, allSchools.size());
        return schoolPage;
    }

    public void deleteSchool(int id) {
        schoolRepository.deleteById(id);
    }

    public void updateSchool(School school) {
        schoolRepository.save(school);
    }

    public boolean existsByName(String name) {
        return schoolRepository.existsByName(name);
    }

    public School findById(Integer id) {
        return schoolRepository.findById(id).get();
    }

    public List<String> findAllSchool_Id() {
        List<String> school_ids = new ArrayList<>();
        try {
            List<School> schoolList = allSchools();
            if (schoolList != null) {
                for (School schoolIndex : schoolList) {
                    school_ids.add(schoolIndex.getId().toString());
                }
            }
            return school_ids;
        } catch (Exception e) {
            return null;
        }
    }

    public School findALatestSchool() {
        return schoolRepository.findFirstByOrderByCreatedAtDesc();
    }

    public List<Object[]> countSchoolsByMonth(int year) {
        return schoolRepository.countSchoolsByMonth(year);
    }

    public List<Object[]> countClassInSchoolByYear(int schoolId) {
        return schoolRepository.countClassInSchoolByYear(schoolId);
    }

    public int countAllYearsInSchool(int schoolId) {
        List<SchoolYear> years = yearRepository.findAllBySchool_Id(schoolId, Sort.by(Sort.Direction.DESC, "id"));
        return years.size();
    }

    public int countAllSemestersInSchool(int schoolId) {
        List<SchoolYear> years = yearRepository.findAllBySchool_Id(schoolId, Sort.by(Sort.Direction.DESC, "id"));
        List<Semester> semesters = new ArrayList<>();
        for (SchoolYear schoolYear : years
        ) {
            semesters.addAll(semesterRepository.findAllBySchoolYear(schoolYear, Sort.by(Sort.Direction.DESC, "id")));
        }
        return semesters.size();
    }

    public int countAllSchedulesInSchool(int schoolId) {
        List<SchoolYear> years = yearRepository.findAllBySchool_Id(schoolId, Sort.by(Sort.Direction.DESC, "id"));
        List<Semester> semesters = new ArrayList<>();
        for (SchoolYear schoolYear : years
        ) {
            semesters.addAll(semesterRepository.findAllBySchoolYear(schoolYear, Sort.by(Sort.Direction.DESC, "id")));
        }
        Collection<Schedule> schedules = new ArrayList<>();
        for (Semester semester : semesters
        ) {
            schedules.addAll(scheduleRepository.findAllBySemester(semester, Sort.by(Sort.Direction.DESC, "id")));
        }
        return schedules.size();
    }

    public int countAllSubjectsInSchool(int schoolId) {
        List<SchoolYear> years = yearRepository.findAllBySchool_Id(schoolId, Sort.by(Sort.Direction.DESC, "id"));
        List<Semester> semesters = new ArrayList<>();
        for (SchoolYear schoolYear : years
        ) {
            semesters.addAll(semesterRepository.findAllBySchoolYear(schoolYear, Sort.by(Sort.Direction.DESC, "id")));
        }
        Collection<Subject> subjects = new ArrayList<>();
        for (Semester semester : semesters
        ) {
            subjects.addAll(subjectRepository.findAllBySemester(semester, Sort.by(Sort.Direction.DESC, "id")));
        }
        return subjects.size();
    }

    public int countAllStudentsInSchool(int schoolId) {
        List<User> users = userRepository.findAllBySchool_Id(schoolId);
        users.removeIf(user -> user.getStudent() == (null));
        return users.size();
    }

    public int countAllTeachersInSchool(int schoolId) {
        List<User> users = userRepository.findAllBySchool_Id(schoolId);
        users.removeIf(user -> user.getTeacher() == (null));
        return users.size();
    }

    public int countAllClassInSchool(int schoolId) {
        List<SchoolYear> years = yearRepository.findAllBySchool_Id(schoolId, Sort.by(Sort.Direction.DESC, "id"));
        List<SchoolClass> schoolClasses = new ArrayList<>();
        for (SchoolYear schoolYear : years
        ) {
            schoolClasses.addAll(schoolClassRepository.findAllBySchoolYear(schoolYear, Sort.by(Sort.Direction.DESC, "id")));
        }
        return schoolClasses.size();
    }

    public int countAllTranscriptsInSchool(int schoolId) {
        List<SchoolYear> years = yearRepository.findAllBySchool_Id(schoolId, Sort.by(Sort.Direction.DESC, "id"));
        List<Transcript> transcripts = new ArrayList<>();
        for (SchoolYear schoolYear : years
        ) {
            transcripts.addAll(transcriptRepository.findAllBySchoolYear(schoolYear, Sort.by(Sort.Direction.DESC, "id")));
        }
        return transcripts.size();
    }

    public int countAllStudentsInSchoolByYearAndGrade(int schoolId, int year, String grade) {
        List<SchoolYear> years = yearRepository.findAllBySchool_Id(schoolId, Sort.by(Sort.Direction.DESC, "id"));
        List<SchoolClass> schoolClasses = new ArrayList<>();
        for (SchoolYear schoolYear : years
        ) {
            if (schoolYear.getId() == year) {
                schoolClasses.addAll(schoolClassRepository.findAllBySchoolYear_IdAndGrade(schoolYear.getId(), grade, Sort.by(Sort.Direction.DESC, "id")));
            }
        }
        Collection<Student> students = new ArrayList<>();
        for (SchoolClass schoolClass: schoolClasses
             ) {
            students.addAll(schoolClass.getStudents());
        }
        return students.size();
    }
}
