package ru.job4j.dreamjob.store;

import ru.job4j.dreamjob.model.Post;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class PostStore {

    private static final PostStore INST = new PostStore();
    private final Map<Integer, Post> posts = new ConcurrentHashMap<>();
    private final AtomicInteger id = new AtomicInteger(4);

    private PostStore() {
        posts.put(1,
                new Post(
                        1, "Junior Java Job",
                        "job description"));
        posts.put(2,
                new Post(
                        2, "Middle Java Job",
                        "job description"));
        posts.put(3,
                new Post(
                        3, "Senior Java Job",
                        "job description"));
    }

    public static PostStore instOf() {
        return INST;
    }

    public boolean add(Post post) {
        return posts.putIfAbsent(id.incrementAndGet(), post) != null;
    }

    public Collection<Post> findAll() {
        return posts.values();
    }
}
