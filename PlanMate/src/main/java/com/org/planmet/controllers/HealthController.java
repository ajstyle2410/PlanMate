package com.org.planmet.controllers;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Health Check Controller
 * Provides endpoint for monitoring application health
 * Used by Render and other monitoring services
 */
@RestController
@RequestMapping("/health")
public class HealthController {

    @Autowired
    private SessionFactory sessionFactory;

    /**
     * Health check endpoint
     * Returns application status and database connectivity
     * 
     * @return 200 OK if healthy, 503 Service Unavailable if database is down
     */
    @GetMapping
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> status = new HashMap<>();
        status.put("service", "PlanMate");

        try {
            // Test database connection
            sessionFactory.openSession().close();
            status.put("status", "UP");
            status.put("database", "connected");
            return ResponseEntity.ok(status);

        } catch (Exception e) {
            status.put("status", "DOWN");
            status.put("database", "disconnected");
            status.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(status);
        }
    }
}
