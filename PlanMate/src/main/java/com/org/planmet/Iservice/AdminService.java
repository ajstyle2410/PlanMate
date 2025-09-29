package com.org.planmet.Iservice;

import com.org.planmet.model.Admin;

public interface AdminService {

    Admin login(String email, String password);

    Admin findByEmail(String email);
}
