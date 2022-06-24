package ru.job4j.dreamjob.controllers;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
@ThreadSafe
public class IndexController implements ManageSession {

    @GetMapping("/index")
    public String index(Model model, HttpSession session) {
        addUserInModelFromSession(model, session);
        return "index";
    }
}
