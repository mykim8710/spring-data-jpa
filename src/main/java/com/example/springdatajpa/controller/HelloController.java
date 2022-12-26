package com.example.springdatajpa.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class HelloController {
    @GetMapping("/hello")
    public String helloView() {
        log.info("[GET] /hello");
        return "hello";
    }
}
