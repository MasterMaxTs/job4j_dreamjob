package ru.job4j.dreamjob.store.candidatestore;

import ru.job4j.dreamjob.model.Candidate;

import java.util.List;

public interface CandidateStore {

    Candidate add(Candidate candidate);

    List<Candidate> findAll();

    void update(Candidate candidate);

    Candidate findById(int id);
}
