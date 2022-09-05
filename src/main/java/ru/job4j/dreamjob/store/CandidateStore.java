package ru.job4j.dreamjob.store;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.Candidate;

import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
@ThreadSafe
public class CandidateStore implements Store<Candidate> {

    private final Map<Integer, Candidate> candidates =
                                                    new ConcurrentHashMap<>();
    private final AtomicInteger id = new AtomicInteger(3);

    private CandidateStore() {
        candidates.put(1,
                        new Candidate(
                                "Junior Java Developer",
                                "here is description",
                                new Timestamp(new Date().getTime()),
                                new byte[]{}
                        ));
        candidates.put(2,
                        new Candidate(
                                "Middle Java Developer",
                                "here is description",
                                new Timestamp(new Date().getTime()),
                                new byte[]{}
                        ));
        candidates.put(3,
                        new Candidate(
                                "Senior Java Developer",
                                "here is description",
                                new Timestamp(new Date().getTime()),
                                new byte[]{}
                        ));
    }

    @Override
    public Candidate add(Candidate candidate) {
        candidate.setId(id.incrementAndGet());
        candidate.setCreated(
                new Timestamp(new Date().getTime())
        );
        return candidates.putIfAbsent(candidate.getId(), candidate);
    }

    @Override
    public List<Candidate> findAll() {
        return new ArrayList<>(candidates.values());
    }

    @Override
    public Candidate findById(int id) {
        return candidates.get(id);
    }

    @Override
    public void update(Candidate candidate) {
        candidate.setCreated(
                new Timestamp(new Date().getTime())
        );
        candidates.replace(candidate.getId(), candidate);
    }
}
