package com.org.planmet.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Transportation entity - Manages travel details (flights, trains, buses, car
 * rentals)
 * Tracks booking information, departure/arrival times, and costs
 */
@Entity
@Table(name = "transportation")
public class Transportation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    @Enumerated(EnumType.STRING)
    @Column(name = "transport_type", nullable = false, length = 50)
    private TransportType transportType;

    @Column(name = "transport_name", length = 200)
    private String transportName;

    @Column(name = "provider", length = 150)
    private String provider;

    @Column(name = "booking_reference", length = 100)
    private String bookingReference;

    @Column(name = "confirmation_number", length = 100)
    private String confirmationNumber;

    // Departure details
    @Column(name = "departure_location", nullable = false, length = 200)
    private String departureLocation;

    @Column(name = "departure_date", nullable = false)
    private LocalDate departureDate;

    @Column(name = "departure_time")
    private LocalTime departureTime;

    @Column(name = "departure_terminal", length = 50)
    private String departureTerminal;

    // Arrival details
    @Column(name = "arrival_location", nullable = false, length = 200)
    private String arrivalLocation;

    @Column(name = "arrival_date")
    private LocalDate arrivalDate;

    @Column(name = "arrival_time")
    private LocalTime arrivalTime;

    @Column(name = "arrival_terminal", length = 50)
    private String arrivalTerminal;

    // Additional details
    @Column(name = "flight_number", length = 50)
    private String flightNumber;

    @Column(name = "seat_number", length = 20)
    private String seatNumber;

    @Column(name = "class_type", length = 50)
    private String classType;

    @Column(name = "duration_minutes")
    private Integer durationMinutes;

    @Column(name = "cost")
    private Double cost;

    @Column(name = "currency", length = 10)
    private String currency = "USD";

    @Column(name = "baggage_allowance", length = 200)
    private String baggageAllowance;

    @Column(name = "notes", length = 1000)
    private String notes;

    @Column(name = "is_confirmed")
    private Boolean isConfirmed = false;

    @Column(name = "check_in_url", length = 300)
    private String checkInUrl;

    // Enum for transport types
    public enum TransportType {
        FLIGHT,
        TRAIN,
        BUS,
        CAR_RENTAL,
        TAXI,
        FERRY,
        CRUISE,
        SUBWAY,
        RIDESHARE,
        OTHER
    }

    // Constructors
    public Transportation() {
    }

    public Transportation(TransportType transportType, String departureLocation, String arrivalLocation,
            LocalDate departureDate) {
        this.transportType = transportType;
        this.departureLocation = departureLocation;
        this.arrivalLocation = arrivalLocation;
        this.departureDate = departureDate;
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

    public TransportType getTransportType() {
        return transportType;
    }

    public void setTransportType(TransportType transportType) {
        this.transportType = transportType;
    }

    public String getTransportName() {
        return transportName;
    }

    public void setTransportName(String transportName) {
        this.transportName = transportName;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getBookingReference() {
        return bookingReference;
    }

    public void setBookingReference(String bookingReference) {
        this.bookingReference = bookingReference;
    }

    public String getConfirmationNumber() {
        return confirmationNumber;
    }

    public void setConfirmationNumber(String confirmationNumber) {
        this.confirmationNumber = confirmationNumber;
    }

    public String getDepartureLocation() {
        return departureLocation;
    }

    public void setDepartureLocation(String departureLocation) {
        this.departureLocation = departureLocation;
    }

    public LocalDate getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(LocalDate departureDate) {
        this.departureDate = departureDate;
    }

    public LocalTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalTime departureTime) {
        this.departureTime = departureTime;
    }

    public String getDepartureTerminal() {
        return departureTerminal;
    }

    public void setDepartureTerminal(String departureTerminal) {
        this.departureTerminal = departureTerminal;
    }

    public String getArrivalLocation() {
        return arrivalLocation;
    }

    public void setArrivalLocation(String arrivalLocation) {
        this.arrivalLocation = arrivalLocation;
    }

    public LocalDate getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(LocalDate arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public LocalTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getArrivalTerminal() {
        return arrivalTerminal;
    }

    public void setArrivalTerminal(String arrivalTerminal) {
        this.arrivalTerminal = arrivalTerminal;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public String getClassType() {
        return classType;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getBaggageAllowance() {
        return baggageAllowance;
    }

    public void setBaggageAllowance(String baggageAllowance) {
        this.baggageAllowance = baggageAllowance;
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

    public String getCheckInUrl() {
        return checkInUrl;
    }

    public void setCheckInUrl(String checkInUrl) {
        this.checkInUrl = checkInUrl;
    }

    @Override
    public String toString() {
        return "Transportation{" +
                "id=" + id +
                ", transportType=" + transportType +
                ", departureLocation='" + departureLocation + '\'' +
                ", arrivalLocation='" + arrivalLocation + '\'' +
                ", departureDate=" + departureDate +
                '}';
    }
}
