package phongvan.hischoolbackend.rest;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import phongvan.hischoolbackend.entity.User;
import phongvan.hischoolbackend.repository.GroupRepository;
import phongvan.hischoolbackend.repository.UserRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserApiService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final GroupRepository groupRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll((Sort.by(Sort.Direction.DESC, "id")));
    }

    public Response getUserWithPagination(@RequestParam int page, @RequestParam int limit) {
        List<User> users = null;
        try {
            PageRequest pageRequest = PageRequest.of(page, limit,Sort.by(Sort.Direction.DESC, "id"));
            Page<User> userPage = userRepository.findAll(pageRequest);
            users = userPage.getContent();

            int totalUser = userRepository.findAll().size();
            if (users.isEmpty()) {
                return Response.builder()
                        .EC(-1)
                        .EM("NOT FOUND ANY USER")
                        .DT(null)
                        .build();
            } else {
                int totalPages = (int) Math.ceil(totalUser / (double) limit);
                Map<String, Object> data = new HashMap<>();
                data.put("totalRows", users.size());
                data.put("totalPages", totalPages);
                data.put("users", users);

                return Response.builder()
                        .EC(0)
                        .EM("GET DATA SUCCESS")
                        .DT(data)
                        .build();
            }
        } catch (Exception e) {
            return Response.builder()
                    .EC(-1)
                    .EM("SOMETHING WENT WRONG IN SERVER")
                    .DT(null)
                    .build();
        }
    }

    public Response createUser(@RequestBody UserRequest userRequest) {
        String email = userRequest.getEmail();
        String phone = userRequest.getPhone();
        String firstname = userRequest.getFirstname();
        String lastname = userRequest.getLastname();
        String password = userRequest.getPassword();
        String address = userRequest.getAddress();
        String gender = userRequest.getGender();
        String groupId = userRequest.getGroupId();
        try {
            if (userRepository.findByEmail(email).isPresent()) {
                return Response.builder()
                        .EC(-1)
                        .EM("THE EMAIL IS ALREADY EXIST")
                        .DT("email")
                        .build();
            }
            if (userRepository.findByPhone(phone).isPresent()) {
                return Response.builder()
                        .EC(-1)
                        .EM("THE PHONE NUMBER IS ALREADY EXIST")
                        .DT("phone")
                        .build();
            } else {
                var user = User.builder()
                        .email(email)
                        .phone(phone)
                        .password(passwordEncoder.encode(password))
                        .gender(gender)
                        .group(groupRepository.getById(Integer.valueOf(groupId)))
                        .build();
                userRepository.save(user);
                return Response.builder()
                        .EC(0)
                        .EM("CREATE USER SUCCESS")
                        .DT(null)
                        .build();
            }

        } catch (Exception e) {
            return Response.builder()
                    .EC(-1)
                    .EM("SOMETHING WENT WRONG IN SERVER")
                    .DT(null)
                    .build();
        }
    }

    public Response updateUser(@RequestBody UserRequest userRequest) {
        Integer id = userRequest.getId();
        String gender = userRequest.getGender();
        String groupId = userRequest.getGroupId();
        try {
            User user = userRepository.getById(id);
            user.setGender(gender);
            user.setGroup(groupRepository.getById(Integer.valueOf(groupId)));
            userRepository.save(user);
            return Response.builder()
                    .EC(0)
                    .EM("UPDATE USER SUCCESS")
                    .DT(null)
                    .build();
        } catch (Exception e) {
            return Response.builder()
                    .EC(-1)
                    .EM("SOMETHING WENT WRONG IN SERVER")
                    .DT(null)
                    .build();
        }
    }

    public Response deleteUser(@PathVariable int id){
        try {
            userRepository.deleteById(id);
            return Response.builder()
                    .EC(0)
                    .EM("DELETE USER SUCCESS")
                    .DT(null)
                    .build();
        } catch (Exception e) {
            return Response.builder()
                    .EC(-1)
                    .EM("SOMETHING WENT WRONG IN SERVER")
                    .DT(null)
                    .build();
        }


    }

}

