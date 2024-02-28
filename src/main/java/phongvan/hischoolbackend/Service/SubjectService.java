package phongvan.hischoolbackend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import phongvan.hischoolbackend.Repository.SubjectRepository;
import phongvan.hischoolbackend.entity.Subject;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class SubjectService {
    @Autowired
    SubjectRepository subjectRepository;
    public Subject findById (Integer id){
        return subjectRepository.findById(id).get();
    }
    public Optional<Subject> aSubject(String name) {
        return subjectRepository.findByName(name);
    }

    public List<Subject> allSubjects() {
        return subjectRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    public Page<Subject> findPaginated(Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Subject> list;
        List<Subject> allSubjects = allSubjects();

        if (allSubjects.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, allSubjects.size());
            list = allSubjects.subList(startItem, toIndex);
        }

        PageRequest pageRequest = PageRequest.of(currentPage, pageSize, Sort.by(Sort.Direction.DESC, "id"));

        Page<Subject> subjectPage = new PageImpl<>(list, pageRequest, allSubjects.size());
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
}
