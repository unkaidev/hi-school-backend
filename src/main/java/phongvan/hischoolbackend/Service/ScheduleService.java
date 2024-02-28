package phongvan.hischoolbackend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import phongvan.hischoolbackend.Repository.ScheduleRepository;
import phongvan.hischoolbackend.entity.Schedule;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ScheduleService {
    @Autowired
    ScheduleRepository scheduleRepository;
    public Schedule findById (Integer id){
        return scheduleRepository.findById(id).get();
    }
    public Optional<Schedule> aSchedule(String name) {
        return scheduleRepository.findByScheduleName(name);
    }

    public List<Schedule> allSchedules() {
        return scheduleRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    public Page<Schedule> findPaginated(Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Schedule> list;
        List<Schedule> allSchedules = allSchedules();

        if (allSchedules.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, allSchedules.size());
            list = allSchedules.subList(startItem, toIndex);
        }

        PageRequest pageRequest = PageRequest.of(currentPage, pageSize, Sort.by(Sort.Direction.DESC, "id"));

        Page<Schedule> schedulePage = new PageImpl<>(list, pageRequest, allSchedules.size());
        return schedulePage;
    }

    public void deleteSchedule(int id) {
        scheduleRepository.deleteById(id);
    }

    public void updateSchedule(Schedule schedule) {
        scheduleRepository.save(schedule);
    }

    public boolean existsByName(String name) {
        return scheduleRepository.existsByScheduleName(name);
    }

}
