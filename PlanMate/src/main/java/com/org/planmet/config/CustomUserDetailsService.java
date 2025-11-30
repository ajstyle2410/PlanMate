package com.org.planmet.config;

import com.org.planmet.model.UserProfile;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Custom UserDetailsService implementation for Spring Security
 * Loads user-specific data from the database for authentication
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private SessionFactory sessionFactory;

    /**
     * Loads user by username or email for authentication
     * @param identifier username or email
     * @return UserDetails object for Spring Security
     * @throws UsernameNotFoundException if user not found
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        Session session = sessionFactory.getCurrentSession();
        
        UserProfile userProfile = session
            .createQuery("FROM UserProfile WHERE username = :identifier OR email = :identifier", UserProfile.class)
            .setParameter("identifier", identifier)
            .uniqueResult();

        if (userProfile == null) {
            throw new UsernameNotFoundException("User not found with identifier: " + identifier);
        }

        return buildUserDetails(userProfile);
    }

    /**
     * Builds Spring Security UserDetails from UserProfile entity
     * @param userProfile the user entity from database
     * @return UserDetails for Spring Security
     */
    private UserDetails buildUserDetails(UserProfile userProfile) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        
        // Default role for all users
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        
        // Add additional roles based on your business logic
        // For example, check if user is admin:
        // if (userProfile.isAdmin()) {
        //     authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        // }

        return User.builder()
            .username(userProfile.getUsername())
            .password(userProfile.getPassword()) // Already BCrypt encoded
            .authorities(authorities)
            .accountExpired(false)
            .accountLocked(false)
            .credentialsExpired(false)
            .disabled(false)
            .build();
    }
}
