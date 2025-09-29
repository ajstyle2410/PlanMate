package com.org.planmet.model.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

public class TripDto {

    private String title;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    private Double budget;

    private String description;

    private String Uploadedby;

    private long userId;

    private List<String> destinationNames = new ArrayList<>();

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Double getBudget() {
        return budget;
    }

    public void setBudget(Double budget) {
        this.budget = budget;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUploadedby() {
        return Uploadedby;
    }

    public void setUploadedby(String uploadedby) {
        Uploadedby = uploadedby;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public List<String> getDestinationNames() {
        return destinationNames;
    }

    public void setDestinationNames(List<String> destinationNames) {
        this.destinationNames = destinationNames != null ? destinationNames : new ArrayList<>();
    }

    @Override
    public String toString() {
        return "TripDto [title=" + title + ", startDate=" + startDate + ", endDate=" + endDate + ", budget=" + budget
                + ", description=" + description + ", destinationNames=" + destinationNames + "]";
    }
}