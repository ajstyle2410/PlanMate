package com.org.planmet.model;

import javax.persistence.*;
import java.time.LocalTime;

/**
 * Activity entity - Represents an activity or event in a day plan
 * Supports categorization, time scheduling, and priority levels
 */
@Entity
@Table(name = "activities")
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "day_plan_id", nullable = false)
    private DayPlan dayPlan;

    @Column(name = "activity_name", nullable = false, length = 200)
    private String activityName;

    @Column(name = "description", length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", length = 50)
    private ActivityCategory category;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    @Column(name = "duration_minutes")
    private Integer durationMinutes;

    @Column(name = "location", length = 300)
    private String location;

    @Column(name = "address", length = 500)
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority", length = 20)
    private Priority priority = Priority.NORMAL;

    @Column(name = "estimated_cost")
    private Double estimatedCost;

    @Column(name = "booking_required")
    private Boolean bookingRequired = false;

    @Column(name = "booking_reference", length = 100)
    private String bookingReference;

    @Column(name = "notes", length = 1000)
    private String notes;

    @Column(name = "completed")
    private Boolean completed = false;

    // Enums
    public enum ActivityCategory {
        SIGHTSEEING,
        DINING,
        ADVENTURE,
        RELAXATION,
        SHOPPING,
        ENTERTAINMENT,
        CULTURAL,
        SPORTS,
        NATURE,
        NIGHTLIFE,
        TRANSPORTATION,
        OTHER
    }

    public enum Priority {
        MUST_DO,
        HIGH,
        NORMAL,
        LOW,
        OPTIONAL,
        BACKUP
    }

    // Constructors
    public Activity() {
    }

    public Activity(String activityName, ActivityCategory category) {
        this.activityName = activityName;
        this.category = category;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DayPlan getDayPlan() {
        return dayPlan;
    }

    public void setDayPlan(DayPlan dayPlan) {
        this.dayPlan = dayPlan;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ActivityCategory getCategory() {
        return category;
    }

    public void setCategory(ActivityCategory category) {
        this.category = category;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public Double getEstimatedCost() {
        return estimatedCost;
    }

    public void setEstimatedCost(Double estimatedCost) {
        this.estimatedCost = estimatedCost;
    }

    public Boolean getBookingRequired() {
        return bookingRequired;
    }

    public void setBookingRequired(Boolean bookingRequired) {
        this.bookingRequired = bookingRequired;
    }

    public String getBookingReference() {
        return bookingReference;
    }

    public void setBookingReference(String bookingReference) {
        this.bookingReference = bookingReference;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    @Override
    public String toString() {
        return "Activity{" +
                "id=" + id +
                ", activityName='" + activityName + '\'' +
                ", category=" + category +
                ", startTime=" + startTime +
                ", priority=" + priority +
                '}';
    }
}
