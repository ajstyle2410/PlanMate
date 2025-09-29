package com.org.planmet.controllers;

import com.org.planmet.Iservice.TripService;
import com.org.planmet.Iservice.UserProfileService;
import com.org.planmet.model.Destination;
import com.org.planmet.model.Trip;
import com.org.planmet.model.UserProfile;
import com.org.planmet.model.dto.TripDto;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class TripPlanningController {

    private final TripService tripService;
    private final UserProfileService userService;

    @Autowired
    public TripPlanningController(TripService tripService, UserProfileService userService) {
        this.tripService = tripService;
        this.userService = userService;
    }

    @GetMapping("/trip_planning_details/{username}")
    public String showTripPlanningPage(@PathVariable String username, Model model) {
        Optional<UserProfile> userOpt = userService.findUserDetails(username);
        if (userOpt.isEmpty()) {
            return "redirect:/userdashboard/" + username + "?message=User not found!&messageType=error";
        }

        UserProfile user = userOpt.get();
        TripDto tripDto = new TripDto();
        tripDto.setUserId(user.getId());
        tripDto.setUploadedby(user.getUsername());

        model.addAttribute("userProfile", user);
        model.addAttribute("trip", tripDto);
        return "trip_planning_details";
    }

    @PostMapping("/trips/save")
    public String saveTrip(@ModelAttribute("trip") TripDto tripDto, RedirectAttributes redirectAttributes) {
        UserProfile user = userService.findByUserId(tripDto.getUserId());
        if (user == null) {
            Optional<UserProfile> fallback = userService.findUserDetails(tripDto.getUploadedby());
            if (fallback.isEmpty()) {
                redirectAttributes.addAttribute("message", "User not found!");
                redirectAttributes.addAttribute("messageType", "error");
                return "redirect:/loginpage";
            }
            user = fallback.get();
        }

        Trip trip = new Trip();
        trip.setTitle(tripDto.getTitle());
        trip.setDescription(tripDto.getDescription());
        trip.setBudget(tripDto.getBudget());
        trip.setStartDate(tripDto.getStartDate());
        trip.setEndDate(tripDto.getEndDate());
        trip.setUploadedby(tripDto.getUploadedby());
        trip.setUplodedDate(LocalDate.now());
        trip.setUser(user);

        List<String> destinationNames = Optional.ofNullable(tripDto.getDestinationNames())
                .orElse(Collections.emptyList())
                .stream()
                .filter(name -> name != null && !name.isBlank())
                .map(String::trim)
                .collect(Collectors.toList());

        destinationNames.forEach(name -> trip.addDestination(new Destination(name)));

        tripService.saveTrip(trip);

        redirectAttributes.addAttribute("message", "Trip added successfully!");
        redirectAttributes.addAttribute("messageType", "success");
        return "redirect:/userdashboard/" + user.getUsername();
    }
}
