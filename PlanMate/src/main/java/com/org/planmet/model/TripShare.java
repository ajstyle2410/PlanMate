package com.org.planmet.model;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * TripShare entity - Manages trip sharing and collaboration
 * Allows users to share trips with others and set permissions
 */
@Entity
@Table(name = "trip_shares")
public class TripShare {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shared_with_user_id", nullable = false)
    private UserProfile sharedWithUser;

    @Enumerated(EnumType.STRING)
    @Column(name = "permission_level", nullable = false, length = 20)
    private PermissionLevel permissionLevel;

    @Column(name = "shared_at")
    private LocalDateTime sharedAt;

    @Column(name = "accepted")
    private Boolean accepted = false;

    @Column(name = "accepted_at")
    private LocalDateTime acceptedAt;

    @Column(name = "message", length = 500)
    private String message;

    // Enum for permission levels
    public enum PermissionLevel {
        VIEW_ONLY,
        COMMENT,
        EDIT,
        ADMIN
    }

    // Constructors
    public TripShare() {
        this.sharedAt = LocalDateTime.now();
    }

    public TripShare(Trip trip, UserProfile sharedWithUser, PermissionLevel permissionLevel) {
        this();
        this.trip = trip;
        this.sharedWithUser = sharedWithUser;
        this.permissionLevel = permissionLevel;
    }

    @PrePersist
    protected void onCreate() {
        if (sharedAt == null) {
            sharedAt = LocalDateTime.now();
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

    public UserProfile getSharedWithUser() {
        return sharedWithUser;
    }

    public void setSharedWithUser(UserProfile sharedWithUser) {
        this.sharedWithUser = sharedWithUser;
    }

    public PermissionLevel getPermissionLevel() {
        return permissionLevel;
    }

    public void setPermissionLevel(PermissionLevel permissionLevel) {
        this.permissionLevel = permissionLevel;
    }

    public LocalDateTime getSharedAt() {
        return sharedAt;
    }

    public void setSharedAt(LocalDateTime sharedAt) {
        this.sharedAt = sharedAt;
    }

    public Boolean getAccepted() {
        return accepted;
    }

    public void setAccepted(Boolean accepted) {
        this.accepted = accepted;
    }

    public LocalDateTime getAcceptedAt() {
        return acceptedAt;
    }

    public void setAcceptedAt(LocalDateTime acceptedAt) {
        this.acceptedAt = acceptedAt;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "TripShare{" +
                "id=" + id +
                ", permissionLevel=" + permissionLevel +
                ", accepted=" + accepted +
                ", sharedAt=" + sharedAt +
                '}';
    }
}
