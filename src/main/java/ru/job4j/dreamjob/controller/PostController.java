package ru.job4j.dreamjob.controller;

import net.jcip.annotations.ThreadSafe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.dreamjob.model.City;
import ru.job4j.dreamjob.model.Post;
import ru.job4j.dreamjob.model.User;
import ru.job4j.dreamjob.service.cityservice.CityService;
import ru.job4j.dreamjob.service.postservice.PostService;

import javax.servlet.http.HttpSession;

@Controller
@ThreadSafe
public class PostController implements ManageSession {

    private final PostService postService;
    private final CityService cityService;

    @Autowired
    public PostController(PostService postService, CityService cityService) {
        this.postService = postService;
        this.cityService = cityService;
    }

    @ModelAttribute("user")
    public User getUser(HttpSession session) {
        return getUserFromSession(session);
    }

    @GetMapping("/posts")
    public String posts(Model model) {
        model.addAttribute("posts", postService.findAll());
        return "post/posts";
    }

    @PostMapping("/createPost")
    public String createPost(@ModelAttribute Post post,
                             @RequestParam("city.id") int cityId) {
        post.setCity(cityService.findById(cityId));
        postService.add(post);
        return "redirect:/posts";
    }
    @GetMapping("/formAddPost")
    public String addPost(Model model) {
        model.addAttribute("cities", cityService.getAllCities());
        return "post/addPost";
    }

    @GetMapping("/formUpdatePost/{postId}")
    public String formUpdatePost(Model model,
                                 @PathVariable("postId") int id
    ) {
        Post post = postService.findById(id);
        model.addAttribute("post", post);
        model.addAttribute("city", cityService.findById(post.getCity().getId()));
        model.addAttribute("cities", cityService.getAllCities());
        return "post/updatePost";
    }

    @PostMapping("/updatePost")
    public String updatePost(@ModelAttribute Post post,
                             @RequestParam("city.id") int cityId) {
        post.setCity(cityService.findById(cityId));
        postService.update(post);
        return "redirect:/posts";
    }

    @GetMapping("/formDeletePost/{postId}")
    public String confirmDeletePost(Model model,
                             @PathVariable("postId") int id) {
        Post post = postService.findById(id);
        post.setCity(cityService.findById(post.getCity().getId()));
        model.addAttribute("post", post);
        return "post/deletePost";
    }

    @GetMapping("/deletePost")
    public String deletePost(@RequestParam("id") int id) {
        postService.delete(id);
        return "redirect:/posts";
    }
}
