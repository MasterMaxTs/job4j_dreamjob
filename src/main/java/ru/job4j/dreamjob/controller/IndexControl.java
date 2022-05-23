package ru.job4j.dreamjob.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexControl {

    @GetMapping("/index")
    public String index() {
        return "Greetings from Spring Boot!";
    }
}
