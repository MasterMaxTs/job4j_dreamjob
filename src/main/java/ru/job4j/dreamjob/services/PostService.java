package ru.job4j.dreamjob.services;

import ru.job4j.dreamjob.model.Post;
import ru.job4j.dreamjob.store.PostStore;

import java.util.ArrayList;
import java.util.List;

public class PostService {

    private final static PostService INST = new PostService();
    private final PostStore store = PostStore.instOf();

    private PostService() {

    }

    public static PostService instOf() {
        return INST;
    }

    public List<Post> findAll() {
        return new ArrayList<>(store.findAll());
    }

    public void add(Post post) {
        store.add(post);
    }

    public Post findById(int id) {
        return store.findById(id);
    }

    public void update(Post post) {
        store.update(post);
    }
}
