package ru.job4j.dreamjob.store;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.Candidate;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class CandidateDBStore {

    private final BasicDataSource pool;
    private static final Logger LOG = Logger.getLogger(CandidateDBStore.class);

    public CandidateDBStore(BasicDataSource pool) {
        this.pool = pool;
    }

    public List<Candidate> findAll() {
        String sql = "SELECT * FROM candidate ORDER BY 1";
        List<Candidate> candidates = new ArrayList<>();
        LOG.info("Trying to get all candidates from DB");
        try (Connection cn = pool.getConnection()) {
            PreparedStatement ps = cn.prepareStatement(sql);
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    candidates.add(
                            new Candidate(
                                        it.getInt("id"),
                                        it.getString("name"),
                                        it.getString("description"),
                                        it.getTimestamp("created"),
                                        it.getBytes("photo")
                            )
                    );
                }
            }
            LOG.info("Success!");
        } catch (Exception e) {
            LOG.error("Not successful: " + e.getMessage(), e);
        }
        return candidates;
    }

    public Candidate add(Candidate candidate) {
        String sql = "INSERT INTO candidate (name, description, created, photo)"
                + "VALUES (? , ? , ? , ?)";
        LOG.info("Trying to add a candidate to DB");
        try (Connection cn = pool.getConnection()) {
            PreparedStatement ps =
                    cn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, candidate.getName());
            ps.setString(2, candidate.getDescription());
            ps.setTimestamp(3, new Timestamp(new Date().getTime()));
            ps.setBytes(4, candidate.getPhoto());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                while (id.next()) {
                    candidate.setId(id.getInt("id"));
                }
            }
            LOG.info("Success!");
        } catch (Exception e) {
            LOG.error("Not successful: " + e.getMessage(), e);
        }
        return candidate;
    }

    public void update(Candidate candidate) {
        String sql = "UPDATE candidate SET name = ? , description = ? , "
                + "created = ? , photo = ? WHERE id = ?";
        LOG.info("Trying to update candidate in the DB");
        try (Connection cn = pool.getConnection()) {
            PreparedStatement ps = cn.prepareStatement(sql);
            ps.setString(1, candidate.getName());
            ps.setString(2, candidate.getDescription());
            ps.setTimestamp(3, new Timestamp(new Date().getTime()));
            ps.setBytes(4, candidate.getPhoto());
            ps.setInt(5, candidate.getId());
            ps.executeUpdate();
            LOG.info("Success!");
        } catch (SQLException e) {
            LOG.error("Not successful: " + e.getMessage(), e);
        }
    }

    public Candidate findById(int id) {
        String sql = "SELECT * FROM candidate WHERE id = ?";
        LOG.info("Trying to find a candidate by id");
        try (Connection cn = pool.getConnection()) {
            PreparedStatement ps = cn.prepareStatement(sql);
            ps.setInt(1, id);
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    return new Candidate(
                            it.getInt("id"),
                            it.getString("name"),
                            it.getString("description"),
                            it.getTimestamp("created"),
                            it.getBytes("photo")
                    );
                }
            }
            LOG.info("Success!");
        } catch (Exception e) {
            LOG.error("Not successful: " + e.getMessage(), e);
        }
        return null;
    }
}
