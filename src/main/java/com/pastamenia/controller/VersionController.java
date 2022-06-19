package com.pastamenia.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VersionController {

    @GetMapping("/api/versions")
    public String retrieveVersion() {
        return "1.0.0";
    }
}
