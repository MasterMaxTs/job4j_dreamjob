package ru.job4j.dreamjob.controllers;

import ru.job4j.dreamjob.model.User;

import javax.servlet.http.HttpSession;

public interface ManageSession {

    default User getUserFromSession(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            user = new User();
            user.setName("Гость");
        }
        return user;
    }
}
