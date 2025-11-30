package com.org.planmet.model;

import org.junit.Test;
import static org.junit.Assert.*;

import java.time.LocalDateTime;

/**
 * Unit tests for Expense entity
 */
public class ExpenseTest {

    @Test
    public void testExpenseCreation() {
        Expense expense = new Expense("Hotel Booking", Expense.ExpenseCategory.ACCOMMODATION, 500.0);

        expense.setDescription("3 nights at Grand Hotel");
        expense.setCurrency("USD");
        expense.setPaymentMethod("Credit Card");
        expense.setPaidBy("John Doe");

        assertEquals("Hotel Booking", expense.getExpenseName());
        assertEquals(Expense.ExpenseCategory.ACCOMMODATION, expense.getCategory());
        assertEquals(500.0, expense.getAmount(), 0.01);
        assertEquals("USD", expense.getCurrency());
        assertEquals("Credit Card", expense.getPaymentMethod());
        assertEquals("John Doe", expense.getPaidBy());
    }

    @Test
    public void testExpenseCategories() {
        Expense accommodation = new Expense("Hotel", Expense.ExpenseCategory.ACCOMMODATION, 200.0);
        Expense food = new Expense("Dinner", Expense.ExpenseCategory.FOOD_DINING, 50.0);
        Expense transport = new Expense("Taxi", Expense.ExpenseCategory.TRANSPORTATION, 30.0);

        assertEquals(Expense.ExpenseCategory.ACCOMMODATION, accommodation.getCategory());
        assertEquals(Expense.ExpenseCategory.FOOD_DINING, food.getCategory());
        assertEquals(Expense.ExpenseCategory.TRANSPORTATION, transport.getCategory());
    }

    @Test
    public void testPlannedVsActualExpense() {
        Expense planned = new Expense("Budget Item", Expense.ExpenseCategory.ACTIVITIES, 100.0);
        planned.setIsPlanned(true);

        Expense actual = new Expense("Actual Cost", Expense.ExpenseCategory.ACTIVITIES, 120.0);
        actual.setIsPlanned(false);

        assertTrue(planned.getIsPlanned());
        assertFalse(actual.getIsPlanned());
    }

    @Test
    public void testReceiptTracking() {
        Expense expense = new Expense("Purchase", Expense.ExpenseCategory.SHOPPING, 75.0);

        expense.setReceiptNumber("REC-2025-001");
        expense.setNotes("Souvenirs from local market");

        assertEquals("REC-2025-001", expense.getReceiptNumber());
        assertEquals("Souvenirs from local market", expense.getNotes());
    }

    @Test
    public void testMultipleCurrencies() {
        Expense usd = new Expense("US Expense", Expense.ExpenseCategory.OTHER, 100.0);
        usd.setCurrency("USD");

        Expense eur = new Expense("EU Expense", Expense.ExpenseCategory.OTHER, 100.0);
        eur.setCurrency("EUR");

        assertEquals("USD", usd.getCurrency());
        assertEquals("EUR", eur.getCurrency());
    }

    @Test
    public void testExpenseTimestamp() {
        Expense expense = new Expense("Test", Expense.ExpenseCategory.OTHER, 10.0);

        assertNotNull(expense.getCreatedAt());
        assertTrue(expense.getCreatedAt().isBefore(LocalDateTime.now().plusSeconds(1)));
    }
}
