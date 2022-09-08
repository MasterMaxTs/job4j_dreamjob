package ru.job4j.dreamjob.controller;

import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.Model;
import ru.job4j.dreamjob.model.City;
import ru.job4j.dreamjob.model.Post;
import ru.job4j.dreamjob.service.cityservice.CityServiceImpl;
import ru.job4j.dreamjob.service.postservice.PostServiceImpl;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;


public class PostControllerTest {

    private PostServiceImpl postService;
    private CityServiceImpl cityService;
    private PostController postController;
    private Model model;
    private List<City> cities;
    private List<Post> posts;

    @Before
    public void whenSetUp() {
        postService = mock(PostServiceImpl.class);
        cityService = mock(CityServiceImpl.class);
        model = mock(Model.class);
        postController = new PostController(postService, cityService);
        final Timestamp created = Timestamp.valueOf(LocalDateTime.now());
        cities = List.of(
                new City(1, "Москва"),
                new City(2, "Краснодар")
        );
        posts = List.of(
                    new Post("New post",
                            "desc",
                            created,
                            cities.get(0),
                            true
                    ),
                    new Post("New post",
                            "desc",
                            created,
                            cities.get(1),
                            true
                    )
        );
    }

    @Test
    public void whenPosts() {
        when(postService.findAll()).thenReturn(posts);
        String page = postController.posts(model);
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
        String page = postController.formUpdatePost(model, postId);
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

    @Test
    public void whenDeletePost() {

    }
}