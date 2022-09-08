package ru.job4j.dreamjob.store.userstore;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

@Repository
@ThreadSafe
public class UserStoreImpl implements UserStore {

    private final Map<Integer, User> users = new ConcurrentHashMap<>();
    private final AtomicInteger id = new AtomicInteger();

    @Override
    public Optional<User> add(User user) {
        user.setId(id.incrementAndGet());
        return Optional.ofNullable(users.putIfAbsent(user.getId(), user));
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User findById(int id) {
        return users.get(id);
    }

    @Override
    public Optional<User> findUserByEmailAndPwd(String email, String password) {
        return users.values()
                .stream()
                .filter(u -> u.getEmail().equals(email) && u.getPassword().equals(password))
                .findFirst();
    }

    @Override
    public void update(User user) {
        users.replace(user.getId(), user);
    }
}
