package ru.job4j.dreamjob.controllers;

import net.jcip.annotations.ThreadSafe;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.job4j.dreamjob.model.Candidate;
import ru.job4j.dreamjob.services.CandidateService;
import ru.job4j.dreamjob.services.photoservice.PhotoService;

import java.io.IOException;

@Controller
@ThreadSafe
public class CandidateController {

    private final CandidateService candidateService;
    private final PhotoService photoService;

    public CandidateController(CandidateService candidateService, PhotoService photoService) {
        this.candidateService = candidateService;
        this.photoService = photoService;
    }

    @GetMapping("/candidates")
    public String candidates(Model model) {
        photoService.loadPhotos();
        model.addAttribute("candidates", candidateService.findAll());
        return "candidate/candidates";
    }

    @PostMapping("/createCandidate")
    public String addCandidate(@ModelAttribute Candidate candidate,
                               @RequestParam("file") MultipartFile file) throws IOException {
        candidate.setPhoto(file.getBytes());
        candidateService.add(candidate);
        photoService.addPhoto(candidate.getId(), candidate.getPhoto());
        return "redirect:/candidates";
    }

    @GetMapping("/photoCandidate/{candidateId}")
    public ResponseEntity<Resource> download(@PathVariable("candidateId") Integer candidateId) {
        Candidate candidate = candidateService.findById(candidateId);
        candidate.setPhoto(photoService.findById(candidateId));
        return ResponseEntity.ok()
                .headers(new HttpHeaders())
                .contentLength(candidate.getPhoto().length)
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(new ByteArrayResource(candidate.getPhoto()));
    }

    @GetMapping("/formAddCandidate")
    public String addCandidate() {
        return "candidate/addCandidate";
    }

    @GetMapping("/formUpdateCandidate/{candidateId}")
    public String formUpdateCandidate(Model model,
                                      @PathVariable("candidateId") int id) {
        model.addAttribute("candidate", candidateService.findById(id));
        return "candidate/updateCandidate";
    }

    @PostMapping("/updateCandidate")
    public String updateCandidate(@ModelAttribute Candidate candidate,
                                  @RequestParam("file") MultipartFile file) throws IOException {
        candidate.setPhoto(file.getBytes());
        candidateService.update(candidate);
        photoService.updatePhoto(candidate.getId(), candidate.getPhoto());
        return "redirect:candidates";
    }
}
