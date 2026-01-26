package com.fitness.activityservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserValidationService {

    // This field must be private and final for @RequiredArgsConstructor to inject it
    private final WebClient userServiceWebClient;

    public boolean validateUser(String userId) {
        log.info("Calling userValidation API for userId:{}", userId);
        try {
            // Making a synchronous call using .block()
            return Boolean.TRUE.equals(userServiceWebClient.get()
                    .uri("/api/users/{userId}/validate", userId)
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block());

        } catch (WebClientResponseException e) {
            // Specific handling for HTTP error codes
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new RuntimeException("User Not Found: " + userId);
            } else if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                throw new RuntimeException("Invalid Request: " + userId);
            }

            // Generic fallback for other HTTP errors (500, etc)
            throw new RuntimeException("User Service Error: " + e.getMessage());

        } catch (Exception e) {
            // Catching connection issues (Service down, timeout)
            throw new RuntimeException("User Service is unreachable: " + e.getMessage());
        }
    }
}