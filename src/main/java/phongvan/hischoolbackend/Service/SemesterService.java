package phongvan.hischoolbackend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import phongvan.hischoolbackend.Repository.SemesterRepository;
import phongvan.hischoolbackend.entity.SchoolYear;
import phongvan.hischoolbackend.entity.Semester;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class SemesterService {
    @Autowired
    SemesterRepository semesterRepository;
    public Semester findById (Integer id){
        return semesterRepository.findById(id).get();
    }
    public Optional<Semester> aSemester(String name) {
        return semesterRepository.findByName(name);
    }

    public List<Semester> allSemesters() {
        return semesterRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    public Page<Semester> findPaginated(Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Semester> list;
        List<Semester> allSemesters = allSemesters();

        if (allSemesters.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, allSemesters.size());
            list = allSemesters.subList(startItem, toIndex);
        }

        PageRequest pageRequest = PageRequest.of(currentPage, pageSize, Sort.by(Sort.Direction.DESC, "id"));

        Page<Semester> semesterPage = new PageImpl<>(list, pageRequest, allSemesters.size());
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
}
