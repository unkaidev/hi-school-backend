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
import phongvan.hischoolbackend.Payload.Request.TeacherRequest;
import phongvan.hischoolbackend.Payload.Response.MessageResponse;
import phongvan.hischoolbackend.Repository.*;
import phongvan.hischoolbackend.Service.TeacherService;
import phongvan.hischoolbackend.entity.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/teacher")
public class TeacherController {
    @Autowired
    TeacherService teacherService;
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
    SchoolClassRepository classRepository;
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

    @GetMapping("/allGroupName")
    public String[] getAllGroupName() {
        EGroup[] groupNames = EGroup.values();
        String[] names = new String[groupNames.length];
        for (int i = 0; i < groupNames.length; i++) {
            names[i] = groupNames[i].getName();
        }
        return names;
    }
    @GetMapping("/latest")
    public ResponseEntity<MessageResponse> getUserLatest(@RequestParam String username) {
        Teacher latestUser = null;
        try {
            latestUser = teacherService.findALatestTeacher(username);
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(0, "Get Data Success", latestUser));

        } catch (Exception e) {
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(-1, "Some Thing Went Wrong In Server", null));
        }

    }
    @GetMapping("{schoolId}/all")
    public ResponseEntity<MessageResponse> getAllTeachersInSchool(@PathVariable int schoolId) {

        List<Teacher> teacherList = null;
        try {
            teacherList = teacherService.findAllInSchool(schoolId);
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(0, "Get Data Success", teacherList));

        } catch (Exception e) {
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(-1, "Some Thing Went Wrong In Server", null));
        }

    }
    @GetMapping("{schoolId}/{yearId}/all")
    public ResponseEntity<MessageResponse> getAllTeachersReadyInSchoolAndYear(@PathVariable int schoolId,@PathVariable int yearId) {

        List<Teacher> teacherList = null;
        try {
            teacherList = teacherService.findAllTeachersReadyInSchoolAndYear(schoolId,yearId);
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(0, "Get Data Success", teacherList));

        } catch (Exception e) {
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(-1, "Some Thing Went Wrong In Server", null));
        }

    }
    @GetMapping("/get/{classId}")
    public ResponseEntity<MessageResponse> getATeacherWithClass(@PathVariable int classId) {
        try {
            SchoolClass schoolClass = classRepository.findById(classId).orElse(null);

            assert schoolClass != null;
            Teacher teacher = schoolClass.getTeacher();
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(0, "Get Data Success", teacher));

        } catch (Exception e) {
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(-1, "Some Thing Went Wrong In Server", null));
        }

    }

    @GetMapping("{schoolId}/all-by-group")
    public ResponseEntity<MessageResponse> getAllTeachersWithGroupInSchool(@PathVariable int schoolId, @RequestParam String group) {

        List<Teacher> teacherList = null;
        try {
            teacherList = teacherService.findAllInSchool_IdAndGroup(schoolId, group);
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(0, "Get Data Success", teacherList));

        } catch (Exception e) {
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(-1, "Some Thing Went Wrong In Server", null));
        }

    }

    @GetMapping("{schoolId}/all-by-group-ready")
    public ResponseEntity<MessageResponse> getAllTeachersWithGroupInSchoolReady(
            @PathVariable int schoolId,
            @RequestParam String group,
            @RequestParam List<Integer> selectedSchedules,
            @RequestParam List<String> selectedWeeks,
            @RequestParam List<String> selectedDays) {

        List<Teacher> teacherList = new ArrayList<>();
        try {
            List<Teacher> teacherInGroup = teacherService.findAllInSchool_IdAndGroup(schoolId, group);
            for (Teacher teacherIndex : teacherInGroup
            ) {
                System.out.println(teacherService.hasScheduledTimeTableForWeekAndDay(teacherIndex, selectedWeeks, selectedDays, selectedSchedules));
                System.out.println(group);
                if (!teacherService.hasScheduledTimeTableForWeekAndDay(teacherIndex, selectedWeeks, selectedDays, selectedSchedules)) {
                    teacherList.add(teacherIndex);
                }else if (group.equals("Ban giám hiệu")){
                    teacherList.add(teacherIndex);
                }
            }
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(0, "Get Data Success", teacherList));
        } catch (Exception e) {
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(-1, "Some Thing Went Wrong In Server", null));
        }
    }


    @GetMapping("/read")
    public ResponseEntity<MessageResponse> getSchoolWithPagination(@RequestParam int page, @RequestParam int limit, @RequestParam int schoolId) {

        Page<Teacher> teacherPage = null;
        try {
            teacherPage = teacherService.findPaginated(schoolId, PageRequest.of(page, limit, Sort.by(Sort.Direction.DESC, "id")));
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(0, "Get Data Success", teacherPage));

        } catch (Exception e) {
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(-1, "Some Thing Went Wrong In Server", null));
        }

    }

    @Transactional
    @PostMapping("/create")
    public ResponseEntity<?> addTeacher(@Valid @RequestBody TeacherRequest teacherRequest) {
        try {
            String newAvatar = teacherRequest.getAvatar();
            String newFirstName = teacherRequest.getFirstName().toUpperCase();
            String newLastName = teacherRequest.getLastName().toUpperCase();
            String newDateOfBirth = teacherRequest.getDateOfBirth();
            String newGender = teacherRequest.getGender();
            if (newGender.isBlank() || newGender.isEmpty()){
                newGender = "Nam";
            }
            String newNationality = teacherRequest.getNationality();
            String newEthnicity = teacherRequest.getEthnicity();
            String newCitizenId = teacherRequest.getCitizenId();
            String newIssuedPlaceName = teacherRequest.getIssuedPlace().getName();
            IssuedPlace newIssuedPlace = issuedPlaceRepository.findByName(newIssuedPlaceName);
            String newIssuedDate = teacherRequest.getIssuedDate();
            String newFirstWorkDate = teacherRequest.getFirstWorkDate();
            String newGroup = teacherRequest.getGroup();
            Address newPermanentAddress = teacherRequest.getPermanentAddress();
            addressRepository.save(newPermanentAddress);
            Address newContactAddress = teacherRequest.getContactAddress();
            addressRepository.save(newContactAddress);
            Integer schoolId = teacherRequest.getUser().getSchoolId();
            School newSchool = schoolRepository.getById(schoolId);

            String randomUsername = generateRandomUsername();
            String randomEmail = generateRandomEmail(randomUsername);

            Set<Role> roles = new HashSet<>();
            Optional<Role> role_Teacher = roleRepository.findByName(ERole.ROLE_TEACHER);
            roles.add(role_Teacher.get());

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

            Teacher newTeacher = Teacher.builder()
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
                    .user(newUser)
                    .firstWorkDate(newFirstWorkDate)
                    .group(newGroup)
                    .build();

            teacherService.updateTeacher(newTeacher);

            return ResponseEntity.ok(new MessageResponse(0, "Create New Teacher successfully!", null));

        } catch (Exception e) {
            e.printStackTrace();

            return ResponseEntity.ok(new MessageResponse(-1, "Error: Create New Teacher!", null));
        }

    }

    @Transactional
    @PostMapping("/delete/{id}")
    public ResponseEntity<MessageResponse> deleteTeacher(@PathVariable int id) {
        try {
            teacherService.deleteTeacher(id);
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(0, "Delete Teacher Success", null));
        } catch (Exception e) {
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(-1, "Some Thing Went Wrong In Server", null));
        }
    }

    @Transactional
    @PutMapping("/update")
    public ResponseEntity<?> updateTeacher(@RequestBody TeacherRequest teacherRequest) {
        try {
            Teacher teacherFind = teacherService.findById(teacherRequest.getId());
            String newGender = teacherRequest.getGender();
            if (newGender.isBlank() || newGender.isEmpty()){
                newGender = "Nam";
            }
            teacherFind.getUser().setGender(newGender);

            Address existPermanentAddress = teacherFind.getPermanentAddress();
            Address existContactAddress = teacherFind.getContactAddress();

            String newAvatar = teacherRequest.getAvatar();
            String newFirstName = teacherRequest.getFirstName().toUpperCase();
            String newLastName = teacherRequest.getLastName().toUpperCase();
            String newDateOfBirth = teacherRequest.getDateOfBirth();
            String newNationality = teacherRequest.getNationality();
            String newEthnicity = teacherRequest.getEthnicity();
            String newCitizenId = teacherRequest.getCitizenId();
            String newFirstWorkDate = teacherRequest.getFirstWorkDate();
            String newGroup = teacherRequest.getGroup();

            Integer newIssuedPlaceId = teacherRequest.getIssuedPlace().getId();
            IssuedPlace newIssuedPlace = issuedPlaceRepository.getById(newIssuedPlaceId);
            String newIssuedDate = teacherRequest.getIssuedDate();


            teacherFind.setAvatar(newAvatar);
            teacherFind.setFirstName(newFirstName);
            teacherFind.setLastName(newLastName);
            teacherFind.setDateOfBirth(newDateOfBirth);
            teacherFind.setNationality(newNationality);
            teacherFind.setEthnicity(newEthnicity);
            teacherFind.setCitizenId(newCitizenId);
            teacherFind.setIssuedPlace(newIssuedPlace);
            teacherFind.setIssuedDate(newIssuedDate);
            teacherFind.setFirstWorkDate(newFirstWorkDate);
            teacherFind.setGroup(newGroup);

            Address newPermanentAddress = teacherRequest.getPermanentAddress();
            if (newPermanentAddress != null && !newPermanentAddress.equals(existPermanentAddress)) {
                addressRepository.save(newPermanentAddress);
                teacherFind.setPermanentAddress(newPermanentAddress);
                addressRepository.delete(existPermanentAddress);
            }

            Address newContactAddress = teacherRequest.getContactAddress();
            if (newContactAddress != null && !newContactAddress.equals(existContactAddress)) {
                addressRepository.save(newContactAddress);
                teacherFind.setContactAddress(newContactAddress);
                addressRepository.delete(existContactAddress);
            }

            teacherService.updateTeacher(teacherFind);
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(0, "Update Teacher Success", null));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(-1, "Some Thing Went Wrong In Server", null));
        }
    }


}
