package phongvan.hischoolbackend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import phongvan.hischoolbackend.Repository.SchoolRepository;
import phongvan.hischoolbackend.entity.School;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class SchoolService {
    @Autowired
    SchoolRepository schoolRepository;

    public Optional<School> aSchool(String name) {
        return schoolRepository.findByName(name);
    }

    public List<School> allSchools() {
        return schoolRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    public Page<School> findPaginated(Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<School> list;
        List<School> allSchools = allSchools();

        if (allSchools.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, allSchools.size());
            list = allSchools.subList(startItem, toIndex);
        }

        PageRequest pageRequest = PageRequest.of(currentPage, pageSize, Sort.by(Sort.Direction.DESC, "id"));

        Page<School> schoolPage = new PageImpl<>(list, pageRequest, allSchools.size());
        return schoolPage;
    }

    public void deleteSchool(int id) {
        schoolRepository.deleteById(id);
    }

    public void updateSchool(School school) {
        schoolRepository.save(school);
    }

    public boolean existsByName(String name) {
        return schoolRepository.existsByName(name);
    }

    public School findById(Integer id) {
        return schoolRepository.findById(id).get();
    }

    public List<String> findAllSchool_Id() {
        List<String> school_ids = new ArrayList<>();
        try {
            List<School> schoolList = allSchools();
            if (schoolList != null) {
                for (School schoolIndex : schoolList) {
                    school_ids.add(schoolIndex.getId().toString());
                }
            }
            return school_ids;
        } catch (Exception e) {
            return null;
        }
    }

}
