package phongvan.hischoolbackend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import phongvan.hischoolbackend.Repository.*;
import phongvan.hischoolbackend.entity.*;
import phongvan.hischoolbackend.utils.DateUtils;

import java.beans.Encoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    TeacherRepository teacherRepository;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    NotificationRepository notificationRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    SchoolRepository schoolRepository;

    public User anUser(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.orElse(null);
    }

    public List<User> allUsers() {
        return userRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    public Page<User> findPaginated(Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<User> list;
        List<User> allUsers = allUsers();

        if (allUsers.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, allUsers.size());
            list = allUsers.subList(startItem, toIndex);
        }

        PageRequest pageRequest = PageRequest.of(currentPage, pageSize, Sort.by(Sort.Direction.DESC, "id"));

        Page<User> userPage = new PageImpl<>(list, pageRequest, allUsers.size());
        return userPage;
    }

    public Page<User> findPaginatedUsersWithManagerRole(String username, Pageable pageable) {
        User user = userRepository.findByUsername(username).orElse(null);
        List<User> users = new ArrayList<>();
        Role role_admin = roleRepository.findByName(ERole.ROLE_ADMIN).orElse(null);

        if(user !=null){
            if(user.getRoles().contains(role_admin)){
                users = userRepository.findAllByRoles_Name(ERole.ROLE_MANAGER, Sort.by(Sort.Direction.DESC, "id"));
            }else {
                users = userRepository.findAllByRoles_NameAndSchool_Id(ERole.ROLE_MANAGER,user.getSchool().getId(), Sort.by(Sort.Direction.DESC, "id"));
            }
        }

        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<User> list;

        if (users.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, users.size());
            list = users.subList(startItem, toIndex);
        }

        PageRequest pageRequest = PageRequest.of(currentPage, pageSize, Sort.by(Sort.Direction.DESC, "id"));

        return new PageImpl<>(list, pageRequest, users.size());
    }

    public Page<User> findPaginatedUsersWithoutManagerAndAdminAndSchool(Integer schoolId, Pageable pageable) {
        List<User> usersWithoutManagerAndAdmin = userRepository.findAllByRoles_NameNotInAndSchoolId(Arrays.asList(ERole.ROLE_MANAGER, ERole.ROLE_ADMIN), schoolId, Sort.by(Sort.Direction.DESC, "id"));
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<User> list;

        if (usersWithoutManagerAndAdmin.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, usersWithoutManagerAndAdmin.size());
            list = usersWithoutManagerAndAdmin.subList(startItem, toIndex);
        }

        PageRequest pageRequest = PageRequest.of(currentPage, pageSize, Sort.by(Sort.Direction.DESC, "id"));

        return new PageImpl<>(list, pageRequest, usersWithoutManagerAndAdmin.size());
    }

    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }

    public void updateUser(User user) {
        userRepository.save(user);
    }

    public boolean existsByPhone(String newPhone) {
        return userRepository.existsByPhone(newPhone);
    }

    public String findAvatarByUsername(String username) {
        User user = userRepository.findByUsername(username).orElse(null);
        String avatar = null;
        if (user != null) {
            try {
                avatar = user.getStudent().getAvatar();
            } catch (Exception e) {
                avatar = user.getTeacher().getAvatar();
            }
        }
        return avatar;
    }

    public Object findUserByUsername(String username) {
        User user = userRepository.findByUsername(username).orElse(null);
        Object object = null;
        if (user != null) {
            object = user.getTeacher();
            if (object == null) {
                object = user.getStudent();
            }
        }
        return object;
    }

    public String changePassword(String username, String oldPassword, String newPassword, String reNewPassword) {
        User user = userRepository.findByUsername(username).orElse(null);
        if (user != null) {
            if (!encoder.matches(oldPassword, user.getPassword())) {
                return "Mật khẩu không chính xác!";
            }
            if (!newPassword.equals(reNewPassword)) {
                return "Mật khẩu mới và xác nhận mật khẩu mới không khớp!";
            }
            user.setPassword(encoder.encode(newPassword));
            userRepository.save(user);

            return "Thay đổi mật khẩu thành công!";
        } else {
            return "Người dùng không tồn tại!";
        }
    }


    public Integer countNumberNotificationsUnRead(String username) {
        User user = userRepository.findByUsername(username).orElse(null);
        if (user != null) {
            List<Notification> notifications = notificationRepository.findAllByReceiverAndIsRead(user, false);
            return notifications.size();
        }
        return 0;
    }

    public List<Notification> getAllNotifications(String username) {
        User user = userRepository.findByUsername(username).orElse(null);
        List<Notification> notifications = new ArrayList<>();
        if (user != null) {
            notifications = notificationRepository.findAllByReceiver(user);
        }
        return notifications;
    }

    public Page<Notification> findAllNotificationsPaginated(String username, Pageable pageable) {
        User user = userRepository.findByUsername(username).orElse(null);
        List<Notification> notifications = new ArrayList<>();
        if (user != null) {
            notifications = notificationRepository.findAllByReceiver(user);
        }

        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Notification> list;


        if (notifications.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, notifications.size());
            list = notifications.subList(startItem, toIndex);
        }

        PageRequest pageRequest = PageRequest.of(currentPage, pageSize, Sort.by(Sort.Direction.DESC, "id"));

        Page<Notification> page = new PageImpl<>(list, pageRequest, notifications.size());
        return page;
    }

    public Integer countNumberUsers(String username) {
        User user = userRepository.findByUsername(username).orElse(null);
        Role role_admin = roleRepository.findByName(ERole.ROLE_ADMIN).orElse(null);
        if (user != null) {
            if(user.getRoles().contains(role_admin)){
                List<User> users = userRepository.findAllByRoles_Name(ERole.ROLE_MANAGER,Sort.by(Sort.Direction.DESC, "id"));
                return users.size();
            }else {
                List<User> users = userRepository.findAllByRoles_NameAndSchool_Id(ERole.ROLE_MANAGER,user.getSchool().getId(),Sort.by(Sort.Direction.DESC, "id"));
                return users.size();
            }
        }
        return 0;
    }

    public Integer countNumberSchools(String username) {
        User user = userRepository.findByUsername(username).orElse(null);
        Role role_admin = roleRepository.findByName(ERole.ROLE_ADMIN).orElse(null);
        if (user != null) {
            if(user.getRoles().contains(role_admin)){
                List<School> schools = schoolRepository.findAll();
                return schools.size();
            }
        }
        return 0;
    }

    public Integer countNumberUsersToday(String username, String today) throws ParseException {
            User user = userRepository.findByUsername(username).orElse(null);
            Role role_admin = roleRepository.findByName(ERole.ROLE_ADMIN).orElse(null);

            Date date = DateUtils.parseDate(today);
            Calendar startOfDay = DateUtils.getStartOfDay(date);
            Calendar endOfDay = DateUtils.getEndOfDay(date);

            int number = 0;
            if (user != null) {
                List<User> users;
                if(user.getRoles().contains(role_admin)){
                    users = userRepository.findAllByRoles_NameAndCreatedAtBetween(ERole.ROLE_MANAGER, startOfDay.getTime(), endOfDay.getTime(), Sort.by(Sort.Direction.DESC, "id"));
                } else {
                    users = userRepository.findAllByRoles_NameAndSchool_IdAndCreatedAtBetween(ERole.ROLE_MANAGER,user.getSchool().getId(), startOfDay.getTime(), endOfDay.getTime());
                }
                number = users.size();
            }
            return number;

    }

    public Integer countNumberSchoolsToday(String username, String today) throws ParseException {
        User user = userRepository.findByUsername(username).orElse(null);
        Role role_admin = roleRepository.findByName(ERole.ROLE_ADMIN).orElse(null);
        Date date = DateUtils.parseDate(today);
        Calendar startOfDay = DateUtils.getStartOfDay(date);
        Calendar endOfDay = DateUtils.getEndOfDay(date);

        if (user != null) {
            if(user.getRoles().contains(role_admin)){
                List<School> schools = schoolRepository.findAllByCreatedAtBetween(startOfDay.getTime(), endOfDay.getTime());
                return schools.size();
            }
        }
        return 0;
    }

    public Integer countNumberAllUsers(String username) {
        User user = userRepository.findByUsername(username).orElse(null);
        Role role_admin = roleRepository.findByName(ERole.ROLE_ADMIN).orElse(null);
        if (user != null) {
            if(user.getRoles().contains(role_admin)){
                List<User> users = userRepository.findAll();
                return users.size();
            }else {
                List<User> users = userRepository.findAllBySchool_Id(user.getSchool().getId());
                return users.size();
            }
        }
        return 0;
    }

    public Integer countNumberAllUsersToday(String username, String today) throws ParseException {
        User user = userRepository.findByUsername(username).orElse(null);
        Role role_admin = roleRepository.findByName(ERole.ROLE_ADMIN).orElse(null);

        Date date = DateUtils.parseDate(today);
        Calendar startOfDay = DateUtils.getStartOfDay(date);
        Calendar endOfDay = DateUtils.getEndOfDay(date);

        if (user != null) {
            if(user.getRoles().contains(role_admin)){
                List<User> users = userRepository.findAllByCreatedAtBetween(startOfDay.getTime(), endOfDay.getTime());
                return users.size();
            }else {
                List<User> users = userRepository.findAllBySchool_IdAndCreatedAtBetween(user.getSchool().getId(), startOfDay.getTime(), endOfDay.getTime());
                return users.size();
            }
        }
        return 0;
    }

    public List<Object[]> countUsersByMonth(int year) {
        return userRepository.countUsersByMonth(year);
    }


}
