package ru.job4j.dreamjob.store;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.*;
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

    private static BasicDataSource pool;
    private CandidateDBStore store;
    private List<Candidate> candidates;

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
        store = new CandidateDBStore(pool);
        Timestamp now = new Timestamp(new Date().getTime());
        Candidate firstCandidate = new Candidate(
                "Maxim", "Middle Java Developer", now, new byte[]{}
        );
        Candidate secondCandidate = new Candidate(
                "Viktoriya", "Senior Java Developer", now, new byte[]{}
        );
        candidates = List.of(firstCandidate, secondCandidate);
    }

    @After
    public void wipeTable() throws SQLException {
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
        assertThat(candidateInDb.getDescription()
                , is(firstCandidate.getDescription())
        );
    }

    @Test
    public void whenFindAllCandidate() {
        candidates.forEach(store::add);
        List<Candidate> rsl = store.findAll();
        assertThat(rsl.get(0).getName(), is(candidates.get(0).getName()));
        assertThat(rsl.get(1).getDescription()
                , is(candidates.get(1).getDescription())
        );
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
        assertThat(candidateInDb.getDescription()
                , is(firstCandidate.getDescription())
        );
    }

    @Test
    public void whenFindCandidateById() {
        Candidate firstCandidate = candidates.get(0);
        store.add(firstCandidate);
        Candidate candidateInDb = store.findById(firstCandidate.getId());
        assertThat(firstCandidate.getDescription()
                , is(candidateInDb.getDescription())
        );
        assertNull(store.findById(2));
    }
}