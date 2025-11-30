package com.org.planmet.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Accommodation entity - Manages hotel/stay details for trips
 * Tracks booking information, check-in/out dates, and costs
 */
@Entity
@Table(name = "accommodations")
public class Accommodation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    @Column(name = "accommodation_name", nullable = false, length = 200)
    private String accommodationName;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", length = 50)
    private AccommodationType type;

    @Column(name = "address", length = 500)
    private String address;

    @Column(name = "city", length = 100)
    private String city;

    @Column(name = "country", length = 100)
    private String country;

    @Column(name = "check_in_date", nullable = false)
    private LocalDate checkInDate;

    @Column(name = "check_in_time")
    private LocalTime checkInTime;

    @Column(name = "check_out_date", nullable = false)
    private LocalDate checkOutDate;

    @Column(name = "check_out_time")
    private LocalTime checkOutTime;

    @Column(name = "number_of_nights")
    private Integer numberOfNights;

    @Column(name = "room_type", length = 100)
    private String roomType;

    @Column(name = "number_of_rooms")
    private Integer numberOfRooms = 1;

    @Column(name = "cost_per_night")
    private Double costPerNight;

    @Column(name = "total_cost")
    private Double totalCost;

    @Column(name = "currency", length = 10)
    private String currency = "USD";

    @Column(name = "booking_reference", length = 100)
    private String bookingReference;

    @Column(name = "booking_platform", length = 100)
    private String bookingPlatform;

    @Column(name = "confirmation_number", length = 100)
    private String confirmationNumber;

    @Column(name = "contact_phone", length = 50)
    private String contactPhone;

    @Column(name = "contact_email", length = 100)
    private String contactEmail;

    @Column(name = "website", length = 300)
    private String website;

    @Column(name = "amenities", length = 500)
    private String amenities;

    @Column(name = "notes", length = 1000)
    private String notes;

    @Column(name = "is_confirmed")
    private Boolean isConfirmed = false;

    @Column(name = "cancellation_policy", length = 500)
    private String cancellationPolicy;

    // Enum for accommodation types
    public enum AccommodationType {
        HOTEL,
        HOSTEL,
        RESORT,
        APARTMENT,
        VACATION_RENTAL,
        BED_AND_BREAKFAST,
        GUESTHOUSE,
        CAMPING,
        HOMESTAY,
        OTHER
    }

    // Constructors
    public Accommodation() {
    }

    public Accommodation(String accommodationName, LocalDate checkInDate, LocalDate checkOutDate) {
        this.accommodationName = accommodationName;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
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

    public String getAccommodationName() {
        return accommodationName;
    }

    public void setAccommodationName(String accommodationName) {
        this.accommodationName = accommodationName;
    }

    public AccommodationType getType() {
        return type;
    }

    public void setType(AccommodationType type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }

    public LocalTime getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(LocalTime checkInTime) {
        this.checkInTime = checkInTime;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public LocalTime getCheckOutTime() {
        return checkOutTime;
    }

    public void setCheckOutTime(LocalTime checkOutTime) {
        this.checkOutTime = checkOutTime;
    }

    public Integer getNumberOfNights() {
        return numberOfNights;
    }

    public void setNumberOfNights(Integer numberOfNights) {
        this.numberOfNights = numberOfNights;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public Integer getNumberOfRooms() {
        return numberOfRooms;
    }

    public void setNumberOfRooms(Integer numberOfRooms) {
        this.numberOfRooms = numberOfRooms;
    }

    public Double getCostPerNight() {
        return costPerNight;
    }

    public void setCostPerNight(Double costPerNight) {
        this.costPerNight = costPerNight;
    }

    public Double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Double totalCost) {
        this.totalCost = totalCost;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getBookingReference() {
        return bookingReference;
    }

    public void setBookingReference(String bookingReference) {
        this.bookingReference = bookingReference;
    }

    public String getBookingPlatform() {
        return bookingPlatform;
    }

    public void setBookingPlatform(String bookingPlatform) {
        this.bookingPlatform = bookingPlatform;
    }

    public String getConfirmationNumber() {
        return confirmationNumber;
    }

    public void setConfirmationNumber(String confirmationNumber) {
        this.confirmationNumber = confirmationNumber;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getAmenities() {
        return amenities;
    }

    public void setAmenities(String amenities) {
        this.amenities = amenities;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Boolean getIsConfirmed() {
        return isConfirmed;
    }

    public void setIsConfirmed(Boolean isConfirmed) {
        this.isConfirmed = isConfirmed;
    }

    public String getCancellationPolicy() {
        return cancellationPolicy;
    }

    public void setCancellationPolicy(String cancellationPolicy) {
        this.cancellationPolicy = cancellationPolicy;
    }

    @Override
    public String toString() {
        return "Accommodation{" +
                "id=" + id +
                ", accommodationName='" + accommodationName + '\'' +
                ", type=" + type +
                ", checkInDate=" + checkInDate +
                ", checkOutDate=" + checkOutDate +
                '}';
    }
}
