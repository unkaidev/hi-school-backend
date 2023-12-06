package phongvan.hischoolbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/backend")
@RequiredArgsConstructor
public class HomeController {

    @GetMapping
    public String showPublicPage(Model model){
        return "home";
    }
}
