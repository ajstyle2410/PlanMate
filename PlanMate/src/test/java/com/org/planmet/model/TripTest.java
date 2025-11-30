package com.org.planmet.model;

import org.junit.Test;
import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Unit tests for Trip entity
 */
public class TripTest {

    @Test
    public void testTripCreation() {
        Trip trip = new Trip();
        trip.setTitle("Summer Vacation");
        trip.setDescription("Beach holiday");
        trip.setBudget(5000.0);
        trip.setStartDate(LocalDate.of(2025, 7, 1));
        trip.setEndDate(LocalDate.of(2025, 7, 15));
        trip.setStatus(Trip.Status.DRAFT);

        assertEquals("Summer Vacation", trip.getTitle());
        assertEquals("Beach holiday", trip.getDescription());
        assertEquals(5000.0, trip.getBudget(), 0.01);
        assertEquals(Trip.Status.DRAFT, trip.getStatus());
    }

    @Test
    public void testAddDestination() {
        Trip trip = new Trip();
        Destination destination = new Destination("Paris");

        trip.addDestination(destination);

        assertEquals(1, trip.getDestinations().size());
        assertEquals(trip, destination.getTrip());
    }

    @Test
    public void testRemoveDestination() {
        Trip trip = new Trip();
        Destination destination = new Destination("Paris");

        trip.addDestination(destination);
        trip.removeDestination(destination);

        assertEquals(0, trip.getDestinations().size());
        assertNull(destination.getTrip());
    }

    @Test
    public void testTripVisibility() {
        Trip trip = new Trip();
        trip.setVisibility(Trip.Visibility.PRIVATE);

        assertEquals(Trip.Visibility.PRIVATE, trip.getVisibility());
    }

    @Test
    public void testTripStatus() {
        Trip trip = new Trip();

        // Default status should be DRAFT
        assertEquals(Trip.Status.DRAFT, trip.getStatus());

        trip.setStatus(Trip.Status.ACTIVE);
        assertEquals(Trip.Status.ACTIVE, trip.getStatus());

        trip.setStatus(Trip.Status.COMPLETED);
        assertEquals(Trip.Status.COMPLETED, trip.getStatus());
    }

    @Test
    public void testTripEquality() {
        Trip trip1 = new Trip();
        trip1.setTripId(1L);

        Trip trip2 = new Trip();
        trip2.setTripId(1L);

        Trip trip3 = new Trip();
        trip3.setTripId(2L);

        assertEquals(trip1, trip2);
        assertNotEquals(trip1, trip3);
    }

    @Test
    public void testTripHashCode() {
        Trip trip1 = new Trip();
        trip1.setTripId(1L);

        Trip trip2 = new Trip();
        trip2.setTripId(1L);

        assertEquals(trip1.hashCode(), trip2.hashCode());
    }
}
