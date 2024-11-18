package model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

//Tests GoalHiearchy
public class GoalHierarchyTest {
    private GoalHierarchy goals;
    private GoalHierarchy goals2;
    private Goal g1;
    private Goal g2;

    @BeforeEach
    void runBefore() {
        goals = new GoalHierarchy();
        goals.setTitle("Chinese");
        goals2 = new GoalHierarchy();
        g1 = new Goal("Achieve HSK 3 in fluency", 1, false);
        g2 = new Goal("start taking chinese classes", 2, false);
        goals2.addGoal(g1);
    }

    @Test
    void testConstructor() {
        assertTrue(goals.getGoals().isEmpty());
        assertEquals(goals.getPosition(), 0);
    }

    @Test
    void testAddGoal() {
        goals.addGoal(g1);
        goals.setPriority(true);
        assertEquals(goals.getPosition(), 1);
        assertEquals(goals.getGoal(0), g1);

    }

    @Test
    void testRemoveGoal() {
        goals.addGoal(g2);
        assertEquals(goals.getSize(), 1);
        goals.removeGoal(0);
        assertEquals(goals.getSize(), 0);

    }

    @Test
    void testAddTitle() {
        assertEquals(goals.getTitle(), "Chinese");
        goals.changeTitle("Spanish");

    }

    @Test
    void testEditGoal() {
        assertEquals("Achieve HSK 3 in fluency", goals2.getGoal(0).getDescription());
        goals2.editGoal(0, "Hello");
        assertEquals("Hello", goals2.getGoal(0).getDescription());
    }

    @Test
    void testMarkAsComplete() {
        assertEquals(false, goals2.getGoal(0).isCompleted());
        goals2.markAsComplete(0);
        assertEquals(true, goals2.getGoal(0).isCompleted());
        goals2.markAsComplete(0);
        assertEquals(true, goals2.getGoal(0).isCompleted());
        goals2.addGoal(g2);
        assertEquals(false, goals2.getGoal(1).isCompleted());
        goals2.markAsComplete(1);
        assertEquals(true, goals2.getGoal(1).isCompleted());
    }

    @Test
    void testMarkAsIncomplete() {
        assertEquals(false, goals2.getGoal(0).isCompleted());
        goals2.markAsComplete(0);
        assertEquals(true, goals2.getGoal(0).isCompleted());
        goals2.markAsIncomplete(0); // test inverse
        assertEquals(false, goals2.getGoal(0).isCompleted());
        goals2.markAsIncomplete(0);
        assertEquals(false, goals2.getGoal(0).isCompleted());
    }

}
