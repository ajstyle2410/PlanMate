package com.org.planmet.model;

import org.junit.Test;
import static org.junit.Assert.*;

import java.time.LocalDate;

/**
 * Unit tests for DayPlan entity
 */
public class DayPlanTest {

    @Test
    public void testDayPlanCreation() {
        Trip trip = new Trip();
        DayPlan dayPlan = new DayPlan(trip, 1, LocalDate.of(2025, 7, 1));

        dayPlan.setDayTitle("Arrival Day");
        dayPlan.setNotes("Check-in at hotel");

        assertEquals(Integer.valueOf(1), dayPlan.getDayNumber());
        assertEquals(LocalDate.of(2025, 7, 1), dayPlan.getPlanDate());
        assertEquals("Arrival Day", dayPlan.getDayTitle());
        assertEquals("Check-in at hotel", dayPlan.getNotes());
        assertEquals(trip, dayPlan.getTrip());
    }

    @Test
    public void testAddActivity() {
        DayPlan dayPlan = new DayPlan();
        Activity activity = new Activity("Visit Eiffel Tower", Activity.ActivityCategory.SIGHTSEEING);

        dayPlan.addActivity(activity);

        assertEquals(1, dayPlan.getActivities().size());
        assertEquals(dayPlan, activity.getDayPlan());
    }

    @Test
    public void testRemoveActivity() {
        DayPlan dayPlan = new DayPlan();
        Activity activity = new Activity("Visit Eiffel Tower", Activity.ActivityCategory.SIGHTSEEING);

        dayPlan.addActivity(activity);
        dayPlan.removeActivity(activity);

        assertEquals(0, dayPlan.getActivities().size());
        assertNull(activity.getDayPlan());
    }

    @Test
    public void testMultipleActivities() {
        DayPlan dayPlan = new DayPlan();

        Activity activity1 = new Activity("Breakfast", Activity.ActivityCategory.DINING);
        Activity activity2 = new Activity("Museum Visit", Activity.ActivityCategory.CULTURAL);
        Activity activity3 = new Activity("Dinner", Activity.ActivityCategory.DINING);

        dayPlan.addActivity(activity1);
        dayPlan.addActivity(activity2);
        dayPlan.addActivity(activity3);

        assertEquals(3, dayPlan.getActivities().size());
    }
}
