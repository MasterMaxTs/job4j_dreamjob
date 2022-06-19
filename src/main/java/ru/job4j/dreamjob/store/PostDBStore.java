package ru.job4j.dreamjob.store;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.City;
import ru.job4j.dreamjob.model.Post;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class PostDBStore {

    private final BasicDataSource pool;
    private static final Logger LOG = Logger.getLogger(PostDBStore.class);

    public PostDBStore(BasicDataSource pool) {
        this.pool = pool;
    }

    public List<Post> findAll() {
        String sql = "SELECT * FROM post ORDER BY 1";
        List<Post> posts = new ArrayList<>();
        LOG.info("Trying to get all posts from DB");
        try (Connection cn = pool.getConnection()) {
            PreparedStatement ps = cn.prepareStatement(sql);
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    posts.add(
                            new Post(
                                    it.getInt("id"),
                                    it.getString("name"),
                                    it.getString("description"),
                                    it.getTimestamp("created"),
                                    new City(it.getInt("city_id"), ""),
                                    it.getBoolean("visible")
                            )
                    );
                }
            }
            LOG.info("Success!");
        } catch (Exception e) {
            LOG.error("Not successful: " + e.getMessage(), e);
        }
        return posts;
    }

    public Post add(Post post) {
        String sql = "INSERT INTO post (name, description, created, "
                + "city_id, visible) VALUES (?, ?, ?, ?, ?)";
        LOG.info("Trying to add a post to DB");
        try (Connection cn = pool.getConnection()) {
            PreparedStatement ps =
                    cn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, post.getName());
            ps.setString(2, post.getDescription());
            ps.setTimestamp(3, new Timestamp(new Date().getTime()));
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

    public void update(Post post) {
        String sql = "UPDATE post SET name = ? , description = ? , "
                + "created = ? , city_id = ? WHERE id = ?";
        LOG.info("Trying to update post in the DB");
        try (Connection cn = pool.getConnection()) {
            PreparedStatement ps = cn.prepareStatement(sql);
            ps.setString(1, post.getName());
            ps.setString(2, post.getDescription());
            ps.setTimestamp(3, new Timestamp(new Date().getTime()));
            ps.setInt(4, post.getCity().getId());
            ps.setInt(5, post.getId());
            ps.executeUpdate();
            LOG.info("Success!");
        } catch (Exception e) {
            LOG.error("Not successful: " + e.getMessage(), e);
        }
    }

    public Post findById(int id) {
        String sql = "SELECT * FROM post WHERE id = ?";
        LOG.info("Trying to find a post by id");
        try (Connection cn = pool.getConnection()) {
           PreparedStatement ps = cn.prepareStatement(sql);
           ps.setInt(1, id);
           try (ResultSet it = ps.executeQuery()) {
               if (it.next()) {
                   return new Post(
                           it.getInt("id"),
                           it.getString("name"),
                           it.getString("description"),
                           it.getTimestamp("created"),
                           new City(it.getInt("city_id"), ""),
                           it.getBoolean("visible")
                   );
               }
           }
        } catch (Exception e) {
            LOG.error("Not successful: " + e.getMessage(), e);
        }
        return null;
    }
}
