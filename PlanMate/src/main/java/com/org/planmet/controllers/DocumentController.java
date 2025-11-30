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
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/trips/{tripId}/documents")
public class DocumentController {

    private final TripService tripService;

    @Autowired
    public DocumentController(TripService tripService) {
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
    public String viewDocuments(@PathVariable Long tripId, Model model, HttpSession session) {
        UserProfile loggedInUser = (UserProfile) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/loginpage";
        }

        Optional<Trip> tripOpt = tripService.getTripById(tripId);
        if (tripOpt.isEmpty() || !tripOpt.get().getUser().getId().equals(loggedInUser.getId())) {
            return "redirect:/";
        }

        Trip trip = tripOpt.get();
        List<TripDocument> documents = trip.getDocuments().stream()
                .sorted(Comparator.comparing(TripDocument::getDocumentType))
                .collect(Collectors.toList());

        // Check for expiring documents (within 30 days)
        LocalDate thirtyDaysFromNow = LocalDate.now().plusDays(30);
        List<TripDocument> expiringDocuments = documents.stream()
                .filter(doc -> doc.getExpiryDate() != null &&
                        doc.getExpiryDate().isBefore(thirtyDaysFromNow) &&
                        doc.getExpiryDate().isAfter(LocalDate.now()))
                .collect(Collectors.toList());

        model.addAttribute("trip", trip);
        model.addAttribute("documents", documents);
        model.addAttribute("expiringDocuments", expiringDocuments);
        model.addAttribute("userProfile", loggedInUser);
        model.addAttribute("documentTypes", TripDocument.DocumentType.values());

        return "documents";
    }

    @PostMapping("/add")
    public String addDocument(@PathVariable Long tripId,
            @RequestParam String documentName,
            @RequestParam String documentType,
            @RequestParam(required = false) String documentNumber,
            @RequestParam(required = false) String issueDate,
            @RequestParam(required = false) String expiryDate,
            @RequestParam(required = false) String issuingAuthority,
            @RequestParam(required = false) String fileUrl,
            @RequestParam(required = false) Boolean isVerified,
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
        TripDocument document = new TripDocument();
        document.setTrip(trip);
        document.setDocumentName(documentName);
        document.setDocumentType(TripDocument.DocumentType.valueOf(documentType));
        document.setDocumentNumber(documentNumber);
        if (issueDate != null && !issueDate.isEmpty()) {
            document.setIssueDate(LocalDate.parse(issueDate));
        }
        if (expiryDate != null && !expiryDate.isEmpty()) {
            document.setExpiryDate(LocalDate.parse(expiryDate));
        }
        document.setIssuingAuthority(issuingAuthority);
        document.setFileUrl(fileUrl);
        document.setIsVerified(isVerified != null ? isVerified : false);
        document.setNotes(notes);

        trip.getDocuments().add(document);
        tripService.saveTrip(trip);

        redirectAttributes.addFlashAttribute("message", "Document added successfully!");
        redirectAttributes.addFlashAttribute("messageType", "success");
        return "redirect:/trips/" + tripId + "/documents";
    }

    @PostMapping("/{documentId}/edit")
    public String editDocument(@PathVariable Long tripId,
            @PathVariable Long documentId,
            @RequestParam String documentName,
            @RequestParam String documentType,
            @RequestParam(required = false) String documentNumber,
            @RequestParam(required = false) String issueDate,
            @RequestParam(required = false) String expiryDate,
            @RequestParam(required = false) String issuingAuthority,
            @RequestParam(required = false) String fileUrl,
            @RequestParam(required = false) Boolean isVerified,
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
        Optional<TripDocument> docOpt = trip.getDocuments().stream()
                .filter(d -> d.getId().equals(documentId))
                .findFirst();

        if (docOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Document not found!");
            redirectAttributes.addFlashAttribute("messageType", "error");
            return "redirect:/trips/" + tripId + "/documents";
        }

        TripDocument document = docOpt.get();
        document.setDocumentName(documentName);
        document.setDocumentType(TripDocument.DocumentType.valueOf(documentType));
        document.setDocumentNumber(documentNumber);
        if (issueDate != null && !issueDate.isEmpty()) {
            document.setIssueDate(LocalDate.parse(issueDate));
        }
        if (expiryDate != null && !expiryDate.isEmpty()) {
            document.setExpiryDate(LocalDate.parse(expiryDate));
        }
        document.setIssuingAuthority(issuingAuthority);
        document.setFileUrl(fileUrl);
        document.setIsVerified(isVerified != null ? isVerified : false);
        document.setNotes(notes);

        tripService.saveTrip(trip);

        redirectAttributes.addFlashAttribute("message", "Document updated successfully!");
        redirectAttributes.addFlashAttribute("messageType", "success");
        return "redirect:/trips/" + tripId + "/documents";
    }

    @GetMapping("/{documentId}/delete")
    public String deleteDocument(@PathVariable Long tripId,
            @PathVariable Long documentId,
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
        trip.getDocuments().removeIf(d -> d.getId().equals(documentId));
        tripService.saveTrip(trip);

        redirectAttributes.addFlashAttribute("message", "Document deleted successfully!");
        redirectAttributes.addFlashAttribute("messageType", "success");
        return "redirect:/trips/" + tripId + "/documents";
    }
}
