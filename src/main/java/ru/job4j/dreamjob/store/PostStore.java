package ru.job4j.dreamjob.store;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.City;
import ru.job4j.dreamjob.model.Post;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
@ThreadSafe
public class PostStore {

    private final Map<Integer, Post> posts = new ConcurrentHashMap<>();
    private final AtomicInteger id = new AtomicInteger(3);

    private PostStore() {
        posts.put(1,
                new Post(1,
                        "Junior Java Job",
                        "job description",
                        new Timestamp(new Date().getTime()),
                        new City(1, ""),
                true)
        );
        posts.put(2,
                new Post(2,
                        "Middle Java Job",
                        "job description",
                        new Timestamp(new Date().getTime()),
                        new City(1, ""),
                        true
                )

        );
        posts.put(3,
                new Post(3,
                        "Senior Java Job",
                        "job description",
                        new Timestamp(new Date().getTime()),
                        new City(1, ""),
                        true
                )
        );
    }

    public boolean add(Post post) {
        post.setId(
                id.incrementAndGet()
        );
        post.setCreated(
                new Timestamp(new Date().getTime())
        );
        return posts.putIfAbsent(post.getId(), post) != null;
    }

    public Collection<Post> findAll() {
        return posts.values();
    }

    public Post findById(int id) {
        return posts.get(id);
    }

    public void update(Post post) {
        post.setCreated(
                new Timestamp(new Date().getTime())
        );
        posts.replace(post.getId(), post);
    }
}
