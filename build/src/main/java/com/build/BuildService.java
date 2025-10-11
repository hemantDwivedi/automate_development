package com.build;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
public class BuildService {
    private final ExecutorService executor = Executors.newCachedThreadPool();

    public ResponseEntity<String> startBuild() {
        SseEmitter sseEmitter = initProcess();
        return ResponseEntity.ok().build();
    }

    private SseEmitter initProcess() {
        SseEmitter sseEmitter = new SseEmitter(Duration.ofMinutes(10).toMillis());
        executor.submit(() -> {
            ProcessBuilder processBuilder = new ProcessBuilder();

            String mavenHome = System.getenv("MAVEN_HOME");

            if (mavenHome == null){
                throw new IllegalStateException("Maven home not set");
            }

            String mvn = mavenHome + "\\bin\\mvn.cmd";

            processBuilder.command(mvn, "clean", "install");

            processBuilder.redirectErrorStream(true);

            Process process = null;
            try {

                process = processBuilder.start();
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

                String line;
                while (true) {
                    try {
                        if ((line = reader.readLine()) == null) break;
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    safeSend(sseEmitter, line); // stream each line
                }


                boolean finished = true;
                finished = process.waitFor(10, TimeUnit.MINUTES);

                if (!finished) {
                    process.destroyForcibly();
                    safeSend(sseEmitter, "Process timed out and was killed.");
                } else {
                    int exit = process.exitValue();
                    safeSend(sseEmitter, "Process exited with code: " + exit);
                }
            } catch (InterruptedException | IOException e) {
                safeSend(sseEmitter, "Error: " + e.getMessage());
            } finally {
                if (process != null && process.isAlive()) process.destroyForcibly();
                sseEmitter.complete();
            }


        });
        return sseEmitter;
    }

    private void safeSend(SseEmitter emitter, String message) {
        try {
            emitter.send(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
