package phongvan.hischoolbackend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import phongvan.hischoolbackend.Repository.SchoolRepository;
import phongvan.hischoolbackend.Repository.SemesterRepository;
import phongvan.hischoolbackend.Repository.YearRepository;
import phongvan.hischoolbackend.entity.School;
import phongvan.hischoolbackend.entity.SchoolYear;
import phongvan.hischoolbackend.entity.Semester;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class SemesterService {
    @Autowired
    SemesterRepository semesterRepository;
    @Autowired
    YearRepository yearRepository;
    @Autowired
    SchoolRepository schoolRepository;

    public Semester findById (Integer id){
        return semesterRepository.findById(id).get();
    }
    public Optional<Semester> aSemester(String name) {
        return semesterRepository.findByName(name);
    }

    public List<Semester> allSemesters() {
        return semesterRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    public Page<Semester> findPaginated(Integer schoolId,Pageable pageable) {

        List<SchoolYear> yearList = yearRepository.findAllBySchool_Id(schoolId,Sort.by(Sort.Direction.DESC, "id"));
        List<Semester> semesterList = new ArrayList<>();

        for (SchoolYear schoolYear: yearList
             ) {
            semesterList.addAll(semesterRepository.findAllBySchoolYear(schoolYear,Sort.by(Sort.Direction.DESC, "id"))) ;
        }

        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Semester> list;

        if (semesterList.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, semesterList.size());
            list = semesterList.subList(startItem, toIndex);
        }

        PageRequest pageRequest = PageRequest.of(currentPage, pageSize, Sort.by(Sort.Direction.DESC, "id"));

        Page<Semester> semesterPage = new PageImpl<>(list, pageRequest, semesterList.size());
        return semesterPage;
    }

    public void deleteSemester(int id) {
        semesterRepository.deleteById(id);
    }

    public void updateSemester(Semester semester) {
        semesterRepository.save(semester);
    }

    public boolean existsByName(String name) {
        return semesterRepository.existsByName(name);
    }

    public List<Semester> findAllByName(String semesterName) {
        return semesterRepository.findAllByName(semesterName);
    }

    public boolean existsByNameAndSchoolYearId(String semesterName, Integer id) {
        return semesterRepository.existsByNameAndSchoolYear_Id(semesterName,id);
    }

    public List<Semester> findAllBySchoolYear(int yearId) {
        try {
            return semesterRepository.findAllBySchoolYear_Id(yearId,Sort.by(Sort.Direction.DESC, "id"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Semester> findAllBySchoolId(int schoolId) {
        School school = schoolRepository.findById(schoolId).orElse(null);
        List<Semester> semesterList = new ArrayList<>();
        if(school!=null){
            List<SchoolYear> schoolYears = school.getSchoolYears();
            for (SchoolYear schoolYear: schoolYears
                 ) {
                semesterList.addAll(semesterRepository.findAllBySchoolYear(schoolYear,Sort.by(Sort.Direction.DESC, "id")));
            }
        }
        return semesterList;
    }
}
