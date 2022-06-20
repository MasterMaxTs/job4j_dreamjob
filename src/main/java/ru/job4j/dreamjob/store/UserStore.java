package ru.job4j.dreamjob.store;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
@ThreadSafe
public class UserStore {

    private final Map<Integer, User> users = new ConcurrentHashMap<>();
    private final AtomicInteger id = new AtomicInteger();

    public UserStore() {

    }

    public boolean add(User user) {
        user.setId(id.incrementAndGet());
        return users.putIfAbsent(user.getId(), user) != null;
    }

    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    public User findById(int id) {
        return users.get(id);
    }

    public void update(User user) {
        users.replace(user.getId(), user);
    }
}
