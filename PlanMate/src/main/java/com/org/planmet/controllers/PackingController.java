package com.org.planmet.controllers;

import com.org.planmet.Iservice.TripService;
import com.org.planmet.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/trips/{tripId}/packing")
public class PackingController {

    private final TripService tripService;

    @Autowired
    public PackingController(TripService tripService) {
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
    public String viewPackingList(@PathVariable Long tripId, Model model, HttpSession session) {
        UserProfile loggedInUser = (UserProfile) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/loginpage";
        }

        Optional<Trip> tripOpt = tripService.getTripById(tripId);
        if (tripOpt.isEmpty() || !tripOpt.get().getUser().getId().equals(loggedInUser.getId())) {
            return "redirect:/";
        }

        Trip trip = tripOpt.get();
        List<PackingItem> packingItems = trip.getPackingItems();

        // Group by category
        Map<PackingItem.PackingCategory, List<PackingItem>> itemsByCategory = packingItems.stream()
                .collect(Collectors.groupingBy(PackingItem::getCategory));

        // Calculate progress
        long totalItems = packingItems.size();
        long packedItems = packingItems.stream()
                .filter(item -> item.getIsPacked() != null && item.getIsPacked())
                .count();
        double progress = totalItems > 0 ? (packedItems * 100.0 / totalItems) : 0;

        model.addAttribute("trip", trip);
        model.addAttribute("packingItems", packingItems);
        model.addAttribute("itemsByCategory", itemsByCategory);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("packedItems", packedItems);
        model.addAttribute("progress", progress);
        model.addAttribute("userProfile", loggedInUser);
        model.addAttribute("categories", PackingItem.PackingCategory.values());
        model.addAttribute("priorities", PackingItem.Priority.values());

        return "packing";
    }

    @PostMapping("/add")
    public String addPackingItem(@PathVariable Long tripId,
            @RequestParam String itemName,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Integer quantity,
            @RequestParam(required = false) String priority,
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
        PackingItem item = new PackingItem();
        item.setTrip(trip);
        item.setItemName(itemName);
        if (category != null && !category.isEmpty()) {
            item.setCategory(PackingItem.PackingCategory.valueOf(category));
        }
        item.setQuantity(quantity != null ? quantity : 1);
        if (priority != null && !priority.isEmpty()) {
            item.setPriority(PackingItem.Priority.valueOf(priority));
        }
        item.setNotes(notes);
        item.setIsPacked(false);

        trip.getPackingItems().add(item);
        tripService.saveTrip(trip);

        redirectAttributes.addFlashAttribute("message", "Packing item added successfully!");
        redirectAttributes.addFlashAttribute("messageType", "success");
        return "redirect:/trips/" + tripId + "/packing";
    }

    @PostMapping("/{itemId}/toggle")
    public String togglePackedStatus(@PathVariable Long tripId,
            @PathVariable Long itemId,
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
        Optional<PackingItem> itemOpt = trip.getPackingItems().stream()
                .filter(item -> item.getId().equals(itemId))
                .findFirst();

        if (itemOpt.isPresent()) {
            PackingItem item = itemOpt.get();
            item.setIsPacked(!item.getIsPacked());
            tripService.saveTrip(trip);
        }

        return "redirect:/trips/" + tripId + "/packing";
    }

    @GetMapping("/{itemId}/delete")
    public String deletePackingItem(@PathVariable Long tripId,
            @PathVariable Long itemId,
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
        trip.getPackingItems().removeIf(item -> item.getId().equals(itemId));
        tripService.saveTrip(trip);

        redirectAttributes.addFlashAttribute("message", "Packing item deleted successfully!");
        redirectAttributes.addFlashAttribute("messageType", "success");
        return "redirect:/trips/" + tripId + "/packing";
    }
}
