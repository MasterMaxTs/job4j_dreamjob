package ru.job4j.dreamjob.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.job4j.dreamjob.model.User;
import ru.job4j.dreamjob.services.UserService;

import java.util.Optional;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/addUser")
    public String add() {
        return "user/addUser";
    }

    @PostMapping("/registration")
    public String registration(RedirectAttributes redirectAttributes,
                               @ModelAttribute User user) {
        Optional<User> regUser = userService.add(user);
        if (regUser.isEmpty()) {
            redirectAttributes.addAttribute("attr",
                    "Пользователь с почтой " + user.getEmail() + " уже существует!");
            return "redirect:/fail";
        }
        redirectAttributes.addAttribute("attr", user.getName());
        return "redirect:/success";
    }

    @GetMapping("/success")
    public String informToSuccess(@RequestParam("attr") String userName,
                                  Model model) {
        model.addAttribute("name", userName);
        return "user/registration_success";
    }

    @GetMapping("/fail")
    public String informToFail(@RequestParam("attr") String msg,
                               Model model) {
        model.addAttribute("msg", msg);
        return "user/registration_fail";
    }
}
