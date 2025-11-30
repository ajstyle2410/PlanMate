package com.org.planmet.model;

import org.junit.Test;
import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Unit tests for Accommodation entity
 */
public class AccommodationTest {

    @Test
    public void testAccommodationCreation() {
        LocalDate checkIn = LocalDate.of(2025, 7, 1);
        LocalDate checkOut = LocalDate.of(2025, 7, 5);

        Accommodation accommodation = new Accommodation("Grand Hotel", checkIn, checkOut);

        accommodation.setType(Accommodation.AccommodationType.HOTEL);
        accommodation.setAddress("123 Main St");
        accommodation.setCity("Paris");
        accommodation.setCountry("France");
        accommodation.setNumberOfNights(4);
        accommodation.setCostPerNight(150.0);
        accommodation.setTotalCost(600.0);

        assertEquals("Grand Hotel", accommodation.getAccommodationName());
        assertEquals(Accommodation.AccommodationType.HOTEL, accommodation.getType());
        assertEquals(checkIn, accommodation.getCheckInDate());
        assertEquals(checkOut, accommodation.getCheckOutDate());
        assertEquals(Integer.valueOf(4), accommodation.getNumberOfNights());
        assertEquals(150.0, accommodation.getCostPerNight(), 0.01);
        assertEquals(600.0, accommodation.getTotalCost(), 0.01);
    }

    @Test
    public void testAccommodationTypes() {
        Accommodation hotel = new Accommodation();
        hotel.setType(Accommodation.AccommodationType.HOTEL);

        Accommodation hostel = new Accommodation();
        hostel.setType(Accommodation.AccommodationType.HOSTEL);

        Accommodation apartment = new Accommodation();
        apartment.setType(Accommodation.AccommodationType.APARTMENT);

        assertEquals(Accommodation.AccommodationType.HOTEL, hotel.getType());
        assertEquals(Accommodation.AccommodationType.HOSTEL, hostel.getType());
        assertEquals(Accommodation.AccommodationType.APARTMENT, apartment.getType());
    }

    @Test
    public void testBookingInformation() {
        Accommodation accommodation = new Accommodation();

        accommodation.setBookingReference("BK-12345");
        accommodation.setConfirmationNumber("CONF-67890");
        accommodation.setBookingPlatform("Booking.com");
        accommodation.setIsConfirmed(true);

        assertEquals("BK-12345", accommodation.getBookingReference());
        assertEquals("CONF-67890", accommodation.getConfirmationNumber());
        assertEquals("Booking.com", accommodation.getBookingPlatform());
        assertTrue(accommodation.getIsConfirmed());
    }

    @Test
    public void testContactInformation() {
        Accommodation accommodation = new Accommodation();

        accommodation.setContactPhone("+33 1 23 45 67 89");
        accommodation.setContactEmail("info@grandhotel.com");
        accommodation.setWebsite("https://grandhotel.com");

        assertEquals("+33 1 23 45 67 89", accommodation.getContactPhone());
        assertEquals("info@grandhotel.com", accommodation.getContactEmail());
        assertEquals("https://grandhotel.com", accommodation.getWebsite());
    }

    @Test
    public void testCheckInOutTimes() {
        Accommodation accommodation = new Accommodation();

        accommodation.setCheckInTime(LocalTime.of(15, 0));
        accommodation.setCheckOutTime(LocalTime.of(11, 0));

        assertEquals(LocalTime.of(15, 0), accommodation.getCheckInTime());
        assertEquals(LocalTime.of(11, 0), accommodation.getCheckOutTime());
    }
}
