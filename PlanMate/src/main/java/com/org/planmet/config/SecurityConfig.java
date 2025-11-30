package com.org.planmet.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Spring Security Configuration
 * Configures authentication, authorization, and password encoding
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * Password encoder bean for hashing passwords
     * Using BCrypt with default strength (10)
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configure authentication manager with UserDetailsService and PasswordEncoder
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
            .passwordEncoder(passwordEncoder());
    }

    /**
     * Configure HTTP security
     * - Permits public access to login, register, and static resources
     * - Requires authentication for all other endpoints
     * - Uses form-based login with session management
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                // Public endpoints - no authentication required
                .antMatchers("/", "/home", "/loginpage", "/login", "/register", "/logindata").permitAll()
                .antMatchers("/css/**", "/js/**", "/images/**", "/webjars/**").permitAll()
                .antMatchers("/health").permitAll()
                // Admin endpoints
                .antMatchers("/admin/**").hasRole("ADMIN")
                // All other endpoints require authentication
                .anyRequest().authenticated()
                .and()
            .formLogin()
                .loginPage("/loginpage")
                .loginProcessingUrl("/logindata")
                .defaultSuccessUrl("/", true)
                .permitAll()
                .and()
            .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/loginpage?logout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
                .and()
            .csrf()
                // CSRF is enabled by default, consider disabling for REST APIs if needed
                .ignoringAntMatchers("/health")
                .and()
            .sessionManagement()
                .maximumSessions(1)
                .maxSessionsPreventsLogin(false);
    }
}
