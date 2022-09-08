package ru.job4j.dreamjob.service.candidateservice;

import net.jcip.annotations.ThreadSafe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.job4j.dreamjob.model.Candidate;
import ru.job4j.dreamjob.store.candidatestore.CandidateStore;

import java.util.List;

@Service
@ThreadSafe
public class CandidateServiceImpl implements CandidateService {

    private final CandidateStore store;

    @Autowired
    public CandidateServiceImpl(@Qualifier("candidateDBStore") CandidateStore store) {
        this.store = store;
    }

    @Override
    public List<Candidate> findAll() {
        return store.findAll();
    }

    @Override
    public Candidate add(Candidate candidate) {
        store.add(candidate);
        return candidate;
    }

    @Override
    public Candidate findById(int id) {
        return store.findById(id);
    }

    @Override
    public void update(Candidate candidate) {
        store.update(candidate);
    }
}
