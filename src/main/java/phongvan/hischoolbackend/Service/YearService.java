package phongvan.hischoolbackend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import phongvan.hischoolbackend.Repository.YearRepository;
import phongvan.hischoolbackend.entity.ERole;
import phongvan.hischoolbackend.entity.SchoolYear;
import phongvan.hischoolbackend.entity.User;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class YearService {
    @Autowired
    YearRepository yearRepository;
    public SchoolYear findById (Integer id){
        return yearRepository.findById(id).get();
    }
    public Optional<SchoolYear> aYear(String name) {
        return yearRepository.findByName(name);
    }

    public List<SchoolYear> allYears() {
        return yearRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    public Page<SchoolYear> findPaginatedInSchool(Integer schoolId,Pageable pageable) {
        List<SchoolYear> yearsInSchool = yearRepository.findAllBySchool_Id(schoolId,Sort.by(Sort.Direction.DESC, "id"));

        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<SchoolYear> list;

        if (yearsInSchool.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, yearsInSchool.size());
            list = yearsInSchool.subList(startItem, toIndex);
        }

        PageRequest pageRequest = PageRequest.of(currentPage, pageSize, Sort.by(Sort.Direction.DESC, "id"));

        Page<SchoolYear> yearPage = new PageImpl<>(list, pageRequest, yearsInSchool.size());
        return yearPage;
    }

    public void deleteYear(int id) {
        yearRepository.deleteById(id);
    }

    public void updateYear(SchoolYear year) {
        yearRepository.save(year);
    }

    public boolean existsByNameAndSchoolId(String name, int id) {
        return yearRepository.existsByNameAndSchool_Id(name,id);
    }

    public List<SchoolYear> findAllInSchool(int schoolId) {
        return yearRepository.findAllBySchool_Id(schoolId, Sort.by(Sort.Direction.DESC,"id"));
    }
}
