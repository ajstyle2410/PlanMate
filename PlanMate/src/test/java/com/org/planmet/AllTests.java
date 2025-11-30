package com.org.planmet;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.org.planmet.model.*;

/**
 * Test Suite - Runs all unit tests
 */
@RunWith(Suite.class)
@SuiteClasses({
        TripTest.class,
        DayPlanTest.class,
        ActivityTest.class,
        ExpenseTest.class,
        AccommodationTest.class,
        PackingItemTest.class
})
public class AllTests {
    // This class remains empty, it is used only as a holder for the above
    // annotations
}
