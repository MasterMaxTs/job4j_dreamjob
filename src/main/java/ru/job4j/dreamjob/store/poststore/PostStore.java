package ru.job4j.dreamjob.store.poststore;

import ru.job4j.dreamjob.model.Post;

import java.util.List;

public interface PostStore {

    Post add(Post post);

    List<Post> findAll();

    void update(Post post);

    Post findById(int id);

    void delete(int id);
}
