package ru.job4j.dreamjob.store;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.log4j.Logger;

@Repository
public class UserDBStore implements Store<User> {

    private final BasicDataSource pool;
    private static final Logger LOG = Logger.getLogger("UserDBStore.class");

    public UserDBStore(BasicDataSource pool) {
        this.pool = pool;
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        LOG.info("Trying to get all users from DB");
        try (Connection cn = pool.getConnection()) {
            PreparedStatement ps = cn.prepareStatement(sql);
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    users.add(
                            new User(it.getInt("id"),
                                    it.getString("name"),
                                    it.getString("email"),
                                    ""
                            )
                    );
                }
            }
            LOG.info("Success!");
        } catch (SQLException e) {
            LOG.error("Not successful: " + e.getMessage(), e);
        }
        return users;
    }

    @Override
    public User add(User user) {
        User rsl = null;
        String sql = "INSERT INTO users (name, email, password)"
                + "VALUES (? , ? , ?)";
        LOG.info("Trying to add a user to DB");
        try (Connection cn = pool.getConnection()) {
            PreparedStatement ps = cn.prepareStatement(
                    sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    user.setId(id.getInt("id"));
                }
            }
            LOG.info("Success!");
            rsl = user;
        } catch (SQLException e) {
            LOG.error("Not successful: " + e.getMessage(), e);
        }
        return rsl;
    }

    @Override
    public void update(User user) {
        String sql = "UPDATE users SET name = ? , email = ? , password = ?"
                + "WHERE id = ?";
        LOG.info("Trying to update user in the DB");
        try (Connection cn = pool.getConnection()) {
            PreparedStatement ps = cn.prepareStatement(sql);
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setInt(4, user.getId());
            ps.executeUpdate();
            LOG.info("Success!");
        } catch (SQLException e) {
            LOG.error("Not successful: " + e.getMessage(), e);
        }
    }

    public User findById(int id) {
        User rsl = null;
        String sql = "SELECT * FROM users WHERE id = ?";
        LOG.info("Trying to find a user by id");
        try (Connection cn = pool.getConnection()) {
            PreparedStatement ps = cn.prepareStatement(sql);
            ps.setInt(1, id);
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    rsl = new User(it.getInt("id"),
                                   it.getString("name"),
                                   it.getString("email"),
                            "");
                }
            }
            LOG.info("Success!");
        } catch (SQLException e) {
            LOG.error("Not successful: " + e.getMessage(), e);
        }
        return rsl;
    }

    public Optional<User> findUserByEmailAndPwd(String email, String password) {
        Optional<User> rsl = Optional.empty();
        String sql = "SELECT * FROM users WHERE email = ? AND password = ?";
        LOG.info("Trying to find a user by email and password");
        try (Connection cn = pool.getConnection()) {
            PreparedStatement ps = cn.prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, password);
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    rsl = Optional.of(
                            new User(it.getInt("id"),
                                    it.getString("name"),
                                    it.getString("email"),
                                    it.getString("password")
                            )
                    );
                }
            }
            LOG.info("Success!");
        } catch (SQLException e) {
            LOG.error("Not successful: " + e.getMessage(), e);
        }
        return rsl;
    }
}
