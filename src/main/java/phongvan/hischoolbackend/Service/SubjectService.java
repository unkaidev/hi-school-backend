package phongvan.hischoolbackend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import phongvan.hischoolbackend.Repository.SemesterRepository;
import phongvan.hischoolbackend.Repository.SubjectRepository;
import phongvan.hischoolbackend.Repository.YearRepository;
import phongvan.hischoolbackend.entity.SchoolYear;
import phongvan.hischoolbackend.entity.Semester;
import phongvan.hischoolbackend.entity.Subject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class SubjectService {
    @Autowired
    SubjectRepository subjectRepository;
    @Autowired
    YearRepository yearRepository;
    @Autowired
    SemesterRepository semesterRepository;


    public Subject findById(Integer id) {
        return subjectRepository.findById(id).get();
    }

    public Optional<Subject> aSubject(String name) {
        return subjectRepository.findByName(name);
    }

    public List<Subject> allSubjects() {
        return subjectRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    public Page<Subject> findPaginated(Integer schoolId, Pageable pageable) {

        List<SchoolYear> yearList = yearRepository.findAllBySchool_Id(schoolId, Sort.by(Sort.Direction.DESC, "id"));
        List<Semester> semesterList = new ArrayList<>();

        for (SchoolYear schoolYear : yearList
        ) {
            semesterList.addAll(semesterRepository.findAllBySchoolYear(schoolYear, Sort.by(Sort.Direction.DESC, "id")));
        }
        List<Subject> subjectList = new ArrayList<>();

        for (Semester semester : semesterList
        ) {
            subjectList.addAll(subjectRepository.findAllBySemester(semester, Sort.by(Sort.Direction.DESC, "id")));
        }
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Subject> list;

        if (subjectList.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, subjectList.size());
            list = subjectList.subList(startItem, toIndex);
        }

        PageRequest pageRequest = PageRequest.of(currentPage, pageSize, Sort.by(Sort.Direction.DESC, "id"));

        Page<Subject> subjectPage = new PageImpl<>(list, pageRequest, subjectList.size());
        return subjectPage;
    }

    public void deleteSubject(int id) {
        subjectRepository.deleteById(id);
    }

    public void updateSubject(Subject subject) {
        subjectRepository.save(subject);
    }

    public boolean existsByName(String name) {
        return subjectRepository.existsByName(name);
    }

    public List<Subject> findAllByName(String subjectName) {
        return subjectRepository.findAllByName(subjectName);
    }

    public List<Subject> findAllBySemester(int semesterId) {
        return subjectRepository.findAllBySemester_Id(semesterId,Sort.by(Sort.Direction.DESC, "id"));
    }

    public List<Subject> findAllBySemester_IdAndGrade(int semesterId, String grade) {
        return subjectRepository.findAllBySemester_IdAndGrade(semesterId,grade,Sort.by(Sort.Direction.DESC, "id"));
    }
}
