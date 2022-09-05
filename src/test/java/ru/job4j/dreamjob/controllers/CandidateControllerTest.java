package ru.job4j.dreamjob.controllers;

import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import ru.job4j.dreamjob.model.Candidate;
import ru.job4j.dreamjob.services.CandidateService;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class CandidateControllerTest {

    private CandidateService candidateService;
    private CandidateController controller;
    private Model model;
    private MultipartFile file;
    private List<Candidate> candidates;

    @Before
    public void whenSetUp() {
        candidateService = mock(CandidateService.class);
        model = mock(Model.class);
        file = mock(MultipartFile.class);
        controller = new CandidateController(candidateService);
        candidates = List.of(
                    new Candidate(1,
                                "name1",
                            "description1",
                                    new Timestamp(new Date().getTime()),
                                    new byte[]{}
                    ),
                    new Candidate(2,
                               "name2",
                            "description2",
                                    new Timestamp(new Date().getTime()),
                                    new byte[]{}
                    )
        );
    }

    @Test
    public void whenCandidates() {
        when(candidateService.findAll()).thenReturn(candidates);
        String page = controller.candidates(model);
        verify(model).addAttribute("candidates", candidates);
        assertThat(page, is("candidate/candidates"));
    }

    @Test
    public void whenAddCandidate() throws IOException {
        Candidate candidate = candidates.get(0);
        String page = controller.addCandidate(candidate, file);
        verify(candidateService).add(candidate);
        assertThat(page, is("redirect:/candidates"));
    }

    @Test
    public void whenUpdateCandidateFromForm() {
        Candidate forUpdate = candidates.get(0);
        int id = forUpdate.getId();
        when(candidateService.findById(id)).thenReturn(forUpdate);
        String page = controller.formUpdateCandidate(model, id);
        verify(model).addAttribute("candidate", candidateService.findById(id));
        assertThat(page, is("candidate/updateCandidate"));
    }

    @Test
    public void whenUpdateCandidate() throws IOException {
        Candidate input = candidates.get(0);
        String page = controller.updateCandidate(input, file);
        verify(candidateService).update(input);
        assertThat(page, is("redirect:candidates"));
    }
}