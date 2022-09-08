package ru.job4j.dreamjob.service.userservice;

import ru.job4j.dreamjob.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> findAll();

    Optional<User> add(User user);

    void update(User user);

    User findById(int id);

    Optional<User> findUserByEmailAndPwd(String email, String password);
}
