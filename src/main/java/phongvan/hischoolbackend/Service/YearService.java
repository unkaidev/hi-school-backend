package phongvan.hischoolbackend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import phongvan.hischoolbackend.Repository.YearRepository;
import phongvan.hischoolbackend.entity.SchoolYear;


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

    public Page<SchoolYear> findPaginated(Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<SchoolYear> list;
        List<SchoolYear> allYears = allYears();

        if (allYears.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, allYears.size());
            list = allYears.subList(startItem, toIndex);
        }

        PageRequest pageRequest = PageRequest.of(currentPage, pageSize, Sort.by(Sort.Direction.DESC, "id"));

        Page<SchoolYear> yearPage = new PageImpl<>(list, pageRequest, allYears.size());
        return yearPage;
    }

    public void deleteYear(int id) {
        yearRepository.deleteById(id);
    }

    public void updateYear(SchoolYear year) {
        yearRepository.save(year);
    }

    public boolean existsByName(String name) {
        return yearRepository.existsByName(name);
    }

}
