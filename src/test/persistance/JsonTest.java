package persistance;

import model.Goal;
import model.GoalHierarchy;

import static org.junit.jupiter.api.Assertions.assertEquals;

//code borrowed from JsonSerializationDemo, writes the implementation to test JSON functions

public class JsonTest {
    protected void checkGoal(String description, int tier, Goal goal) {
        assertEquals(description, goal.getDescription());
        assertEquals(tier, goal.getTier());
    }
}
