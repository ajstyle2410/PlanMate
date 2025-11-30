package com.org.planmet.controllers;

import com.org.planmet.Iservice.TripService;
import com.org.planmet.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/trips/{tripId}/budget")
public class ExpenseController {

    private final TripService tripService;

    @Autowired
    public ExpenseController(TripService tripService) {
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
    public String viewBudget(@PathVariable Long tripId, Model model, HttpSession session) {
        UserProfile loggedInUser = (UserProfile) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/loginpage";
        }

        Optional<Trip> tripOpt = tripService.getTripById(tripId);
        if (tripOpt.isEmpty() || !tripOpt.get().getUser().getId().equals(loggedInUser.getId())) {
            return "redirect:/";
        }

        Trip trip = tripOpt.get();
        List<Expense> expenses = trip.getExpenses();

        // Calculate totals
        double totalExpenses = expenses.stream()
                .mapToDouble(Expense::getAmount)
                .sum();

        double plannedExpenses = expenses.stream()
                .filter(e -> e.getIsPlanned() != null && e.getIsPlanned())
                .mapToDouble(Expense::getAmount)
                .sum();

        double actualExpenses = expenses.stream()
                .filter(e -> e.getIsPlanned() == null || !e.getIsPlanned())
                .mapToDouble(Expense::getAmount)
                .sum();

        // Group by category
        Map<Expense.ExpenseCategory, Double> categoryTotals = expenses.stream()
                .collect(Collectors.groupingBy(
                        Expense::getCategory,
                        Collectors.summingDouble(Expense::getAmount)));

        model.addAttribute("trip", trip);
        model.addAttribute("expenses", expenses);
        model.addAttribute("totalExpenses", totalExpenses);
        model.addAttribute("plannedExpenses", plannedExpenses);
        model.addAttribute("actualExpenses", actualExpenses);
        model.addAttribute("categoryTotals", categoryTotals);
        model.addAttribute("userProfile", loggedInUser);
        model.addAttribute("categories", Expense.ExpenseCategory.values());

        return "budget";
    }

    @PostMapping("/expense/add")
    public String addExpense(@PathVariable Long tripId,
            @RequestParam String expenseName,
            @RequestParam String category,
            @RequestParam Double amount,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String currency,
            @RequestParam(required = false) String paymentMethod,
            @RequestParam(required = false) String paidBy,
            @RequestParam(required = false) Boolean isPlanned,
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
        Expense expense = new Expense();
        expense.setTrip(trip);
        expense.setExpenseName(expenseName);
        expense.setCategory(Expense.ExpenseCategory.valueOf(category));
        expense.setAmount(amount);
        expense.setDescription(description);
        expense.setCurrency(currency != null ? currency : "USD");
        expense.setPaymentMethod(paymentMethod);
        expense.setPaidBy(paidBy);
        expense.setIsPlanned(isPlanned != null ? isPlanned : false);
        expense.setNotes(notes);
        expense.setExpenseDate(LocalDateTime.now());

        trip.getExpenses().add(expense);
        tripService.saveTrip(trip);

        redirectAttributes.addFlashAttribute("message", "Expense added successfully!");
        redirectAttributes.addFlashAttribute("messageType", "success");
        return "redirect:/trips/" + tripId + "/budget";
    }

    @PostMapping("/expense/{expenseId}/edit")
    public String editExpense(@PathVariable Long tripId,
            @PathVariable Long expenseId,
            @RequestParam String expenseName,
            @RequestParam String category,
            @RequestParam Double amount,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String currency,
            @RequestParam(required = false) String paymentMethod,
            @RequestParam(required = false) String paidBy,
            @RequestParam(required = false) Boolean isPlanned,
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
        Optional<Expense> expenseOpt = trip.getExpenses().stream()
                .filter(e -> e.getId().equals(expenseId))
                .findFirst();

        if (expenseOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Expense not found!");
            redirectAttributes.addFlashAttribute("messageType", "error");
            return "redirect:/trips/" + tripId + "/budget";
        }

        Expense expense = expenseOpt.get();
        expense.setExpenseName(expenseName);
        expense.setCategory(Expense.ExpenseCategory.valueOf(category));
        expense.setAmount(amount);
        expense.setDescription(description);
        expense.setCurrency(currency);
        expense.setPaymentMethod(paymentMethod);
        expense.setPaidBy(paidBy);
        expense.setIsPlanned(isPlanned != null ? isPlanned : false);
        expense.setNotes(notes);

        tripService.saveTrip(trip);

        redirectAttributes.addFlashAttribute("message", "Expense updated successfully!");
        redirectAttributes.addFlashAttribute("messageType", "success");
        return "redirect:/trips/" + tripId + "/budget";
    }

    @GetMapping("/expense/{expenseId}/delete")
    public String deleteExpense(@PathVariable Long tripId,
            @PathVariable Long expenseId,
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
        trip.getExpenses().removeIf(e -> e.getId().equals(expenseId));
        tripService.saveTrip(trip);

        redirectAttributes.addFlashAttribute("message", "Expense deleted successfully!");
        redirectAttributes.addFlashAttribute("messageType", "success");
        return "redirect:/trips/" + tripId + "/budget";
    }
}
