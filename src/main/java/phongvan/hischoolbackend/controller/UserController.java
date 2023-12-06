package phongvan.hischoolbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import phongvan.hischoolbackend.entity.User;
import phongvan.hischoolbackend.repository.UserRepository;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/backend/user")
public class UserController {
    private final UserRepository repository;


    @GetMapping
    public String showUserPage(Model model) {
        model.addAttribute("users", repository.findAll());

        return "user";
    }

    @PostMapping("/create")
    public String addUser(@ModelAttribute User user){
        repository.save(user);
        return "redirect:/backend/user";
    }
    @GetMapping("/edit/{id}")
    public String editUser(@PathVariable int id, Model model){
        Optional<User> user = repository.findById(id);
        if (user.isPresent()){
            model.addAttribute("user",user);
            return "user-update";
        }
        return "user";
    }
    @PostMapping("/update")
    public String updateUser(@ModelAttribute User user){
        repository.saveAndFlush(user);
        return "redirect:/backend/user";
    }
    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable int id){
        repository.deleteById(id);
        return "redirect:/backend/user";
    }


}
