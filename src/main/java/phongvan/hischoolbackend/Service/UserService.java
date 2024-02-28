package phongvan.hischoolbackend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import phongvan.hischoolbackend.Repository.UserRepository;
import phongvan.hischoolbackend.entity.User;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    public Optional<User> anUser(String username) {
        return userRepository.findByUsername(username);
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

    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }

    public void updateUser(User user) {
        userRepository.save(user);
    }
}
