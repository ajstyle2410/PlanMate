package com.org.planmet.controllers;

import com.org.planmet.Iservice.UserProfileService;
import com.org.planmet.model.AuthRequest;
import com.org.planmet.model.Trip;
import com.org.planmet.model.UserProfile;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Collections;
import java.util.Optional;

@Controller
public class UserController {

    private final UserProfileService userService;

    @Autowired
    public UserController(UserProfileService userService) {
        this.userService = userService;
    }
    
    @GetMapping("/userdashboard/{username}")
    public String userDashboard(Model model,
                            @PathVariable("username") String username,
                            @RequestParam(required = false) String message,
                            @RequestParam(required = false) String messageType) {
        Optional<UserProfile> profileOpt = userService.findUserDetails(username);
        if (profileOpt.isEmpty()) {
            model.addAttribute("error", "User not found");
            return "UserDashboard";
        }

        UserProfile profile = profileOpt.get();
        model.addAttribute("userProfile", profile);

        List<Trip> trips = Optional.ofNullable(userService.uploadedTripsUserProfile(profile.getId()))
                .orElseGet(Collections::emptyList);
        model.addAttribute("trips", trips);

        if (message != null) {
            model.addAttribute("message", message);
            model.addAttribute("messageType", (messageType == null || messageType.isBlank()) ? "info" : messageType);
        }

        return "UserDashboard";
    }

    // ------------------- REGISTER -------------------
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("userProfile", new UserProfile());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("userProfile") UserProfile userProfile, Model model) {
        userService.registerUser(userProfile);
        model.addAttribute("msg", "User registered successfully! Please login.");
        return "login";
    }

    // ------------------- LOGIN -------------------
    @GetMapping("/loginpage")
    public String showLogin(Model model) {
        model.addAttribute("authRequest", new AuthRequest());
        return "login";
    }

    @PostMapping("/logindata")
    public String loginUser(@ModelAttribute AuthRequest authRequest,
                            HttpSession session,
                            Model model) {
        Optional<UserProfile> userOpt = Optional.ofNullable(
                userService.login(authRequest.getUsername(), authRequest.getPassword())
        );

        if (userOpt.isEmpty()) {
            model.addAttribute("error", "Invalid username or password");
            model.addAttribute("authRequest", authRequest);
            return "login";
        }

        session.setAttribute("loggedInUser", userOpt.get());
        return "redirect:/"; // redirect to home (handled in TripController)
    }

    // ------------------- PROFILE -------------------
    @GetMapping("/profile")
    public String profilePage(HttpSession session, Model model) {
        return Optional.ofNullable((UserProfile) session.getAttribute("loggedInUser"))
                .map(user -> {
                    model.addAttribute("user", user);
                    return "profile";
                })
                .orElse("redirect:/loginpage");
    }

    // ------------------- LOGOUT -------------------
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}

