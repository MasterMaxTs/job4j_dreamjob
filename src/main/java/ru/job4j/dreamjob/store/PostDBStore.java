package ru.job4j.dreamjob.store;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.stereotype.Repository;
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

    public PostDBStore(BasicDataSource pool) {
        this.pool = pool;
    }

    public List<Post> findAll() {
        String sql = "SELECT * FROM post";
        List<Post> posts = new ArrayList<>();
        try (Connection cn = pool.getConnection()) {
            PreparedStatement ps = cn.prepareStatement(sql);
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    posts.add(
                            new Post(
                                    it.getInt("id"),
                                    it.getString("name"),
                                    it.getString("description")
                            )
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return posts;
    }

    public Post add(Post post) {
        String sql = "INSERT INTO post (name, city_id, description, created)"
                        + "VALUES (?, ?, ?, ?)";
        try (Connection cn = pool.getConnection()) {
            PreparedStatement ps =
                    cn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, post.getName());
            ps.setInt(2, post.getCity().getId());
            ps.setString(3, post.getDescription());
            ps.setTimestamp(4, new Timestamp(post.getCreated().getTime()));
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    post.setId(id.getInt("id"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return post;
    }

    public void update(Post post) {
        String sql = "UPDATE post SET name = ? , city_id = ? , description = ? , "
                + "created = ? WHERE id = ?";
        try (Connection cn = pool.getConnection()) {
            PreparedStatement ps = cn.prepareStatement(sql);
            ps.setString(1, post.getName());
            ps.setInt(2, post.getCity().getId());
            ps.setString(3, post.getDescription());
            ps.setTimestamp(4, new Timestamp(new Date().getTime()));
            ps.setInt(5, post.getId());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Post findById(int id) {
        String sql = "SELECT * FROM post WHERE id = ?";
        try (Connection cn = pool.getConnection()) {
           PreparedStatement ps = cn.prepareStatement(sql);
           ps.setInt(1, id);
           try (ResultSet it = ps.executeQuery()) {
               if (it.next()) {
                   return new Post(
                           it.getInt("id"),
                           it.getString("name"),
                           it.getString("description")
                   );
               }
           }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
