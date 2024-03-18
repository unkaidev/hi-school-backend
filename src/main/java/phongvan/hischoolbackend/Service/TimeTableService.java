package phongvan.hischoolbackend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import phongvan.hischoolbackend.Repository.SemesterRepository;
import phongvan.hischoolbackend.Repository.TimeTableRepository;
import phongvan.hischoolbackend.entity.Semester;
import phongvan.hischoolbackend.entity.TimeTable;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

@Service

public class TimeTableService {
    @Autowired
    TimeTableRepository timeTableRepository;
    @Autowired
    static SemesterRepository semesterRepository;

    public static List<TimeTable> createTimetableFromStudyPeriod(String start_date, String end_date, Semester semester) {
        List<TimeTable> timetableList = new ArrayList<>();

        LocalDate startDate = LocalDate.parse(start_date, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        LocalDate endDate = LocalDate.parse(end_date, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        if (endDate.getDayOfWeek() != DayOfWeek.SUNDAY) {
            endDate = endDate.with(TemporalAdjusters.next(DayOfWeek.SUNDAY));
        }

        LocalDate currentDate = startDate.with(DayOfWeek.MONDAY);

        int weekNumber = 1;

        while (!currentDate.isAfter(endDate)) {
            LocalDate weekStart = currentDate;
            LocalDate weekEnd = weekStart.plusDays(6);

            String weekDescription = "Tuần " + weekNumber + " (" + weekStart.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + " - " + weekEnd.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + ")";

            while (!currentDate.isAfter(weekEnd) && !currentDate.isAfter(endDate)) {
                int dayOfWeek = currentDate.getDayOfWeek().getValue();

                String dayName;

                if (dayOfWeek == 1) {
                    dayName = "Thứ 2";
                } else if (dayOfWeek == 2) {
                    dayName = "Thứ 3";
                } else if (dayOfWeek == 3) {
                    dayName = "Thứ 4";
                } else if (dayOfWeek == 4) {
                    dayName = "Thứ 5";
                } else if (dayOfWeek == 5) {
                    dayName = "Thứ 6";
                } else if (dayOfWeek == 6) {
                    dayName = "Thứ 7";
                } else {
                    dayName = "Chủ Nhật";
                }

                TimeTable timetableEntry = new TimeTable();
                timetableEntry.setStudyDay(dayName);
                timetableEntry.setStudyDate(String.valueOf(currentDate));
                timetableEntry.setStudyWeek(weekDescription);
                timetableEntry.setSemester(semester);

                timetableList.add(timetableEntry);

                currentDate = currentDate.plusDays(1);
            }

            weekNumber++;

            while (currentDate.getDayOfWeek() != DayOfWeek.MONDAY && !currentDate.isAfter(endDate)) {
                currentDate = currentDate.plusDays(1);
            }
        }

        return timetableList;
    }




    public void updateTimeTable(TimeTable timeTable) {
        timeTableRepository.save(timeTable);
    }

    public void saveManyTimeTable(List<TimeTable> timeTables) {
        timeTableRepository.saveAll(timeTables);
    }

    public List<TimeTable> findAllBySemester(Semester semesterFind) {
        return timeTableRepository.findAllBySemester(semesterFind);
    }

    public void deleteAll(List<TimeTable> existTimeTables) {
        timeTableRepository.deleteAll(existTimeTables);
    }

    public Set<String> findAllWeekBySemester_Id(int semesterId) {
        Set<String> timeWeek = new TreeSet<>();
        List<TimeTable> timeTableList = timeTableRepository.findAllBySemester_Id(semesterId);
        for (TimeTable timeTable: timeTableList
             ) {
            timeWeek.add(timeTable.getStudyWeek());
        }
        return  timeWeek;
    }

    public List<TimeTable> findAllBySemester_Id(int semesterId) {
        return timeTableRepository.findAllBySemester_Id(semesterId);
    }

    public List<TimeTable> findAllByWeek(Set<String> studyWeeks) {
        List<TimeTable> timeTableList = new ArrayList<>();
        for (String study_week: studyWeeks
             ) {
            List<TimeTable> timeTablesFind =  timeTableRepository.findAllByStudyWeek(study_week);
            timeTableList.addAll(timeTablesFind);
        }
        return timeTableList;
    }


    public Set<String> findAllDayBySemester_Id(int semesterId) {
        Set<String> timeDay = new TreeSet<>();
        List<TimeTable> timeTableList = timeTableRepository.findAllBySemester_Id(semesterId);
        for (TimeTable timeTable: timeTableList
        ) {
            timeDay.add(timeTable.getStudyDay());
        }
        return  timeDay;
    }

    public List<TimeTable> findAllByWeekAndDay(Set<String> studyWeeks, Set<String> studyDays) {
        List<TimeTable> timeTableList = new ArrayList<>();
        List<TimeTable> timeTableWithWeek = findAllByWeek(studyWeeks);

        for (TimeTable timeTable : timeTableWithWeek) {
            if (studyDays.contains(timeTable.getStudyDay())) {
                timeTableList.add(timeTable);
            }
        }
        return timeTableList;
    }

    public TimeTable findById(int id) {
        return timeTableRepository.findById(id).orElse(null);
    }
}
