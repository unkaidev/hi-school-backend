package phongvan.hischoolbackend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import phongvan.hischoolbackend.Repository.AttendanceRepository;
import phongvan.hischoolbackend.Repository.SchoolClassRepository;
import phongvan.hischoolbackend.Repository.StudentRepository;
import phongvan.hischoolbackend.Repository.UserRepository;
import phongvan.hischoolbackend.entity.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
public class AttendanceService {
    @Autowired
    AttendanceRepository attendanceRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    SchoolClassRepository schoolClassRepository;

    public Page<Attendance> findPaginated(int timeTableId, int scheduleId, int teacherId,int classId, Pageable pageable) {
        List<Attendance> attendances = new ArrayList<>();
        SchoolClass schoolClass = schoolClassRepository.findById(classId).orElse(null);
        if(schoolClass!=null){
            Collection<Student> students = schoolClass.getStudents();
            for (Student student: students){
                int studentId = student.getId();
            attendances.addAll(attendanceRepository.findAllByTimeTable_IdAndSchedule_IdAndTeacher_IdAndStudent_Id(timeTableId,scheduleId,teacherId,studentId,Sort.by(Sort.Direction.DESC, "id")));
            }
        }

        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Attendance> list;

        if (attendances.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, attendances.size());
            list = attendances.subList(startItem, toIndex);
        }

        PageRequest pageRequest = PageRequest.of(currentPage, pageSize, Sort.by(Sort.Direction.DESC, "id"));

        Page<Attendance> timeTablePage = new PageImpl<>(list, pageRequest, attendances.size());
        return timeTablePage;
    }

    public Attendance AnAttendance(int id) {
        return attendanceRepository.findById(id).orElse(null);
    }

    public void save(Attendance attendance) {
        attendanceRepository.save(attendance);
    }


}
