package ru.job4j.dreamjob.service.postservice;

import ru.job4j.dreamjob.model.Post;

import java.util.List;

public interface PostService {

    Post add(Post post);

    List<Post> findAll();

    void update(Post post);

    Post findById(int id);

    void delete(int id);
}
