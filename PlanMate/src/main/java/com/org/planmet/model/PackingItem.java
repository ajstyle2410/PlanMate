package com.org.planmet.model;

import javax.persistence.*;

/**
 * PackingItem entity - Manages packing checklist for trips
 * Categorized items with quantity and packed status tracking
 */
@Entity
@Table(name = "packing_items")
public class PackingItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    @Column(name = "item_name", nullable = false, length = 200)
    private String itemName;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", length = 50)
    private PackingCategory category;

    @Column(name = "quantity")
    private Integer quantity = 1;

    @Column(name = "is_packed")
    private Boolean isPacked = false;

    @Column(name = "notes", length = 500)
    private String notes;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority", length = 20)
    private Priority priority = Priority.NORMAL;

    // Enums
    public enum PackingCategory {
        CLOTHING,
        TOILETRIES,
        ELECTRONICS,
        DOCUMENTS,
        MEDICATIONS,
        ACCESSORIES,
        FOOTWEAR,
        OUTDOOR_GEAR,
        ENTERTAINMENT,
        FOOD_SNACKS,
        BABY_ITEMS,
        PET_SUPPLIES,
        SPORTS_EQUIPMENT,
        MISCELLANEOUS,
        OTHER
    }

    public enum Priority {
        ESSENTIAL,
        IMPORTANT,
        NORMAL,
        OPTIONAL
    }

    // Constructors
    public PackingItem() {
    }

    public PackingItem(String itemName, PackingCategory category) {
        this.itemName = itemName;
        this.category = category;
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

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public PackingCategory getCategory() {
        return category;
    }

    public void setCategory(PackingCategory category) {
        this.category = category;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Boolean getIsPacked() {
        return isPacked;
    }

    public void setIsPacked(Boolean isPacked) {
        this.isPacked = isPacked;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    @Override
    public String toString() {
        return "PackingItem{" +
                "id=" + id +
                ", itemName='" + itemName + '\'' +
                ", category=" + category +
                ", quantity=" + quantity +
                ", isPacked=" + isPacked +
                '}';
    }
}
