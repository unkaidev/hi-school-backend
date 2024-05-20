package phongvan.hischoolbackend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import phongvan.hischoolbackend.Repository.*;
import phongvan.hischoolbackend.entity.*;

import java.util.*;

@Service
public class TranscriptService {
    @Autowired
    TranscriptRepository transcriptRepository;

    @Autowired
    SchoolClassRepository schoolClassRepository;
    @Autowired
    SemesterRepository semesterRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    StudentRepository studentRepository;

    public Page<Transcript> findPaginated(int semesterId, int schoolClassId, Pageable pageable) {
        List<Transcript> transcripts = new ArrayList<>();
        SchoolClass schoolClass = schoolClassRepository.findById(schoolClassId).orElse(null);
        assert schoolClass != null;
        Collection<Student> students = schoolClass.getStudents();
        Semester semester = semesterRepository.findById(semesterId).orElse(null);
        if (semester != null) {
            SchoolYear schoolYear = semester.getSchoolYear();
            for (Student student : students
            ) {
                Transcript transcript = transcriptRepository.findByStudentAndSchoolYear(student,schoolYear);
                transcripts.add(transcript);
            }
        }

        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Transcript> list;

        if (transcripts.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, transcripts.size());
            list = transcripts.subList(startItem, toIndex);
        }

        PageRequest pageRequest = PageRequest.of(currentPage, pageSize, Sort.by(Sort.Direction.DESC, "id"));

        Page<Transcript> transcriptPage = new PageImpl<>(list, pageRequest, transcripts.size());
        return transcriptPage;
    }


    public Transcript findById(Integer id) {
        return transcriptRepository.findById(id).orElse(null);
    }

    public void save(Transcript transcript) {
        transcriptRepository.save(transcript);
    }

    public Page<Transcript> findPaginatedForStudent(int semesterId,String username, PageRequest pageable) {
        List<Transcript> transcripts = new ArrayList<>();
       User user = userRepository.findByUsername(username).orElse(null);
       if(user!=null) {
           Student student = studentRepository.findByUser(user);
           Semester semester = semesterRepository.findById(semesterId).orElse(null);
           if (semester != null) {
               SchoolYear schoolYear = semester.getSchoolYear();
               Transcript transcript = transcriptRepository.findByStudentAndSchoolYear(student, schoolYear);
               transcripts.add(transcript);
           }
       }
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Transcript> list;

        if (transcripts.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, transcripts.size());
            list = transcripts.subList(startItem, toIndex);
        }

        PageRequest pageRequest = PageRequest.of(currentPage, pageSize, Sort.by(Sort.Direction.DESC, "id"));

        Page<Transcript> transcriptPage = new PageImpl<>(list, pageRequest, transcripts.size());
        return transcriptPage;
    }

    public Page<Transcript> findPaginatedFromSemester(int semesterId, PageRequest pageable) {
        List<Transcript> transcripts = new ArrayList<>();
        Semester semester = semesterRepository.findById(semesterId).orElse(null);
        assert semester != null;
        List<SchoolClass> schoolClasses = schoolClassRepository.findAllBySchoolYear_Id(semester.getSchoolYear().getId(),Sort.by(Sort.Direction.DESC, "id"));
        Collection<Student> students = new HashSet<>();
        for (SchoolClass schoolClass:schoolClasses
             ) {
            students.addAll(schoolClass.getStudents());
        }

        if (semester != null) {
            SchoolYear schoolYear = semester.getSchoolYear();
            for (Student student : students
            ) {
                Transcript transcript = transcriptRepository.findByStudentAndSchoolYear(student,schoolYear);
                transcripts.add(transcript);
            }
        }

        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Transcript> list;

        if (transcripts.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, transcripts.size());
            list = transcripts.subList(startItem, toIndex);
        }

        PageRequest pageRequest = PageRequest.of(currentPage, pageSize, Sort.by(Sort.Direction.DESC, "id"));

        Page<Transcript> transcriptPage = new PageImpl<>(list, pageRequest, transcripts.size());
        return transcriptPage;
    }
}
