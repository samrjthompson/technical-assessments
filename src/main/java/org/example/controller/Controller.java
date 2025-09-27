package org.example.controller;

import java.util.function.Supplier;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@CrossOrigin
@RestController
public class Controller {

    private final Supplier<WebClient> webClientSupplier;

    public Controller(Supplier<WebClient> webClientSupplier) {
        this.webClientSupplier = webClientSupplier;
    }

    // This is for demo endpoint only to test cucumber tests and wiremock stubbing
    @GetMapping("/fetch/external")
    public ResponseEntity<String> fetchExternal() {
        HttpStatusCode statusCode;
        ResponseEntity<String> responseEntity;
        try {
            responseEntity = webClientSupplier.get()
                    .get()
                    .uri("/external-endpoint")
                    .retrieve()
                    .toEntity(String.class)
                    .blockOptional()
                    .orElseThrow(RuntimeException::new);

            statusCode = responseEntity.getStatusCode();
        } catch (WebClientResponseException ex) {
            statusCode = ex.getStatusCode();
        }
        return ResponseEntity.status(statusCode).build();
    }
}
