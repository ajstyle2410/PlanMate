package com.org.planmet.controllers;

import com.org.planmet.Iservice.TripService;
import com.org.planmet.Iservice.UserProfileService;
import com.org.planmet.model.Trip;
import com.org.planmet.model.UserProfile;
import com.org.planmet.model.dto.TripDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/trips")
public class TripController {

	private final TripService tripService;
	private final UserProfileService userService;

	@Autowired
	public TripController(TripService tripService, UserProfileService userService) {
		this.tripService = tripService;
		this.userService = userService;
	}

	// ------------------- HOME PAGE -------------------
	@GetMapping("/")
	public String home(HttpSession session, Model model) {
		System.out.println("*************");
		List<Trip> trips = tripService.getAllTrips();
		model.addAttribute("trips", trips);

		UserProfile user = (UserProfile) session.getAttribute("loggedInUser");
		if (user != null) {
			model.addAttribute("loggedInUser", user);
		}

		return "home";
	}

	// ------------------- USER DASHBOARD -------------------
	@GetMapping("/dashboard/{username}")
	public String userDashboard(@PathVariable String username, @RequestParam(required = false) String message,
			@RequestParam(required = false) String messageType, Model model) {

		Optional<UserProfile> userOpt = userService.findUserDetails(username);
		if (userOpt.isEmpty()) {
			model.addAttribute("message", "User not found!");
			model.addAttribute("messageType", "error");
			return "UserDashboard";
		}

		UserProfile user = userOpt.get();
		model.addAttribute("userProfile", user);

		List<Trip> trips = Optional.ofNullable(tripService.getTripsByUserId(user.getId()))
				.orElseGet(Collections::emptyList);
		model.addAttribute("trips", trips);

		if (message != null) {
			model.addAttribute("message", message);
			model.addAttribute("messageType", messageType);
		}

		return "UserDashboard";
	}

	// ------------------- ADD TRIP -------------------
	@PostMapping("/dashboard/{username}/add")
	public String addTrip(@PathVariable String username, @ModelAttribute TripDto tripDto) {

		Optional<UserProfile> userOpt = userService.findUserDetails(username);
		if (userOpt.isEmpty()) {
			return "redirect:/userdashboard/" + username + "?message=User not found!&messageType=error";
		}

		UserProfile user = userOpt.get();
		Trip trip = new Trip();
		trip.setTitle(tripDto.getTitle());
		trip.setDescription(tripDto.getDescription());
		trip.setBudget(tripDto.getBudget());
		trip.setStartDate(tripDto.getStartDate());
		trip.setEndDate(tripDto.getEndDate());
		trip.setUploadedby(username);
		trip.setUplodedDate(LocalDate.now());
		trip.setUser(user);

		tripService.saveTrip(trip);
		return "redirect:/userdashboard/" + username + "?message=Trip added successfully!&messageType=success";
	}

	@GetMapping("/create/{id}")
	public String showCreateTripForm(Model model, @PathVariable("id") long id) {
		Trip tripdetails = tripService.getTripById(id).get();
		if (tripdetails==null) {
			return "redirect:/";
		}
		model.addAttribute("trip", tripdetails);
		return "trip-details";
	}

	// ------------------- EDIT TRIP -------------------
	@PostMapping("/dashboard/{username}/edit")
	public String editTrip(@PathVariable String username, @ModelAttribute Trip trip) {
		Optional<UserProfile> userOpt = userService.findUserDetails(username);
		if (userOpt.isEmpty()) {
			return "redirect:/userdashboard/" + username + "?message=User not found!&messageType=error";
		}

		trip.setUser(userOpt.get());
		long updatedId = tripService.updateTrip(trip);
		if (updatedId != 0) {
			return "redirect:/userdashboard/" + username + "?message=Trip updated successfully!&messageType=success";
		}

		return "redirect:/userdashboard/" + username + "?message=Failed to update trip!&messageType=error";
	}

	// ------------------- DELETE TRIP -------------------
	@GetMapping("/dashboard/{username}/delete/{id}")
	public String deleteTrip(@PathVariable String username, @PathVariable Long id) {
		tripService.deleteTrip(id);
		return "redirect:/userdashboard/" + username + "?message=Trip deleted successfully!&messageType=success";
	}

	// ------------------- VIEW SINGLE TRIP -------------------
	@GetMapping("/view/{id}")
	public String viewTrip(@PathVariable Long id, Model model) {
		Optional<Trip> tripOpt = tripService.getTripById(id);
		if (tripOpt.isPresent()) {
			model.addAttribute("trip", tripOpt.get());
			return "TripView";
		}
		model.addAttribute("message", "Trip not found!");
		model.addAttribute("messageType", "error");
		return "redirect:/";
	}

	// ------------------- API ENDPOINTS -------------------
	@GetMapping("/api")
	@ResponseBody
	public ResponseEntity<List<Trip>> getTripsApi() {
		return ResponseEntity.ok(tripService.getAllTrips());
	}

	@GetMapping("/api/{id}")
	@ResponseBody
	public ResponseEntity<Trip> getTripByIdApi(@PathVariable Long id) {
		return tripService.getTripById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	@DeleteMapping("/api/{id}")
	@ResponseBody
	public ResponseEntity<String> deleteTripApi(@PathVariable Long id) {
		tripService.deleteTrip(id);
		return ResponseEntity.ok("Trip deleted successfully");
	}
}
