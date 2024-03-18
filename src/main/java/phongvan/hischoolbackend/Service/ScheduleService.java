package phongvan.hischoolbackend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import phongvan.hischoolbackend.Repository.ScheduleRepository;
import phongvan.hischoolbackend.Repository.SemesterRepository;
import phongvan.hischoolbackend.Repository.YearRepository;
import phongvan.hischoolbackend.entity.Schedule;
import phongvan.hischoolbackend.entity.SchoolYear;
import phongvan.hischoolbackend.entity.Semester;
import phongvan.hischoolbackend.entity.Subject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ScheduleService {
    @Autowired
    ScheduleRepository scheduleRepository;
    @Autowired
    YearRepository yearRepository;

    @Autowired
    SemesterRepository semesterRepository;

    public Schedule findById (Integer id){
        Optional<Schedule> schedule = scheduleRepository.findById(id);
        return schedule.orElse(null);
    }
    public Optional<Schedule> aSchedule(String name) {
        return scheduleRepository.findByName(name);
    }

    public List<Schedule> allSchedules() {
        return scheduleRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    public Page<Schedule> findPaginated(Integer schoolId, Pageable pageable) {
        List<SchoolYear> yearList = yearRepository.findAllBySchool_Id(schoolId, Sort.by(Sort.Direction.DESC, "id"));
        List<Semester> semesterList = new ArrayList<>();

        for (SchoolYear schoolYear : yearList
        ) {
            semesterList.addAll(semesterRepository.findAllBySchoolYear(schoolYear, Sort.by(Sort.Direction.DESC, "id")));
        }
        List<Schedule> scheduleList = new ArrayList<>();

        for (Semester semester : semesterList
        ) {
            scheduleList.addAll(scheduleRepository.findAllBySemester(semester, Sort.by(Sort.Direction.DESC, "id")));
        }

        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Schedule> list;

        if (scheduleList.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, scheduleList.size());
            list = scheduleList.subList(startItem, toIndex);
        }

        PageRequest pageRequest = PageRequest.of(currentPage, pageSize, Sort.by(Sort.Direction.DESC, "id"));

        Page<Schedule> schedulePage = new PageImpl<>(list, pageRequest, scheduleList.size());
        return schedulePage;
    }

    public void deleteSchedule(int id) {
        scheduleRepository.deleteById(id);
    }

    public void updateSchedule(Schedule schedule) {
        scheduleRepository.save(schedule);
    }

    public boolean existsByName(String name) {
        return scheduleRepository.existsByName(name);
    }

    public List<Schedule> findAllByName(String scheduleName) {
        return scheduleRepository.findAllByName(scheduleName);
    }

    public List<Schedule> findAllBySemester_Id(int semesterId) {
        return scheduleRepository.findAllBySemester_Id(semesterId,Sort.by(Sort.Direction.ASC, "id"));
    }
}
