package com.org.planmet.controllers;

import com.org.planmet.Iservice.TripService;
import com.org.planmet.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/trips/{tripId}/itinerary")
public class DayPlanController {

    private final TripService tripService;

    @Autowired
    public DayPlanController(TripService tripService) {
        this.tripService = tripService;
    }

    @ModelAttribute
    public void addLoggedInUser(HttpSession session, Model model) {
        UserProfile loggedInUser = (UserProfile) session.getAttribute("loggedInUser");
        if (loggedInUser != null) {
            model.addAttribute("loggedInUser", loggedInUser);
        }
    }

    @GetMapping
    public String viewItinerary(@PathVariable Long tripId, Model model, HttpSession session) {
        UserProfile loggedInUser = (UserProfile) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/loginpage";
        }

        Optional<Trip> tripOpt = tripService.getTripById(tripId);
        if (tripOpt.isEmpty() || !tripOpt.get().getUser().getId().equals(loggedInUser.getId())) {
            return "redirect:/";
        }

        Trip trip = tripOpt.get();
        List<DayPlan> dayPlans = trip.getDayPlans().stream()
                .sorted(Comparator.comparing(DayPlan::getDayNumber))
                .collect(Collectors.toList());

        model.addAttribute("trip", trip);
        model.addAttribute("dayPlans", dayPlans);
        model.addAttribute("userProfile", loggedInUser);

        return "itinerary";
    }

    @PostMapping("/add")
    public String addDayPlan(@PathVariable Long tripId,
            @RequestParam Integer dayNumber,
            @RequestParam String planDate,
            @RequestParam(required = false) String dayTitle,
            @RequestParam(required = false) String notes,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        UserProfile loggedInUser = (UserProfile) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/loginpage";
        }

        Optional<Trip> tripOpt = tripService.getTripById(tripId);
        if (tripOpt.isEmpty() || !tripOpt.get().getUser().getId().equals(loggedInUser.getId())) {
            return "redirect:/";
        }

        Trip trip = tripOpt.get();
        DayPlan dayPlan = new DayPlan();
        dayPlan.setTrip(trip);
        dayPlan.setDayNumber(dayNumber);
        dayPlan.setPlanDate(LocalDate.parse(planDate));
        dayPlan.setDayTitle(dayTitle);
        dayPlan.setNotes(notes);

        trip.getDayPlans().add(dayPlan);
        tripService.saveTrip(trip);

        redirectAttributes.addFlashAttribute("message", "Day plan added successfully!");
        redirectAttributes.addFlashAttribute("messageType", "success");
        return "redirect:/trips/" + tripId + "/itinerary";
    }

    @PostMapping("/{dayPlanId}/activities/add")
    public String addActivity(@PathVariable Long tripId,
            @PathVariable Long dayPlanId,
            @RequestParam String activityName,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String priority,
            @RequestParam(required = false) Double estimatedCost,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        UserProfile loggedInUser = (UserProfile) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/loginpage";
        }

        Optional<Trip> tripOpt = tripService.getTripById(tripId);
        if (tripOpt.isEmpty() || !tripOpt.get().getUser().getId().equals(loggedInUser.getId())) {
            return "redirect:/";
        }

        Trip trip = tripOpt.get();
        Optional<DayPlan> dayPlanOpt = trip.getDayPlans().stream()
                .filter(dp -> dp.getId().equals(dayPlanId))
                .findFirst();

        if (dayPlanOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Day plan not found!");
            redirectAttributes.addFlashAttribute("messageType", "error");
            return "redirect:/trips/" + tripId + "/itinerary";
        }

        DayPlan dayPlan = dayPlanOpt.get();
        Activity activity = new Activity();
        activity.setActivityName(activityName);
        activity.setDescription(description);
        if (category != null && !category.isEmpty()) {
            activity.setCategory(Activity.ActivityCategory.valueOf(category));
        }
        if (startTime != null && !startTime.isEmpty()) {
            activity.setStartTime(java.time.LocalTime.parse(startTime));
        }
        if (endTime != null && !endTime.isEmpty()) {
            activity.setEndTime(java.time.LocalTime.parse(endTime));
        }
        activity.setLocation(location);
        if (priority != null && !priority.isEmpty()) {
            activity.setPriority(Activity.Priority.valueOf(priority));
        }
        activity.setEstimatedCost(estimatedCost);

        dayPlan.addActivity(activity);
        tripService.saveTrip(trip);

        redirectAttributes.addFlashAttribute("message", "Activity added successfully!");
        redirectAttributes.addFlashAttribute("messageType", "success");
        return "redirect:/trips/" + tripId + "/itinerary";
    }

    @GetMapping("/{dayPlanId}/delete")
    public String deleteDayPlan(@PathVariable Long tripId,
            @PathVariable Long dayPlanId,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        UserProfile loggedInUser = (UserProfile) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/loginpage";
        }

        Optional<Trip> tripOpt = tripService.getTripById(tripId);
        if (tripOpt.isEmpty() || !tripOpt.get().getUser().getId().equals(loggedInUser.getId())) {
            return "redirect:/";
        }

        Trip trip = tripOpt.get();
        trip.getDayPlans().removeIf(dp -> dp.getId().equals(dayPlanId));
        tripService.saveTrip(trip);

        redirectAttributes.addFlashAttribute("message", "Day plan deleted successfully!");
        redirectAttributes.addFlashAttribute("messageType", "success");
        return "redirect:/trips/" + tripId + "/itinerary";
    }

    @PostMapping("/activities/{activityId}/delete")
    public String deleteActivity(@PathVariable Long tripId,
            @PathVariable Long activityId,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        UserProfile loggedInUser = (UserProfile) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/loginpage";
        }

        Optional<Trip> tripOpt = tripService.getTripById(tripId);
        if (tripOpt.isEmpty() || !tripOpt.get().getUser().getId().equals(loggedInUser.getId())) {
            return "redirect:/";
        }

        Trip trip = tripOpt.get();
        trip.getDayPlans()
                .forEach(dayPlan -> dayPlan.getActivities().removeIf(activity -> activity.getId().equals(activityId)));
        tripService.saveTrip(trip);

        redirectAttributes.addFlashAttribute("message", "Activity deleted successfully!");
        redirectAttributes.addFlashAttribute("messageType", "success");
        return "redirect:/trips/" + tripId + "/itinerary";
    }
}
