package ru.job4j.dreamjob.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.job4j.dreamjob.model.Post;
import ru.job4j.dreamjob.store.PostStore;

import java.sql.Timestamp;
import java.util.Date;

@Controller
public class PostController {

    private final PostStore store = PostStore.instOf();
    private static int id = 0;

    @GetMapping("/posts")
    public String posts(Model model) {
        model.addAttribute("posts", store.findAll());
        return "posts";
    }

    @GetMapping("/formAddPost")
    public String addPost(Model model) {
        model.addAttribute(
                "post",
                new Post(++id,
                        "Заполните поле",
                        "Заполните поле",
                        new Timestamp(new Date().getTime())

        ));
        return "addPost";
    }
}
