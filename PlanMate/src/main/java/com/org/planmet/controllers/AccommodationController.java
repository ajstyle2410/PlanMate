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
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/trips/{tripId}/accommodations")
public class AccommodationController {

    private final TripService tripService;

    @Autowired
    public AccommodationController(TripService tripService) {
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
    public String viewAccommodations(@PathVariable Long tripId, Model model, HttpSession session) {
        UserProfile loggedInUser = (UserProfile) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/loginpage";
        }

        Optional<Trip> tripOpt = tripService.getTripById(tripId);
        if (tripOpt.isEmpty() || !tripOpt.get().getUser().getId().equals(loggedInUser.getId())) {
            return "redirect:/";
        }

        Trip trip = tripOpt.get();
        List<Accommodation> accommodations = trip.getAccommodations().stream()
                .sorted(Comparator.comparing(Accommodation::getCheckInDate))
                .collect(Collectors.toList());

        model.addAttribute("trip", trip);
        model.addAttribute("accommodations", accommodations);
        model.addAttribute("userProfile", loggedInUser);
        model.addAttribute("accommodationTypes", Accommodation.AccommodationType.values());

        return "accommodations";
    }

    @PostMapping("/add")
    public String addAccommodation(@PathVariable Long tripId,
            @RequestParam String accommodationName,
            @RequestParam(required = false) String type,
            @RequestParam String checkInDate,
            @RequestParam String checkOutDate,
            @RequestParam(required = false) String checkInTime,
            @RequestParam(required = false) String checkOutTime,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String roomType,
            @RequestParam(required = false) Integer numberOfRooms,
            @RequestParam(required = false) Double costPerNight,
            @RequestParam(required = false) Double totalCost,
            @RequestParam(required = false) String bookingReference,
            @RequestParam(required = false) String confirmationNumber,
            @RequestParam(required = false) String contactPhone,
            @RequestParam(required = false) String contactEmail,
            @RequestParam(required = false) Boolean isConfirmed,
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
        Accommodation accommodation = new Accommodation();
        accommodation.setTrip(trip);
        accommodation.setAccommodationName(accommodationName);
        if (type != null && !type.isEmpty()) {
            accommodation.setType(Accommodation.AccommodationType.valueOf(type));
        }
        accommodation.setCheckInDate(LocalDate.parse(checkInDate));
        accommodation.setCheckOutDate(LocalDate.parse(checkOutDate));
        if (checkInTime != null && !checkInTime.isEmpty()) {
            accommodation.setCheckInTime(LocalTime.parse(checkInTime));
        }
        if (checkOutTime != null && !checkOutTime.isEmpty()) {
            accommodation.setCheckOutTime(LocalTime.parse(checkOutTime));
        }
        accommodation.setAddress(address);
        accommodation.setCity(city);
        accommodation.setCountry(country);
        accommodation.setRoomType(roomType);
        accommodation.setNumberOfRooms(numberOfRooms != null ? numberOfRooms : 1);
        accommodation.setCostPerNight(costPerNight);
        accommodation.setTotalCost(totalCost);
        accommodation.setBookingReference(bookingReference);
        accommodation.setConfirmationNumber(confirmationNumber);
        accommodation.setContactPhone(contactPhone);
        accommodation.setContactEmail(contactEmail);
        accommodation.setIsConfirmed(isConfirmed != null ? isConfirmed : false);
        accommodation.setNotes(notes);

        trip.getAccommodations().add(accommodation);
        tripService.saveTrip(trip);

        redirectAttributes.addFlashAttribute("message", "Accommodation added successfully!");
        redirectAttributes.addFlashAttribute("messageType", "success");
        return "redirect:/trips/" + tripId + "/accommodations";
    }

    @GetMapping("/{accommodationId}/delete")
    public String deleteAccommodation(@PathVariable Long tripId,
            @PathVariable Long accommodationId,
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
        trip.getAccommodations().removeIf(a -> a.getId().equals(accommodationId));
        tripService.saveTrip(trip);

        redirectAttributes.addFlashAttribute("message", "Accommodation deleted successfully!");
        redirectAttributes.addFlashAttribute("messageType", "success");
        return "redirect:/trips/" + tripId + "/accommodations";
    }
}
