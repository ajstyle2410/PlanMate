package com.org.planmet.Iservice;

import java.util.List;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.org.planmet.model.Trip;
import com.org.planmet.model.UserProfile;

@Service
public class UserProfileServiceImpl implements UserProfileService {

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public boolean registerUser(UserProfile userProfile) {
        String encryptedPassword = passwordEncoder.encode(userProfile.getPassword());
        userProfile.setPassword(encryptedPassword);

        sessionFactory.getCurrentSession().save(userProfile);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<List<UserProfile>> viewAllUsers() {
        List<UserProfile> users = sessionFactory.getCurrentSession()
                .createQuery("FROM UserProfile", UserProfile.class)
                .list();
        return Optional.ofNullable(users);
    }

    @Override
    @Transactional
    public UserProfile login(String identifier, String password) {
        if (identifier == null || identifier.isBlank()) {
            return null;
        }

        var session = sessionFactory.getCurrentSession();
        UserProfile user = session
                .createQuery("FROM UserProfile WHERE username = :identifier OR email = :identifier", UserProfile.class)
                .setParameter("identifier", identifier.trim())
                .uniqueResult();

        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return user;
        }
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserProfile> findUserDetails(String username) {
        try {
            UserProfile userProfile = (UserProfile) sessionFactory.getCurrentSession()
                    .createQuery("FROM UserProfile WHERE username = :username").setParameter("username", username)
                    .uniqueResult();

            return Optional.ofNullable(userProfile);
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public UserProfile findByUserId(long userId) {
        return (UserProfile) sessionFactory.getCurrentSession().createQuery("FROM UserProfile WHERE id = :userId")
                .setParameter("userId", userId).uniqueResult();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Trip> uploadedTripsUserProfile(long userId) {
        return sessionFactory.getCurrentSession()
                .createQuery("FROM Trip t WHERE t.user.id = :userId", Trip.class)
                .setParameter("userId", userId)
                .list();
    }
}
