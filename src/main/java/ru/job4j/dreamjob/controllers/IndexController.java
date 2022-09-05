package ru.job4j.dreamjob.controllers;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import ru.job4j.dreamjob.model.User;

import javax.servlet.http.HttpSession;

@Controller
@ThreadSafe
public class IndexController implements ManageSession {

    @ModelAttribute("user")
    public User getUser(HttpSession session) {
        return getUserFromSession(session);
    }

    @GetMapping("/index")
    public String index() {
        return "index";
    }
}
