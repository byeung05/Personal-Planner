package model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

//Tests Goal
public class GoalTest {
    private Goal g1;
    private Goal g2;

    @BeforeEach
    void runBefore() {
        g1 = new Goal("Achieve HSK 3 in fluency", 1, false);
        g2 = new Goal("start taking chinese classes", 2, false);

    }

    @Test
    void testConstructor() {
        assertEquals(g1.getProgress(), 0);
        assertEquals(g2.getProgress(), 0);
        assertEquals("Achieve HSK 3 in fluency", g1.getDescription());
        assertEquals(g1.isCompleted(), false);
        assertEquals("start taking chinese classes", g2.getDescription());
        assertEquals(g2.isCompleted(), false);
    }

}
