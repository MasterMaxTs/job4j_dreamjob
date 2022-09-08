package ru.job4j.dreamjob.store.poststore;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.City;
import ru.job4j.dreamjob.model.Post;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PostDBStore implements PostStore {

    private final BasicDataSource pool;
    private static final Logger LOG = Logger.getLogger(PostDBStore.class);

    @Autowired
    public PostDBStore(BasicDataSource pool) {
        this.pool = pool;
    }

    @Override
    public List<Post> findAll() {
        String sql = "SELECT * FROM posts ORDER BY 1";
        List<Post> posts = new ArrayList<>();
        LOG.info("Trying to get all posts from DB");
        try (Connection cn = pool.getConnection()) {
            PreparedStatement ps = cn.prepareStatement(sql);
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    Post post = new Post(it.getString("name"),
                            it.getString("description"),
                            it.getTimestamp("created"),
                            new City(it.getInt("city_id"), ""),
                            it.getBoolean("visible")
                    );
                    post.setId(it.getInt("id"));
                    posts.add(post);
                }
            }
            LOG.info("Success!");
        } catch (Exception e) {
            LOG.error("Not successful: " + e.getMessage(), e);
        }
        return posts;
    }

    @Override
    public Post add(Post post) {
        String sql = "INSERT INTO posts (name, description, created, "
                + "city_id, visible) VALUES (?, ?, ?, ?, ?)";
        LOG.info("Trying to add a post to DB");
        try (Connection cn = pool.getConnection()) {
            PreparedStatement ps =
                    cn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, post.getName());
            ps.setString(2, post.getDescription());
            ps.setTimestamp(3,
                    Timestamp.valueOf(LocalDateTime.now().withNano(0))
            );
            ps.setInt(4, post.getCity().getId());
            ps.setBoolean(5, post.isVisible());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    post.setId(id.getInt("id"));
                }
            }
            LOG.info("Success!");
        } catch (Exception e) {
            LOG.error("Not successful: " + e.getMessage(), e);
        }
        return post;
    }

    @Override
    public void update(Post post) {
        String sql = "UPDATE posts SET name = ? , description = ? , "
                + "created = ? , city_id = ? , visible = ? WHERE id = ?";
        LOG.info("Trying to update post in the DB");
        try (Connection cn = pool.getConnection()) {
            PreparedStatement ps = cn.prepareStatement(sql);
            ps.setString(1, post.getName());
            ps.setString(2, post.getDescription());
            ps.setTimestamp(3,
                    Timestamp.valueOf(LocalDateTime.now().withNano(0))
            );
            ps.setInt(4, post.getCity().getId());
            ps.setBoolean(5, post.isVisible());
            ps.setInt(6, post.getId());
            ps.executeUpdate();
            LOG.info("Success!");
        } catch (Exception e) {
            LOG.error("Not successful: " + e.getMessage(), e);
        }
    }

    @Override
    public Post findById(int id) {
        Post rsl = null;
        String sql = "SELECT * FROM posts WHERE id = ?";
        LOG.info("Trying to find a post by id");
        try (Connection cn = pool.getConnection()) {
           PreparedStatement ps = cn.prepareStatement(sql);
           ps.setInt(1, id);
           try (ResultSet it = ps.executeQuery()) {
               if (it.next()) {
                   rsl = new Post(it.getString("name"),
                                  it.getString("description"),
                                  it.getTimestamp("created"),
                                  new City(it.getInt("city_id"), ""),
                                  it.getBoolean("visible")
                   );
                   rsl.setId(it.getInt("id"));
               }
           }
        } catch (Exception e) {
            LOG.error("Not successful: " + e.getMessage(), e);
        }
        return rsl;
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM posts WHERE id = ?";
        LOG.info("Trying to delete a post by id");
        try (Connection cn = pool.getConnection()) {
            PreparedStatement ps = cn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (Exception e) {
            LOG.error("Not successful: " + e.getMessage(), e);
        }
    }
}
