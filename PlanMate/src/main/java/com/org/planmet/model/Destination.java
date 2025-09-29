package com.org.planmet.model;

import javax.persistence.*;

@Entity
@Table(name = "destinations")
public class Destination {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "place_name", nullable = false, length = 150)
    private String placeName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    // ---------- Constructors ----------
    public Destination() {}

    public Destination(String placeName) {
        this.placeName = placeName;
    }

    public Destination(String placeName, Trip trip) {
        this.placeName = placeName;
        this.trip = trip;
    }

    // ---------- Getters & Setters ----------
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    @Override
	public String toString() {
		return "Destination [id=" + id + ", placeName=" + placeName + ", trip=" + trip + "]";
	}
    
    
}
