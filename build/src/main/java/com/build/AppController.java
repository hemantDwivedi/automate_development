package com.build;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppController {
    private final BuildService buildService;

    public AppController(BuildService buildService) {
        this.buildService = buildService;
    }

    @GetMapping("/api/build/start")
    public ResponseEntity<String> startBuild() {
        return buildService.startBuild();
    }
}


