package com.build;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppController {
    @PostMapping("/api/build/start")
    public ResponseEntity<String> startBuild() {
        return new ResponseEntity<>("completed", HttpStatus.OK);
    }
}


