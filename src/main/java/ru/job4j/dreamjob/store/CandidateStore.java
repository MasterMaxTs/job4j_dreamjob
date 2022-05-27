package ru.job4j.dreamjob.store;

import ru.job4j.dreamjob.model.Candidate;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CandidateStore {

    private static final CandidateStore INST = new CandidateStore();
    private final Map<Integer, Candidate> candidates =
                                                    new ConcurrentHashMap<>();

    private CandidateStore() {

    }

    public static CandidateStore instOf() {
        return new CandidateStore();
    }

    public Collection<Candidate> findAll() {
        return candidates.values();
    }
}
