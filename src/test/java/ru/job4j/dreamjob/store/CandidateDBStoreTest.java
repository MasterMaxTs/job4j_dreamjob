package ru.job4j.dreamjob.store;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.job4j.dreamjob.Main;
import ru.job4j.dreamjob.model.Candidate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class CandidateDBStoreTest {

    private List<Candidate> candidates;
    private CandidateDBStore store;

    @Before
    public void whenSetUp() {
        Timestamp now = new Timestamp(new Date().getTime());
        Candidate firstCandidate = new Candidate(
                1, "Maxim", "Middle Java Developer", now, new byte[]{}
        );
        Candidate secondCandidate = new Candidate(
                2, "Viktoriya", "Senior Java Developer", now, new byte[]{}
        );
        candidates = List.of(firstCandidate, secondCandidate);
        store = new CandidateDBStore(new Main().loadPool());
    }

    @After
    public void wipeTable() throws SQLException {
        BasicDataSource pool = new Main().loadPool();
        try (Connection cn = pool.getConnection()) {
            PreparedStatement ps = cn.prepareStatement("DELETE FROM candidate");
            ps.execute();
        }
    }

    @Test
    public void whenCreateCandidate() {
        Candidate firstCandidate = candidates.get(0);
        store.add(firstCandidate);
        Candidate candidateInDb = store.findById(firstCandidate.getId());
        assertThat(candidateInDb.getName(), is(firstCandidate.getName()));
        assertThat(candidateInDb.getDescription(), is(firstCandidate.getDescription()));
    }

    @Test
    public void whenFindAllCandidate() {
        store.add(candidates.get(0));
        store.add(candidates.get(1));
        assertEquals(candidates, store.findAll());
    }

    @Test
    public void whenUpdateCandidate() {
        Candidate firstCandidate = candidates.get(0);
        store.add(firstCandidate);
        firstCandidate.setName("Oleg");
        firstCandidate.setDescription("Junior Java Job");
        store.update(firstCandidate);
        Candidate candidateInDb = store.findById(firstCandidate.getId());
        assertThat(candidateInDb.getName(), is(firstCandidate.getName()));
        assertThat(candidateInDb.getDescription(), is(firstCandidate.getDescription()));
    }

    @Test
    public void whenFindCandidateById() {
        Candidate firstCandidate = candidates.get(0);
        store.add(firstCandidate);
        assertEquals(firstCandidate, store.findById(firstCandidate.getId()));
        assertNull(store.findById(2));
    }
}