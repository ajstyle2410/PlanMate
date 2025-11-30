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
@RequestMapping("/trips/{tripId}/transportation")
public class TransportationController {

    private final TripService tripService;

    @Autowired
    public TransportationController(TripService tripService) {
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
    public String viewTransportation(@PathVariable Long tripId, Model model, HttpSession session) {
        UserProfile loggedInUser = (UserProfile) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/loginpage";
        }

        Optional<Trip> tripOpt = tripService.getTripById(tripId);
        if (tripOpt.isEmpty() || !tripOpt.get().getUser().getId().equals(loggedInUser.getId())) {
            return "redirect:/";
        }

        Trip trip = tripOpt.get();
        List<Transportation> transportations = trip.getTransportations().stream()
                .sorted(Comparator.comparing(Transportation::getDepartureDate))
                .collect(Collectors.toList());

        model.addAttribute("trip", trip);
        model.addAttribute("transportations", transportations);
        model.addAttribute("userProfile", loggedInUser);
        model.addAttribute("transportTypes", Transportation.TransportType.values());

        return "transportation";
    }

    @PostMapping("/add")
    public String addTransportation(@PathVariable Long tripId,
            @RequestParam String transportType,
            @RequestParam String departureLocation,
            @RequestParam String arrivalLocation,
            @RequestParam String departureDate,
            @RequestParam(required = false) String departureTime,
            @RequestParam(required = false) String arrivalDate,
            @RequestParam(required = false) String arrivalTime,
            @RequestParam(required = false) String transportName,
            @RequestParam(required = false) String provider,
            @RequestParam(required = false) String bookingReference,
            @RequestParam(required = false) String confirmationNumber,
            @RequestParam(required = false) String flightNumber,
            @RequestParam(required = false) String seatNumber,
            @RequestParam(required = false) String classType,
            @RequestParam(required = false) Double cost,
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
        Transportation transportation = new Transportation();
        transportation.setTrip(trip);
        transportation.setTransportType(Transportation.TransportType.valueOf(transportType));
        transportation.setDepartureLocation(departureLocation);
        transportation.setArrivalLocation(arrivalLocation);
        transportation.setDepartureDate(LocalDate.parse(departureDate));
        if (departureTime != null && !departureTime.isEmpty()) {
            transportation.setDepartureTime(LocalTime.parse(departureTime));
        }
        if (arrivalDate != null && !arrivalDate.isEmpty()) {
            transportation.setArrivalDate(LocalDate.parse(arrivalDate));
        }
        if (arrivalTime != null && !arrivalTime.isEmpty()) {
            transportation.setArrivalTime(LocalTime.parse(arrivalTime));
        }
        transportation.setTransportName(transportName);
        transportation.setProvider(provider);
        transportation.setBookingReference(bookingReference);
        transportation.setConfirmationNumber(confirmationNumber);
        transportation.setFlightNumber(flightNumber);
        transportation.setSeatNumber(seatNumber);
        transportation.setClassType(classType);
        transportation.setCost(cost);
        transportation.setIsConfirmed(isConfirmed != null ? isConfirmed : false);
        transportation.setNotes(notes);

        trip.getTransportations().add(transportation);
        tripService.saveTrip(trip);

        redirectAttributes.addFlashAttribute("message", "Transportation added successfully!");
        redirectAttributes.addFlashAttribute("messageType", "success");
        return "redirect:/trips/" + tripId + "/transportation";
    }

    @GetMapping("/{transportationId}/delete")
    public String deleteTransportation(@PathVariable Long tripId,
            @PathVariable Long transportationId,
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
        trip.getTransportations().removeIf(t -> t.getId().equals(transportationId));
        tripService.saveTrip(trip);

        redirectAttributes.addFlashAttribute("message", "Transportation deleted successfully!");
        redirectAttributes.addFlashAttribute("messageType", "success");
        return "redirect:/trips/" + tripId + "/transportation";
    }
}
