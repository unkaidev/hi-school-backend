package phongvan.hischoolbackend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import phongvan.hischoolbackend.Repository.*;
import phongvan.hischoolbackend.entity.*;

import java.util.*;

@Service
public class TimeTableDetailService {
    @Autowired
    TimeTableDetailRepository repository;
    @Autowired
    TimeTableRepository timeTableRepository;
    @Autowired
    SchoolClassRepository schoolClassRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    TeacherRepository teacherRepository;
    @Autowired
    StudentRepository studentRepository;

    public Page<TimeTableDetail> findPaginated(int semesterId, int schoolClassId, Pageable pageable) {
        SchoolClass schoolClass = schoolClassRepository.findById(schoolClassId).orElse(null);

        List<TimeTableDetail> timeTableDetails = new ArrayList<>();
        List<TimeTable> timeTables = timeTableRepository.findAllBySemester_Id(semesterId);

        for (TimeTable timeTable : timeTables) {
            List<TimeTableDetail> timeTableDetail = repository.findAllBySchoolClassAndTimeTable(schoolClass, timeTable);
            timeTableDetails.addAll(timeTableDetail);
        }
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<TimeTableDetail> list;

        if (timeTableDetails.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, timeTableDetails.size());
            list = timeTableDetails.subList(startItem, toIndex);
        }

        PageRequest pageRequest = PageRequest.of(currentPage, pageSize, Sort.by(Sort.Direction.DESC, "id"));

        Page<TimeTableDetail> timeTablePage = new PageImpl<>(list, pageRequest, timeTableDetails.size());
        return timeTablePage;
    }

    public void save(TimeTableDetail newTableDetail) {
        repository.save(newTableDetail);
    }

    public void deleteTimeDate(int id) {
        repository.deleteById(id);
    }

    public void deleteManyTimeDate(List<Integer> timeTables) {
        for (Integer i : timeTables
        ) {
            repository.deleteById(i);
        }
    }

    public TimeTableDetail ATimeTableDetail(int id) {
        return repository.findById(id).orElse(null);
    }

    public List<TimeTableDetail> findAllByTeacherAndSubject(Subject subject, Teacher teacher) {
        List<TimeTableDetail> timeTableDetailList = new ArrayList<>();

        List<TimeTableDetail> timeTableDetailsWithTeacher = repository.findAllBySubject(subject);
        for (TimeTableDetail detail : timeTableDetailsWithTeacher
        ) {
            if (detail.getTeacher().equals(teacher)) {
                timeTableDetailList.add(detail);
            }
        }
        return timeTableDetailList;
    }

    public TimeTableDetail findById(Integer id) {
        return repository.findById(id).orElse(null);
    }

    public List<TimeTableDetail> findALlByTeacher(String username) {
        User user = userRepository.findByUsername(username).orElse(null);
        if(user!= null){
            Teacher teacher = teacherRepository.findByUser(user);
            return repository.findAllByTeacher(teacher);
        }
        return null;
    }

    public List<TimeTableDetail> findAllByStudent(String username) {
        User user = userRepository.findByUsername(username).orElse(null);
        List<TimeTableDetail> timeTableDetailList = new ArrayList<>();
        if(user!= null){
            Student student = studentRepository.findByUser(user);
            Collection<SchoolClass> classes = student.getClasses();
            for (SchoolClass schoolClass: classes
                 ) {
                timeTableDetailList.addAll(repository.findAllBySchoolClass(schoolClass));
            }
        }
        return timeTableDetailList;
    }
}
