package ru.job4j.dreamjob.store.userstore;

import ru.job4j.dreamjob.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStore {
    Optional<User> add(User user);

    List<User> findAll();

    void update(User user);

    User findById(int id);

    Optional<User> findUserByEmailAndPwd(String email, String password);

}
