package com.coomiix.admin.shared.infrastructure.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api")
@Slf4j
public class HealthController {

    @GetMapping("/health")
    public ResponseEntity<HealthStatus> health() {
        log.debug("Health check endpoint called");
        return ResponseEntity.ok(new HealthStatus("UP"));
    }

    @GetMapping("/health/live")
    public ResponseEntity<HealthStatus> liveness() {
        log.debug("Liveness probe called");
        return ResponseEntity.ok(new HealthStatus("UP"));
    }

    @GetMapping("/health/ready")
    public ResponseEntity<HealthStatus> readiness() {
        log.debug("Readiness probe called");
        return ResponseEntity.ok(new HealthStatus("UP"));
    }

    public record HealthStatus(String status) { }
}
