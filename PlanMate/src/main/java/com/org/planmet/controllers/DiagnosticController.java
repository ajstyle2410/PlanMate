package com.org.planmet.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Diagnostic Controller - Simple endpoint to verify Spring is working
 */
@RestController
@RequestMapping("/diagnostic")
public class DiagnosticController {

    @GetMapping
    public Map<String, String> diagnostic() {
        Map<String, String> info = new HashMap<>();
        info.put("status", "Spring MVC is working!");
        info.put("message", "If you see this, DispatcherServlet loaded successfully");
        info.put("timestamp", String.valueOf(System.currentTimeMillis()));
        return info;
    }

    @GetMapping("/env")
    public Map<String, String> checkEnvironment() {
        Map<String, String> env = new HashMap<>();
        env.put("DB_URL", System.getenv("DB_URL") != null ? "SET" : "NOT SET");
        env.put("DB_USERNAME", System.getenv("DB_USERNAME") != null ? "SET" : "NOT SET");
        env.put("DB_PASSWORD", System.getenv("DB_PASSWORD") != null ? "SET" : "NOT SET");
        env.put("SPRING_DATASOURCE_URL", System.getenv("SPRING_DATASOURCE_URL") != null ? "SET" : "NOT SET");
        return env;
    }
}
