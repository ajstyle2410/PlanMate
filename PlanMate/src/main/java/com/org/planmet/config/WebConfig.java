package com.org.planmet.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC Configuration
 * Configures CORS and other web-related settings
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Configure CORS mappings
     * Allows frontend applications to access the API from different origins
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        String allowedOrigins = getAllowedOrigins();

        registry.addMapping("/**")
                .allowedOrigins(allowedOrigins.split(","))
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600); // Cache preflight response for 1 hour
    }

    /**
     * Get allowed origins from environment variable or use defaults
     * 
     * @return comma-separated list of allowed origins
     */
    private String getAllowedOrigins() {
        String origins = System.getenv("ALLOWED_ORIGINS");

        // Default to localhost for development if not set
        if (origins == null || origins.trim().isEmpty()) {
            return "http://localhost:4200,http://localhost:3000";
        }

        return origins;
    }
}
