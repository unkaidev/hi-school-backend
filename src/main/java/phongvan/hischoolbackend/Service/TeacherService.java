package phongvan.hischoolbackend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import phongvan.hischoolbackend.Repository.TeacherRepository;
import phongvan.hischoolbackend.Repository.TimeTableDetailRepository;
import phongvan.hischoolbackend.Repository.TimeTableRepository;
import phongvan.hischoolbackend.Repository.UserRepository;
import phongvan.hischoolbackend.entity.ERole;
import phongvan.hischoolbackend.entity.Teacher;
import phongvan.hischoolbackend.entity.TimeTableDetail;
import phongvan.hischoolbackend.entity.User;

import java.util.*;

@Service
public class TeacherService {
    @Autowired
    TeacherRepository teacherRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    TimeTableDetailRepository timeTableDetailRepository;
    public Teacher aTeacher(String lastName) {
        Optional<Teacher> teacher = teacherRepository.findByLastName(lastName);
        return teacher.orElse(null);
    }

    public List<Teacher> allTeachers() {
        return teacherRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    public Page<Teacher> findPaginated(Integer schoolId, Pageable pageable) {
        List<User> userList = userRepository.findAllByRoles_NameNotInAndSchoolId(Arrays.asList(ERole.ROLE_MANAGER,ERole.ROLE_ADMIN,ERole.ROLE_USER), schoolId, Sort.by(Sort.Direction.DESC, "id"));
        List<Teacher> teacherList = new ArrayList<>();
        for (User user : userList) {
            Teacher teacherIndex = teacherRepository.findByUser(user);
            if (teacherIndex != null) {
                teacherList.add(teacherIndex);
            }
        }

        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Teacher> list;
        int totalTeachers = teacherList.size();

        if (totalTeachers < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, totalTeachers);
            list = teacherList.subList(startItem, toIndex);
        }

        PageRequest pageRequest = PageRequest.of(currentPage, pageSize, Sort.by(Sort.Direction.DESC, "id"));

        return new PageImpl<>(list, pageRequest, totalTeachers);
    }

    public void deleteTeacher(int id) {
        teacherRepository.deleteById(id);
    }

    public void updateTeacher(Teacher teacher) {
        teacherRepository.save(teacher);
    }

    public boolean existsByFirstName(String lastName) {
        return teacherRepository.existsByLastName(lastName);
    }

    public Teacher findById(Integer id) {
        Optional<Teacher> teacher = teacherRepository.findById(id);
        return teacher.orElse(null);
    }

    public List<Teacher> findAllInSchool(int schoolId) {
        List<User> userList = userRepository.findAllByRoles_NameAndSchool_Id(ERole.ROLE_TEACHER,schoolId,Sort.by(Sort.Direction.DESC, "id"));
        List<Teacher> teacherList = new ArrayList<>();
        for (User user: userList
             ) {
            Teacher teacher = teacherRepository.findByUser(user);
            teacherList.add(teacher);
        }
        return teacherList;
    }

    public List<Teacher> findAllInSchool_IdAndGroup(int schoolId, String group) {
        List<User> userList = userRepository.findAllByRoles_NameAndSchool_Id(ERole.ROLE_TEACHER,schoolId,Sort.by(Sort.Direction.DESC, "id"));
        List<Teacher> teacherList = new ArrayList<>();
        for (User user: userList
        ) {
            Teacher teacher = teacherRepository.findByUser(user);
            if(teacher.getGroup().equals(group)){
                teacherList.add(teacher);
            }
        }
        return teacherList;
    }

    public List<Teacher> findAllInSchool_IdAndGroup_Ready(int schoolId, String group) {
        List<User> userList = userRepository.findAllByRoles_NameAndSchool_Id(ERole.ROLE_TEACHER,schoolId,Sort.by(Sort.Direction.DESC, "id"));
        List<Teacher> teacherList = new ArrayList<>();
        List<Teacher> teachersNotReady = teacherRepository.findAllByTimeTableDetailsNotEmpty();
        for (User user: userList
        ) {
            Teacher teacher = teacherRepository.findByUser(user);
            if(teacher.getGroup().equals(group)){
                teacherList.add(teacher);
            }
        }
        if(!group.equals("Ban giám hiệu")){
        teacherList.removeAll(teachersNotReady);}
        return teacherList;
    }

    public List<Teacher> findAllInSchool_IdAndGroupAndSchedulesReady(int schoolId, String group, List<Integer> selectedSchedules) {
        List<User> userList = userRepository.findAllByRoles_NameAndSchool_Id(ERole.ROLE_TEACHER,schoolId,Sort.by(Sort.Direction.DESC, "id"));
        List<Teacher> teacherList = new ArrayList<>();
        List<Teacher> allTeachersInGroup = new ArrayList<>();
        for (User user: userList
        ) {
            Teacher teacher = teacherRepository.findByUser(user);
            if(teacher.getGroup().equals(group)){
                allTeachersInGroup.add(teacher);
            }
        }

        for (Teacher teacher : allTeachersInGroup) {
            boolean teacherAvailable = true;
            for (TimeTableDetail timeTableDetail : teacher.getTimeTableDetails()) {
                if (selectedSchedules.contains(timeTableDetail.getSchedule().getId())) {
                    teacherAvailable = false;
                    break;
                }
            }
            if (teacherAvailable) {
                teacherList.add(teacher);
            }
        }

        return teacherList;
    }
    public boolean hasScheduledTimeTableForWeekAndDay(Teacher teacher,List<String> weeks, List<String> days, List<Integer> scheduleIds) {
        List<TimeTableDetail> timeTableDetailList = timeTableDetailRepository.findAllByTeacher(teacher);
        if (timeTableDetailList != null) {
            for (TimeTableDetail detail : timeTableDetailList) {
                if (detail.getSchedule() != null && detail.getTimeTable() != null &&
                        weeks.contains(detail.getTimeTable().getStudyWeek()) && days.contains(detail.getTimeTable().getStudyDay()) &&
                        scheduleIds.contains(detail.getSchedule().getId())) {
                    return true;
                }
            }
        }
        return false;
    }

}
