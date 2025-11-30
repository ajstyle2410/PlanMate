package com.org.planmet.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * DayPlan entity - Represents a single day in a trip itinerary
 * Allows users to plan activities day by day
 */
@Entity
@Table(name = "day_plans")
public class DayPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    @Column(name = "day_number", nullable = false)
    private Integer dayNumber;

    @Column(name = "plan_date", nullable = false)
    private LocalDate planDate;

    @Column(name = "day_title", length = 200)
    private String dayTitle;

    @Column(name = "notes", length = 1000)
    private String notes;

    @OneToMany(mappedBy = "dayPlan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Activity> activities = new ArrayList<>();

    // Constructors
    public DayPlan() {
    }

    public DayPlan(Trip trip, Integer dayNumber, LocalDate planDate) {
        this.trip = trip;
        this.dayNumber = dayNumber;
        this.planDate = planDate;
    }

    // Convenience methods
    public void addActivity(Activity activity) {
        if (activity != null) {
            activities.add(activity);
            activity.setDayPlan(this);
        }
    }

    public void removeActivity(Activity activity) {
        if (activity != null) {
            activities.remove(activity);
            activity.setDayPlan(null);
        }
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public Integer getDayNumber() {
        return dayNumber;
    }

    public void setDayNumber(Integer dayNumber) {
        this.dayNumber = dayNumber;
    }

    public LocalDate getPlanDate() {
        return planDate;
    }

    public void setPlanDate(LocalDate planDate) {
        this.planDate = planDate;
    }

    public String getDayTitle() {
        return dayTitle;
    }

    public void setDayTitle(String dayTitle) {
        this.dayTitle = dayTitle;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<Activity> getActivities() {
        return activities;
    }

    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }

    @Override
    public String toString() {
        return "DayPlan{" +
                "id=" + id +
                ", dayNumber=" + dayNumber +
                ", planDate=" + planDate +
                ", dayTitle='" + dayTitle + '\'' +
                '}';
    }
}
