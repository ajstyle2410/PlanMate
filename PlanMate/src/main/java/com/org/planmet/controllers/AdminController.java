package com.org.planmet.controllers;

import com.org.planmet.Iservice.TripService;
import com.org.planmet.Iservice.UserProfileService;
import com.org.planmet.model.Trip;
import com.org.planmet.model.UserProfile;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserProfileService userService;
    private final TripService tripService;

    @Autowired
    public AdminController(UserProfileService userService, TripService tripService) {
        this.userService = userService;
        this.tripService = tripService;
    }

    @GetMapping
    public String dashboard(Model model, HttpSession session) {
        if (session.getAttribute("adminUser") == null) {
            return "redirect:/admin/login";
        }
        List<UserProfile> users = userService.viewAllUsers().orElseGet(Collections::emptyList);
        List<Trip> trips = Optional.ofNullable(tripService.getAllTrips())
                .orElseGet(Collections::emptyList);

        long activeTrips = trips.stream()
                .filter(trip -> trip.getStatus() == Trip.Status.ACTIVE)
                .count();
        long draftTrips = trips.stream()
                .filter(trip -> trip.getStatus() == Trip.Status.DRAFT)
                .count();
        long completedTrips = trips.stream()
                .filter(trip -> trip.getStatus() == Trip.Status.COMPLETED)
                .count();

        List<Trip> recentTrips = trips.stream()
                .sorted(Comparator.comparing(Trip::getUplodedDate, Comparator.nullsLast(Comparator.reverseOrder()))
                        .thenComparing(Trip::getStartDate, Comparator.nullsLast(Comparator.reverseOrder())))
                .limit(5)
                .collect(Collectors.toList());

        Map<UserProfile, Long> topContributors = trips.stream()
                .filter(trip -> trip.getUser() != null)
                .collect(Collectors.groupingBy(Trip::getUser, Collectors.counting()))
                .entrySet()
                .stream()
                .sorted(Map.Entry.<UserProfile, Long>comparingByValue().reversed())
                .limit(5)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (existing, replacement) -> existing, LinkedHashMap::new));

        model.addAttribute("userCount", users.size());
        model.addAttribute("tripCount", trips.size());
        model.addAttribute("activeTripCount", activeTrips);
        model.addAttribute("draftTripCount", draftTrips);
        model.addAttribute("completedTripCount", completedTrips);
        model.addAttribute("recentTrips", recentTrips);
        model.addAttribute("topContributors", topContributors);

        return "admin/dashboard";
    }

    @GetMapping("/users")
    public String users(Model model, HttpSession session) {
        if (session.getAttribute("adminUser") == null) {
            return "redirect:/admin/login";
        }
        List<UserProfile> users = userService.viewAllUsers().orElseGet(Collections::emptyList);
        model.addAttribute("users", users);
        return "admin/users";
    }

        @GetMapping("/progress")
    public String progress(Model model, HttpSession session) {
        if (session.getAttribute("adminUser") == null) {
            return "redirect:/admin/login";
        }

        List<Trip> trips = Optional.ofNullable(tripService.getAllTrips())
                .orElseGet(Collections::emptyList);

        Map<Trip.Status, Long> statusCounts = trips.stream()
                .collect(Collectors.groupingBy(Trip::getStatus, Collectors.counting()));

        Map<YearMonth, Long> tripsPerMonth = new TreeMap<>();
        trips.stream()
                .map(Trip::getStartDate)
                .filter(date -> date != null)
                .map(YearMonth::from)
                .forEach(month -> tripsPerMonth.merge(month, 1L, Long::sum));

        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMM yyyy");
        List<String> monthLabels = tripsPerMonth.keySet().stream()
                .map(monthFormatter::format)
                .collect(Collectors.toList());
        List<Long> monthValues = tripsPerMonth.values().stream().collect(Collectors.toList());

        model.addAttribute("statusCounts", statusCounts);
        model.addAttribute("monthLabels", monthLabels);
        model.addAttribute("monthValues", monthValues);
        model.addAttribute("totalTrips", trips.size());

        return "admin/progress";
    }
@GetMapping("/trips")
    public String trips(Model model, HttpSession session) {
        if (session.getAttribute("adminUser") == null) {
            return "redirect:/admin/login";
        }
        List<Trip> trips = Optional.ofNullable(tripService.getAllTrips())
                .orElseGet(Collections::emptyList);

        Map<Trip.Status, Long> statusCounts = trips.stream()
                .collect(Collectors.groupingBy(Trip::getStatus, Collectors.counting()));

        model.addAttribute("trips", trips);
        model.addAttribute("statusCounts", statusCounts);
        return "admin/trips";
    }
}







