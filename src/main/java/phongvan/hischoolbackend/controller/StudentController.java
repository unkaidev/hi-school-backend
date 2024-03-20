package phongvan.hischoolbackend.Controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import phongvan.hischoolbackend.Payload.Request.StudentRequest;
import phongvan.hischoolbackend.Payload.Response.MessageResponse;
import phongvan.hischoolbackend.Repository.*;
import phongvan.hischoolbackend.Service.StudentService;
import phongvan.hischoolbackend.entity.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/student")
public class StudentController {
    @Autowired
    StudentService studentService;
    @Autowired
    AddressRepository addressRepository;
    @Autowired
    IssuedPlaceRepository issuedPlaceRepository;
    @Autowired
    ParentRepository parentRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    SchoolRepository schoolRepository;
    @Autowired
    PasswordEncoder encoder;

    public static String generateRandomUsername() {
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 7; i++) {
            int randomNumber = random.nextInt(10);
            stringBuilder.append(randomNumber);
        }
        return stringBuilder.toString();
    }

    public static String generateRandomEmail(String username) {
        return username + "@hischool.com";
    }

    @GetMapping("/read")
    public ResponseEntity<MessageResponse> getStudentWithPagination(@RequestParam int page, @RequestParam int limit, @RequestParam int schoolId) {

        Page<Student> studentPage = null;
        try {
            studentPage = studentService.findPaginated(schoolId, PageRequest.of(page, limit, Sort.by(Sort.Direction.DESC, "id")));
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(0, "Get Data Success", studentPage));

        } catch (Exception e) {
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(-1, "Some Thing Went Wrong In Server", null));
        }

    }

    @GetMapping("/latest")
    public ResponseEntity<MessageResponse> getUserLatest(@RequestParam String username) {
        Student latestUser = null;
        try {
            latestUser = studentService.findALatestStudent(username);
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(0, "Get Data Success", latestUser));

        } catch (Exception e) {
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(-1, "Some Thing Went Wrong In Server", null));
        }

    }

    @GetMapping("/view")
    public ResponseEntity<MessageResponse> getStudentInClassWithPagination(@RequestParam int page, @RequestParam int limit, @RequestParam int schoolClassId) {

        Page<Student> studentPage = null;
        try {
            studentPage = studentService.findPaginatedInClass(schoolClassId, PageRequest.of(page, limit, Sort.by(Sort.Direction.DESC, "id")));
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(0, "Get Data Success", studentPage));

        } catch (Exception e) {
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(-1, "Some Thing Went Wrong In Server", null));
        }

    }

    @GetMapping("/add")
    public ResponseEntity<MessageResponse> getStudentNotInClassWithPagination(@RequestParam int page, @RequestParam int limit, @RequestParam int schoolClassId) {

        Page<Student> studentPage = null;
        try {
            studentPage = studentService.findPaginatedNotInClass(schoolClassId, PageRequest.of(page, limit, Sort.by(Sort.Direction.DESC, "id")));
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(0, "Get Data Success", studentPage));

        } catch (Exception e) {
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(-1, "Some Thing Went Wrong In Server", null));
        }

    }

    @Transactional
    @PostMapping("/create")
    public ResponseEntity<?> addStudent(@Valid @RequestBody StudentRequest studentRequest) {
        try {
            String newAvatar = studentRequest.getAvatar();
            String newFirstName = studentRequest.getFirstName().toUpperCase();
            String newLastName = studentRequest.getLastName().toUpperCase();
            String newDateOfBirth = studentRequest.getDateOfBirth();
            String newGender = studentRequest.getGender();
            if (newGender.isBlank() || newGender.isEmpty()) {
                newGender = "Nam";
            }
            String newNationality = studentRequest.getNationality();
            String newEthnicity = studentRequest.getEthnicity();
            String newCitizenId = studentRequest.getCitizenId();
            String newIssuedPlaceName = studentRequest.getIssuedPlace().getName();
            IssuedPlace newIssuedPlace = issuedPlaceRepository.findByName(newIssuedPlaceName);
            String newIssuedDate = studentRequest.getIssuedDate();
            Address newPermanentAddress = studentRequest.getPermanentAddress();
            addressRepository.save(newPermanentAddress);
            Address newContactAddress = studentRequest.getContactAddress();
            addressRepository.save(newContactAddress);
            String newParentFirstName = studentRequest.getParent().getFirstName().toUpperCase();
            String newParentLastName = studentRequest.getParent().getLastName().toUpperCase();
            Integer schoolId = studentRequest.getUser().getSchool().getId();
            School newSchool = schoolRepository.getById(schoolId);

            Parent newParent = Parent.builder()
                    .firstName(newParentFirstName)
                    .lastName(newParentLastName)
                    .build();
            parentRepository.save(newParent);

            String randomUsername = generateRandomUsername();
            String randomEmail = generateRandomEmail(randomUsername);

            Set<Role> roles = new HashSet<>();
            Optional<Role> role_User = roleRepository.findByName(ERole.ROLE_USER);
            roles.add(role_User.get());

            User newUser = User.builder()
                    .username(randomUsername)
                    .email(randomEmail)
                    .phone(null)
                    .gender(newGender)
                    .password(encoder.encode("12345678"))
                    .roles(roles)
                    .school(newSchool)
                    .build();
            userRepository.save(newUser);

            Student newStudent = Student.builder()
                    .avatar(newAvatar)
                    .firstName(newFirstName)
                    .lastName(newLastName)
                    .dateOfBirth(newDateOfBirth)
                    .nationality(newNationality)
                    .ethnicity(newEthnicity)
                    .citizenId(newCitizenId)
                    .issuedPlace(newIssuedPlace)
                    .issuedDate(newIssuedDate)
                    .permanentAddress(newPermanentAddress)
                    .contactAddress(newContactAddress)
                    .parent(newParent)
                    .user(newUser)
                    .build();

            studentService.updateStudent(newStudent);

            return ResponseEntity.ok(new MessageResponse(0, "Create New Student successfully!", null));

        } catch (Exception e) {
            return ResponseEntity.ok(new MessageResponse(-1, "Error: Create New Student!", null));
        }

    }

    @Transactional
    @PostMapping("/delete/{id}")
    public ResponseEntity<MessageResponse> deleteStudent(@PathVariable int id) {
        try {
            studentService.deleteStudent(id);
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(0, "Delete Student Success", null));
        } catch (Exception e) {
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(-1, "Some Thing Went Wrong In Server", null));
        }
    }

    @Transactional
    @PostMapping("{schoolClassId}/remove/{studentId}")
    public ResponseEntity<MessageResponse> removeStudentFromClass(@PathVariable int schoolClassId, @PathVariable int studentId) {
        try {
            studentService.removeStudentFromClass(schoolClassId, studentId);
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(0, "Remove Student And Remove Transcript Success", null));
        } catch (Exception e) {
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(-1, "Some Thing Went Wrong In Server", null));
        }
    }

    @Transactional
    @PostMapping("{schoolClassId}/add/{studentId}")
    public ResponseEntity<MessageResponse> addStudentFromClass(@PathVariable int schoolClassId, @PathVariable int studentId) {
        try {
            studentService.addStudentFromClass(schoolClassId, studentId);
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(0, "Add Student And Add Transcript Success", null));
        } catch (Exception e) {
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(-1, "Some Thing Went Wrong In Server", null));
        }
    }

    @Transactional
    @PostMapping("{schoolClassId}/add-many/{students}")
    public ResponseEntity<MessageResponse> addStudentsFromClass(@PathVariable int schoolClassId, @PathVariable List<Integer> students) {
        try {
            studentService.addManyStudentsFromClass(schoolClassId, students);
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(0, "Add Many Student And Add Many Transcript Success", null));
        } catch (Exception e) {
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(-1, "Some Thing Went Wrong In Server", null));
        }
    }
    @Transactional
    @PostMapping("{schoolClassId}/remove-many/{students}")
    public ResponseEntity<MessageResponse> removeStudentsFromClass(@PathVariable int schoolClassId, @PathVariable List<Integer> students) {
        try {
            studentService.removeManyStudentsFromClass(schoolClassId, students);
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(0, "Remove Many Student And Add Many Transcript Success", null));
        } catch (Exception e) {
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(-1, "Some Thing Went Wrong In Server", null));
        }
    }

    @Transactional
    @PutMapping("/update")
    public ResponseEntity<?> updateStudent(@RequestBody StudentRequest studentRequest) {
        try {
            Student studentFind = studentService.findById(studentRequest.getId());
            String newGender = studentRequest.getGender();
            if (newGender.isBlank() || newGender.isEmpty()) {
                newGender = "Nam";
            }
            studentFind.getUser().setGender(newGender);

            Parent parentFind = parentRepository.getById(studentFind.getParent().getId());
            String newParentFirstName = studentRequest.getParent().getFirstName().toUpperCase();
            String newParentLastName = studentRequest.getParent().getLastName().toUpperCase();
            parentFind.setFirstName(newParentFirstName);
            parentFind.setLastName(newParentLastName);
            studentFind.setParent(parentFind);

            Address existPermanentAddress = studentFind.getPermanentAddress();
            Address existContactAddress = studentFind.getContactAddress();

            String newAvatar = studentRequest.getAvatar();
            String newFirstName = studentRequest.getFirstName().toUpperCase();
            String newLastName = studentRequest.getLastName().toUpperCase();
            String newDateOfBirth = studentRequest.getDateOfBirth();
            String newNationality = studentRequest.getNationality();
            String newEthnicity = studentRequest.getEthnicity();
            String newCitizenId = studentRequest.getCitizenId();

            Integer newIssuedPlaceId = studentRequest.getIssuedPlace().getId();
            IssuedPlace newIssuedPlace = issuedPlaceRepository.getById(newIssuedPlaceId);
            String newIssuedDate = studentRequest.getIssuedDate();


            studentFind.setAvatar(newAvatar);
            studentFind.setFirstName(newFirstName);
            studentFind.setLastName(newLastName);
            studentFind.setDateOfBirth(newDateOfBirth);
            studentFind.setNationality(newNationality);
            studentFind.setEthnicity(newEthnicity);
            studentFind.setCitizenId(newCitizenId);
            studentFind.setIssuedPlace(newIssuedPlace);
            studentFind.setIssuedDate(newIssuedDate);

            Address newPermanentAddress = studentRequest.getPermanentAddress();

            if (newPermanentAddress != null && !newPermanentAddress.equals(existPermanentAddress)) {
                addressRepository.save(newPermanentAddress);
                studentFind.setPermanentAddress(newPermanentAddress);
                addressRepository.delete(existPermanentAddress);
            }

            Address newContactAddress = studentRequest.getContactAddress();
            if (newContactAddress != null && !newContactAddress.equals(existContactAddress)) {
                addressRepository.save(newContactAddress);
                studentFind.setContactAddress(newContactAddress);
                addressRepository.delete(existContactAddress);
            }

            studentService.updateStudent(studentFind);
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(0, "Update Student Success", null));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(-1, "Some Thing Went Wrong In Server", null));
        }
    }


}
