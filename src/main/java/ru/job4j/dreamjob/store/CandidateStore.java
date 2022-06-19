package ru.job4j.dreamjob.store;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.Candidate;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
@ThreadSafe
public class CandidateStore {

    private final Map<Integer, Candidate> candidates =
                                                    new ConcurrentHashMap<>();
    private final AtomicInteger id = new AtomicInteger(3);

    private CandidateStore() {
        candidates.put(1,
                        new Candidate(
                                1,
                                "Junior Java Developer",
                                "here is description",
                                new Timestamp(new Date().getTime()),
                                new byte[]{}
                        ));
        candidates.put(2,
                        new Candidate(
                                2,
                                "Middle Java Developer",
                                "here is description",
                                new Timestamp(new Date().getTime()),
                                new byte[]{}
                        ));
        candidates.put(3,
                        new Candidate(
                                3,
                                "Senior Java Developer",
                                "here is description",
                                new Timestamp(new Date().getTime()),
                                new byte[]{}
                        ));
    }

    public boolean add(Candidate candidate) {
        candidate.setId(id.incrementAndGet());
        candidate.setCreated(
                new Timestamp(new Date().getTime())
        );
        return candidates.putIfAbsent(candidate.getId(), candidate) != null;
    }

    public Collection<Candidate> findAll() {
        return candidates.values();
    }

    public Candidate findById(int id) {
        return candidates.get(id);
    }

    public void update(Candidate candidate) {
        candidate.setCreated(
                new Timestamp(new Date().getTime())
        );
        candidates.replace(candidate.getId(), candidate);
    }
}
