package ru.job4j.dreamjob.service.postservice;

import net.jcip.annotations.ThreadSafe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.job4j.dreamjob.model.Post;
import ru.job4j.dreamjob.service.cityservice.CityService;
import ru.job4j.dreamjob.store.poststore.PostStore;

import java.util.List;

@Service
@ThreadSafe
public class PostServiceImpl implements PostService {

    private final PostStore store;
    private final CityService cityService;

    @Autowired
    public PostServiceImpl(@Qualifier("postDBStore") PostStore store,
                           CityService cityService) {
        this.store = store;
        this.cityService = cityService;
    }

    @Override
    public List<Post> findAll() {
        List<Post> posts = store.findAll();
        posts.forEach(
                post -> post.setCity(
                        cityService.findById(
                                post.getCity().getId()
                        ))
        );
        return posts;
    }

    @Override
    public Post add(Post post) {
        return store.add(post);
    }

    @Override
    public Post findById(int id) {
        return store.findById(id);
    }

    @Override
    public void delete(int id) {
        store.delete(id);
    }

    @Override
    public void update(Post post) {
        store.update(post);
    }
}
