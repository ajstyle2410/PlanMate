package com.org.planmet.model;

import javax.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "trips")
public class Trip {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long tripId;

	@Column(nullable = false, length = 150)
	private String title;

	private Double budget;

	private LocalDate uplodedDate;

	@Column(length = 1000)
	private String description;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name = "start_date", nullable = false)
	private LocalDate startDate;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name = "end_date", nullable = false)
	private LocalDate endDate;

	@OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<Destination> destinations = new ArrayList<>();

	// New relationships for enhanced features
	@OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<DayPlan> dayPlans = new ArrayList<>();

	@OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<Expense> expenses = new ArrayList<>();

	@OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<Accommodation> accommodations = new ArrayList<>();

	@OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<Transportation> transportations = new ArrayList<>();

	@OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<PackingItem> packingItems = new ArrayList<>();

	@OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<TripDocument> documents = new ArrayList<>();

	@OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<TripShare> shares = new ArrayList<>();

	// Enhanced trip fields
	@Enumerated(EnumType.STRING)
	@Column(length = 20)
	private Visibility visibility = Visibility.PRIVATE;

	@Column(name = "tags", length = 500)
	private String tags;

	@Column(name = "category", length = 100)
	private String category;

	@Enumerated(EnumType.STRING)
	@Column(length = 20, nullable = false)
	private Status status = Status.DRAFT;

	// 🔗 Many trips belong to one user
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false) // foreign key
	private UserProfile user;

	public enum Status {
		DRAFT, ACTIVE, COMPLETED, CANCELLED
	}

	public enum Visibility {
		PRIVATE, SHARED, PUBLIC
	}

	// ---------- Convenience methods ----------
	public void addDestination(Destination destination) {
		if (destination != null) {
			destinations.add(destination);
			destination.setTrip(this);
		}
	}

	public void removeDestination(Destination destination) {
		if (destination != null) {
			destinations.remove(destination);
			destination.setTrip(null);
		}
	}

	// ---------- Getters & Setters ----------
	public Long getTripId() {
		return tripId;
	}

	public void setTripId(Long tripId) {
		this.tripId = tripId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public List<Destination> getDestinations() {
		return destinations;
	}

	public void setDestinations(List<Destination> destinations) {
		this.destinations.clear();
		if (destinations != null) {
			this.destinations.addAll(destinations);
		}
	}

	public LocalDate getUplodedDate() {
		return uplodedDate;
	}

	public void setUplodedDate(LocalDate uplodedDate) {
		this.uplodedDate = uplodedDate;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Visibility getVisibility() {
		return visibility;
	}

	public void setVisibility(Visibility visibility) {
		this.visibility = visibility;
	}

	public UserProfile getUser() {
		return user;
	}

	public void setUser(UserProfile user) {
		this.user = user;
	}

	private String Uploadedby;

	public String getUploadedby() {
		return Uploadedby;
	}

	public void setUploadedby(String uploadedby) {
		Uploadedby = uploadedby;
	}

	// Getters and Setters for new relationships
	public List<DayPlan> getDayPlans() {
		return dayPlans;
	}

	public void setDayPlans(List<DayPlan> dayPlans) {
		this.dayPlans.clear();
		if (dayPlans != null) {
			this.dayPlans.addAll(dayPlans);
		}
	}

	public List<Expense> getExpenses() {
		return expenses;
	}

	public void setExpenses(List<Expense> expenses) {
		this.expenses.clear();
		if (expenses != null) {
			this.expenses.addAll(expenses);
		}
	}

	public List<Accommodation> getAccommodations() {
		return accommodations;
	}

	public void setAccommodations(List<Accommodation> accommodations) {
		this.accommodations.clear();
		if (accommodations != null) {
			this.accommodations.addAll(accommodations);
		}
	}

	public List<Transportation> getTransportations() {
		return transportations;
	}

	public void setTransportations(List<Transportation> transportations) {
		this.transportations.clear();
		if (transportations != null) {
			this.transportations.addAll(transportations);
		}
	}

	public List<PackingItem> getPackingItems() {
		return packingItems;
	}

	public void setPackingItems(List<PackingItem> packingItems) {
		this.packingItems.clear();
		if (packingItems != null) {
			this.packingItems.addAll(packingItems);
		}
	}

	public List<TripDocument> getDocuments() {
		return documents;
	}

	public void setDocuments(List<TripDocument> documents) {
		this.documents.clear();
		if (documents != null) {
			this.documents.addAll(documents);
		}
	}

	public List<TripShare> getShares() {
		return shares;
	}

	public void setShares(List<TripShare> shares) {
		this.shares.clear();
		if (shares != null) {
			this.shares.addAll(shares);
		}
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	@Override
	public String toString() {
		return "Trip [tripId=" + tripId + ", title=" + title + ", budget=" + budget + ", uplodedDate=" + uplodedDate
				+ ", description=" + description + ", startDate=" + startDate + ", endDate=" + endDate
				+ ", destinations=" + destinations + ", status=" + status + ", user=" + user + ", Uploadedby="
				+ Uploadedby + "]";
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Trip))
			return false;
		Trip trip = (Trip) o;
		return Objects.equals(tripId, trip.tripId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(tripId);
	}
}
