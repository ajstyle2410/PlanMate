package com.org.planmet.model;

import org.junit.Test;
import static org.junit.Assert.*;

import java.time.LocalTime;

/**
 * Unit tests for Activity entity
 */
public class ActivityTest {

    @Test
    public void testActivityCreation() {
        Activity activity = new Activity("Visit Louvre", Activity.ActivityCategory.CULTURAL);

        activity.setDescription("See the Mona Lisa");
        activity.setStartTime(LocalTime.of(10, 0));
        activity.setEndTime(LocalTime.of(14, 0));
        activity.setDurationMinutes(240);
        activity.setLocation("Louvre Museum");
        activity.setPriority(Activity.Priority.MUST_DO);
        activity.setEstimatedCost(25.0);

        assertEquals("Visit Louvre", activity.getActivityName());
        assertEquals(Activity.ActivityCategory.CULTURAL, activity.getCategory());
        assertEquals("See the Mona Lisa", activity.getDescription());
        assertEquals(LocalTime.of(10, 0), activity.getStartTime());
        assertEquals(LocalTime.of(14, 0), activity.getEndTime());
        assertEquals(Integer.valueOf(240), activity.getDurationMinutes());
        assertEquals("Louvre Museum", activity.getLocation());
        assertEquals(Activity.Priority.MUST_DO, activity.getPriority());
        assertEquals(25.0, activity.getEstimatedCost(), 0.01);
    }

    @Test
    public void testActivityCategories() {
        Activity sightseeing = new Activity("Tour", Activity.ActivityCategory.SIGHTSEEING);
        Activity dining = new Activity("Lunch", Activity.ActivityCategory.DINING);
        Activity adventure = new Activity("Hiking", Activity.ActivityCategory.ADVENTURE);

        assertEquals(Activity.ActivityCategory.SIGHTSEEING, sightseeing.getCategory());
        assertEquals(Activity.ActivityCategory.DINING, dining.getCategory());
        assertEquals(Activity.ActivityCategory.ADVENTURE, adventure.getCategory());
    }

    @Test
    public void testActivityPriorities() {
        Activity mustDo = new Activity("Must See", Activity.ActivityCategory.SIGHTSEEING);
        mustDo.setPriority(Activity.Priority.MUST_DO);

        Activity optional = new Activity("If Time", Activity.ActivityCategory.SHOPPING);
        optional.setPriority(Activity.Priority.OPTIONAL);

        assertEquals(Activity.Priority.MUST_DO, mustDo.getPriority());
        assertEquals(Activity.Priority.OPTIONAL, optional.getPriority());
    }

    @Test
    public void testBookingInformation() {
        Activity activity = new Activity("Restaurant", Activity.ActivityCategory.DINING);

        activity.setBookingRequired(true);
        activity.setBookingReference("RES-12345");

        assertTrue(activity.getBookingRequired());
        assertEquals("RES-12345", activity.getBookingReference());
    }

    @Test
    public void testActivityCompletion() {
        Activity activity = new Activity("Task", Activity.ActivityCategory.OTHER);

        assertFalse(activity.getCompleted());

        activity.setCompleted(true);
        assertTrue(activity.getCompleted());
    }
}
