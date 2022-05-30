package ru.job4j.dreamjob.store;

import ru.job4j.dreamjob.model.Candidate;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CandidateStore {

    private static final CandidateStore INST = new CandidateStore();
    private final Map<Integer, Candidate> candidates =
                                                    new ConcurrentHashMap<>();

    private CandidateStore() {
        candidates.put(1,
                        new Candidate(
                                1,
                                "Junior Java Developer",
                                "here is description",
                                new Timestamp(new Date().getTime())
                        ));
        candidates.put(2,
                        new Candidate(
                                2,
                                "Middle Java Developer",
                                "here is description",
                                new Timestamp(new Date().getTime())
                        ));
        candidates.put(3,
                        new Candidate(
                                3,
                                "Senior Java Developer",
                                "here is description",
                                new Timestamp(new Date().getTime())
                        ));
    }

    public static CandidateStore instOf() {
        return new CandidateStore();
    }

    public Collection<Candidate> findAll() {
        return candidates.values();
    }
}
