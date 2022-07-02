package ru.job4j.dreamjob.store;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.job4j.dreamjob.Main;
import ru.job4j.dreamjob.model.City;
import ru.job4j.dreamjob.model.Post;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class PostDBStoreTest {

    private BasicDataSource pool;
    private PostDBStore store;
    private List<Post> posts;

    @Before
    public void whenSetUp() {
        pool = new Main().loadPool();
        store = new PostDBStore(pool);
        Timestamp now = new Timestamp(new Date().getTime());
        City firstCity = new City(1, "Краснодар");
        City secondCity = new City(2, "Москва");
        Post firstPost = new Post(
                1, "Java Junior Job", "Description", now, firstCity, true
        );
        Post secondPost = new Post(
                2, "Java Middle Job", "Description", now, secondCity, true
        );
        posts = List.of(firstPost, secondPost);
    }

    @After
    public void wipeTable() throws SQLException {
        try (Connection cn = pool.getConnection()) {
            PreparedStatement ps = cn.prepareStatement("DELETE FROM post");
            ps.execute();
        }
    }

    @Test
    public void whenCreatePost() {
        Post firstPost = posts.get(0);
        store.add(firstPost);
        Post postInDb = store.findById(firstPost.getId());
        assertThat(postInDb.getName(), is(firstPost.getName()));
        assertThat(postInDb.getDescription(), is(firstPost.getDescription()));
    }

    @Test
    public void whenFindAllPost() {
        store.add(posts.get(0));
        store.add(posts.get(1));
        assertEquals(posts, store.findAll());
    }

    @Test
    public void whenUpdatePost() {
        Post firstPost = posts.get(0);
        store.add(firstPost);
        firstPost.setName("Senior Java Job");
        firstPost.setDescription("New description");
        store.update(firstPost);
        Post postInDb = store.findById(firstPost.getId());
        assertThat(postInDb.getName(), is(firstPost.getName()));
        assertThat(postInDb.getDescription(), is(firstPost.getDescription()));
    }

    @Test
    public void whenFindPostById() {
        Post firstPost = posts.get(0);
        store.add(firstPost);
        assertEquals(firstPost, store.findById(firstPost.getId()));
        assertNull(store.findById(2));
    }
}
