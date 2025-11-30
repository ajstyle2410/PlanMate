package com.org.planmet.model;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Expense entity - Tracks trip expenses and budget
 * Supports categorization and actual vs planned comparison
 */
@Entity
@Table(name = "expenses")
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    @Column(name = "expense_name", nullable = false, length = 200)
    private String expenseName;

    @Column(name = "description", length = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false, length = 50)
    private ExpenseCategory category;

    @Column(name = "amount", nullable = false)
    private Double amount;

    @Column(name = "currency", length = 10)
    private String currency = "USD";

    @Column(name = "expense_date")
    private LocalDateTime expenseDate;

    @Column(name = "payment_method", length = 50)
    private String paymentMethod;

    @Column(name = "paid_by", length = 100)
    private String paidBy;

    @Column(name = "receipt_number", length = 100)
    private String receiptNumber;

    @Column(name = "notes", length = 1000)
    private String notes;

    @Column(name = "is_planned")
    private Boolean isPlanned = false;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Enum for expense categories
    public enum ExpenseCategory {
        ACCOMMODATION,
        FOOD_DINING,
        TRANSPORTATION,
        ACTIVITIES,
        SHOPPING,
        ENTERTAINMENT,
        INSURANCE,
        VISA_FEES,
        MEDICAL,
        COMMUNICATION,
        TIPS_GRATUITY,
        EMERGENCY,
        MISCELLANEOUS,
        OTHER
    }

    // Constructors
    public Expense() {
        this.createdAt = LocalDateTime.now();
    }

    public Expense(String expenseName, ExpenseCategory category, Double amount) {
        this();
        this.expenseName = expenseName;
        this.category = category;
        this.amount = amount;
    }

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (expenseDate == null) {
            expenseDate = LocalDateTime.now();
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

    public String getExpenseName() {
        return expenseName;
    }

    public void setExpenseName(String expenseName) {
        this.expenseName = expenseName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ExpenseCategory getCategory() {
        return category;
    }

    public void setCategory(ExpenseCategory category) {
        this.category = category;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public LocalDateTime getExpenseDate() {
        return expenseDate;
    }

    public void setExpenseDate(LocalDateTime expenseDate) {
        this.expenseDate = expenseDate;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaidBy() {
        return paidBy;
    }

    public void setPaidBy(String paidBy) {
        this.paidBy = paidBy;
    }

    public String getReceiptNumber() {
        return receiptNumber;
    }

    public void setReceiptNumber(String receiptNumber) {
        this.receiptNumber = receiptNumber;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Boolean getIsPlanned() {
        return isPlanned;
    }

    public void setIsPlanned(Boolean isPlanned) {
        this.isPlanned = isPlanned;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Expense{" +
                "id=" + id +
                ", expenseName='" + expenseName + '\'' +
                ", category=" + category +
                ", amount=" + amount +
                ", currency='" + currency + '\'' +
                ", isPlanned=" + isPlanned +
                '}';
    }
}
