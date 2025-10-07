package com.org.planmet.controllers;

import com.org.planmet.Iservice.AdminService;
import com.org.planmet.model.Admin;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminAuthController {

    private final AdminService adminService;

    @Autowired
    public AdminAuthController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("adminLogin", new AdminLoginForm());
        return "admin/login";
    }

    @PostMapping("/login")
    public String handleLogin(@ModelAttribute("adminLogin") AdminLoginForm form,
                              HttpSession session,
                              Model model) {
        Admin admin = adminService.login(form.getEmail(), form.getPassword());
        if (admin == null) {
            model.addAttribute("error", "Invalid email or password");
            return "admin/login";
        }
        session.setAttribute("adminUser", admin);
        return "redirect:/admin";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("adminUser");
        return "redirect:/admin/login";
    }

    public static class AdminLoginForm {
        private String email;
        private String password;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
