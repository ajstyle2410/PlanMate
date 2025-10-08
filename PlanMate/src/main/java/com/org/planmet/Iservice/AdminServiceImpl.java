//package com.org.planmet.Iservice;
//
//import com.org.planmet.model.Admin;
//import javax.transaction.Transactional;
//import org.hibernate.Session;
//import org.hibernate.SessionFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//@Service
//@Transactional
//public class AdminServiceImpl implements AdminService {
//
//    private static final String DEFAULT_EMAIL = "admin@gmail.com";
//    private static final String DEFAULT_PASSWORD = "admin123";
//
//    @Autowired
//    private SessionFactory sessionFactory;
//
//    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//
//    @Override
//    public Admin login(String email, String password) {
//        ensureDefaultAdminExists();
//
//        Admin admin = findByEmail(email);
//        if (admin != null && passwordEncoder.matches(password, admin.getPassword())) {
//            return admin;
//        }
//        return null;
//    }
//
//    @Override
//    public Admin findByEmail(String email) {
//        Session session = sessionFactory.getCurrentSession();
//        return session.createQuery("FROM Admin WHERE email = :email", Admin.class)
//                .setParameter("email", email)
//                .uniqueResult();
//    }
//
//    private void ensureDefaultAdminExists() {
//        Session session = sessionFactory.getCurrentSession();
//        Admin existing = session.createQuery("FROM Admin WHERE email = :email", Admin.class)
//                .setParameter("email", DEFAULT_EMAIL)
//                .uniqueResult();
//        if (existing == null) {
//            Admin admin = new Admin();
//            admin.setEmail(DEFAULT_EMAIL);
//            admin.setPassword(passwordEncoder.encode(DEFAULT_PASSWORD));
//            session.save(admin);
//        }
//    }
//}
