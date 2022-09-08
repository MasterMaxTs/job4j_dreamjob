package ru.job4j.dreamjob.service.candidateservice;

import ru.job4j.dreamjob.model.Candidate;

import java.util.List;

public interface CandidateService {

    Candidate add(Candidate candidate);

    List<Candidate> findAll();

    void update(Candidate candidate);

    Candidate findById(int id);
}
