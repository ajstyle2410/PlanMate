package com.org.planmet.model;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for PackingItem entity
 */
public class PackingItemTest {

    @Test
    public void testPackingItemCreation() {
        PackingItem item = new PackingItem("Passport", PackingItem.PackingCategory.DOCUMENTS);

        item.setQuantity(1);
        item.setPriority(PackingItem.Priority.ESSENTIAL);
        item.setNotes("Keep in carry-on");

        assertEquals("Passport", item.getItemName());
        assertEquals(PackingItem.PackingCategory.DOCUMENTS, item.getCategory());
        assertEquals(Integer.valueOf(1), item.getQuantity());
        assertEquals(PackingItem.Priority.ESSENTIAL, item.getPriority());
        assertEquals("Keep in carry-on", item.getNotes());
    }

    @Test
    public void testPackingCategories() {
        PackingItem clothing = new PackingItem("T-Shirt", PackingItem.PackingCategory.CLOTHING);
        PackingItem toiletries = new PackingItem("Toothbrush", PackingItem.PackingCategory.TOILETRIES);
        PackingItem electronics = new PackingItem("Phone Charger", PackingItem.PackingCategory.ELECTRONICS);

        assertEquals(PackingItem.PackingCategory.CLOTHING, clothing.getCategory());
        assertEquals(PackingItem.PackingCategory.TOILETRIES, toiletries.getCategory());
        assertEquals(PackingItem.PackingCategory.ELECTRONICS, electronics.getCategory());
    }

    @Test
    public void testPackingStatus() {
        PackingItem item = new PackingItem("Suitcase", PackingItem.PackingCategory.MISCELLANEOUS);

        assertFalse(item.getIsPacked());

        item.setIsPacked(true);
        assertTrue(item.getIsPacked());
    }

    @Test
    public void testPriorities() {
        PackingItem essential = new PackingItem("Medication", PackingItem.PackingCategory.MEDICATIONS);
        essential.setPriority(PackingItem.Priority.ESSENTIAL);

        PackingItem optional = new PackingItem("Book", PackingItem.PackingCategory.ENTERTAINMENT);
        optional.setPriority(PackingItem.Priority.OPTIONAL);

        assertEquals(PackingItem.Priority.ESSENTIAL, essential.getPriority());
        assertEquals(PackingItem.Priority.OPTIONAL, optional.getPriority());
    }

    @Test
    public void testQuantityTracking() {
        PackingItem socks = new PackingItem("Socks", PackingItem.PackingCategory.CLOTHING);
        socks.setQuantity(7);

        PackingItem shoes = new PackingItem("Shoes", PackingItem.PackingCategory.FOOTWEAR);
        shoes.setQuantity(2);

        assertEquals(Integer.valueOf(7), socks.getQuantity());
        assertEquals(Integer.valueOf(2), shoes.getQuantity());
    }
}
