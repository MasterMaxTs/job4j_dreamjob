package ru.job4j.dreamjob.controllers;

import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.Model;
import ru.job4j.dreamjob.model.City;
import ru.job4j.dreamjob.model.Post;
import ru.job4j.dreamjob.services.CityService;
import ru.job4j.dreamjob.services.PostService;

import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;


public class PostControllerTest {

    private PostService postService;
    private CityService cityService;
    private PostController postController;
    private Model model;
    private HttpSession session;
    private List<City> cities;
    private List<Post> posts;

    @Before
    public void whenSetUp() {
        postService = mock(PostService.class);
        cityService = mock(CityService.class);
        model = mock(Model.class);
        session = mock(HttpSession.class);
        postController = new PostController(
                postService,
                cityService
        );
        cities = List.of(
                new City(1, "Москва"),
                new City(2, "Краснодар")
        );
        posts = List.of(
                    new Post(1,
                            "New post",
                            "desc",
                            new Timestamp(new Date().getTime()),
                            cities.get(0),
                            true
                    ),
                    new Post(2,
                            "New post",
                            "desc",
                            new Timestamp(new Date().getTime()),
                            cities.get(1),
                            true
                    )
        );
    }

    @Test
    public void whenPosts() {
        when(postService.findAll()).thenReturn(posts);
        String page = postController.posts(model, session);
        verify(model).addAttribute("posts", posts);
        assertThat(page, is("post/posts"));
    }

    @Test
    public void whenCreatePost() {
        Post input = posts.get(0);
        String page = postController.createPost(input, 1);
        verify(postService).add(input);
        assertThat(page, is("redirect:/posts"));
    }


    @Test
    public void whenUpdatePostFromForm() {
        Post forUpdate = posts.get(0);
        City currentCity = forUpdate.getCity();
        int postId= forUpdate.getId();
        int cityId = forUpdate.getCity().getId();
        when(postService.findById(postId)).thenReturn(forUpdate);
        when(cityService.findById(cityId)).thenReturn(currentCity);
        when(cityService.getAllCities()).thenReturn(cities);
        String page = postController.formUpdatePost(model, postId, session);
        verify(model).addAttribute("post", forUpdate);
        verify(model).addAttribute("city", currentCity);
        verify(model).addAttribute("cities", cities);
        verify(postService).findById(postId);
        assertThat(page, is("post/updatePost"));
    }

    @Test
    public void whenUpdatePost() {
        Post input = posts.get(0);
        City newCity = cities.get(1);
        int newCityId = newCity.getId();
        when(cityService.findById(newCityId)).thenReturn(newCity);
        String page = postController.updatePost(input, newCityId);
        verify(postService).update(input);
        assertThat(page, is("redirect:/posts"));
        assertThat(input.getCity(), is(newCity));
    }
}