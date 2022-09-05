package ru.job4j.dreamjob.store;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.City;
import ru.job4j.dreamjob.model.Post;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
@ThreadSafe
public class PostStore implements Store<Post> {

    private final Map<Integer, Post> posts = new ConcurrentHashMap<>();
    private final AtomicInteger id = new AtomicInteger(3);

    private PostStore() {
        posts.put(1,
                new Post("Junior Java Job",
                        "job description",
                        Timestamp.valueOf(LocalDateTime.now().withNano(0)),
                        new City(1, ""),
                        true
                )
        );
        posts.put(2,
                new Post("Middle Java Job",
                        "job description",
                        Timestamp.valueOf(LocalDateTime.now().withNano(0)),
                        new City(1, ""),
                        true
                )
        );
        posts.put(3,
                new Post("Senior Java Job",
                        "job description",
                        Timestamp.valueOf(LocalDateTime.now().withNano(0)),
                        new City(1, ""),
                        true
                )
        );
    }

    @Override
    public Post add(Post post) {
        post.setId(
                id.incrementAndGet()
        );
        post.setCreated(
                new Timestamp(new Date().getTime())
        );
        return posts.putIfAbsent(post.getId(), post);
    }

    @Override
    public List<Post> findAll() {
        return new ArrayList<>(posts.values());
    }

    @Override
    public Post findById(int id) {
        return posts.get(id);
    }

    @Override
    public void update(Post post) {
        post.setCreated(
                new Timestamp(new Date().getTime())
        );
        posts.replace(post.getId(), post);
    }
}
