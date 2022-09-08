package ru.job4j.dreamjob.store;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.*;
import ru.job4j.dreamjob.Main;
import ru.job4j.dreamjob.model.City;
import ru.job4j.dreamjob.model.Post;
import ru.job4j.dreamjob.store.poststore.PostDBStore;
import ru.job4j.dreamjob.store.poststore.PostStore;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class PostDBStoreTest {

    private static BasicDataSource pool;
    private PostStore store;
    private List<Post> posts;

    @BeforeClass
    public static void initPool() {
        pool = new Main().loadPool();
    }

    @AfterClass
    public static void closePool() throws SQLException {
        pool.close();
    }

    @Before
    public void whenSetUp() {
        store = new PostDBStore(pool);
        Timestamp now = new Timestamp(new Date().getTime());
        City firstCity = new City(1, "Краснодар");
        City secondCity = new City(2, "Москва");
        Post firstPost = new Post(
                "Java Junior Job", "Description1", now, firstCity, true
        );
        Post secondPost = new Post(
                "Java Middle Job", "Description2", now, secondCity, true
        );
        posts = List.of(firstPost, secondPost);
    }

    @After
    public void wipeTable() {
        try (Connection cn = pool.getConnection()) {
            PreparedStatement ps = cn.prepareStatement("DELETE FROM posts");
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
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
        posts.forEach(store::add);
        List<Post> rsl = store.findAll();
        assertThat(rsl.get(0).getName(), is(posts.get(0).getName()));
        assertThat(rsl.get(1).getDescription(), is(posts.get(1).getDescription()));
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
        assertThat(firstPost.getName(),
                is(store.findById(firstPost.getId()).getName()));
        assertNull(store.findById(2));
    }

    @Test
    public void whenDeletePost(){
        posts.forEach(store::add);
        Post post= posts.get(0);
        store.delete(post.getId());
        List<Post> rsl = store.findAll();
        assertEquals(1, rsl.size());
        assertThat(rsl.get(0).getName(), is(posts.get(1).getName()));
    }
}
