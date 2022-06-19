package ru.job4j.dreamjob.services;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.dreamjob.model.Candidate;
import ru.job4j.dreamjob.services.photoservice.PhotoService;
import ru.job4j.dreamjob.store.CandidateDBStore;

import java.util.List;

@Service
@ThreadSafe
public class CandidateService {

    private final CandidateDBStore store;
    private final PhotoService photoService;

    public CandidateService(CandidateDBStore store, PhotoService photoService) {
        this.store = store;
        this.photoService = photoService;
    }

    public List<Candidate> findAll() {
        List<Candidate> candidates = store.findAll();
        candidates.forEach(
                candidate -> candidate.setPhoto(
                        photoService.findById(candidate.getId())
                )
        );
        return candidates;
    }

    public void add(Candidate candidate) {
        store.add(candidate);
    }

    public Candidate findById(int id) {
        return store.findById(id);
    }

    public void update(Candidate candidate) {
        store.update(candidate);
    }
}
