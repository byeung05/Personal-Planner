package persistance;

import persistance.JsonReader;
import persistance.JsonWriter;
import persistance.JsonTest;
import model.Goal;
import model.GoalHierarchy;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//code borrowed from JsonSerializationDemo, writes the implementation to test JSON functions
public class JsonWriterTest extends JsonTest {
    // NOTE TO CPSC 210 STUDENTS: the strategy in designing tests for the JsonWriter
    // is to
    // write data to a file and then use the reader to read it back in and check
    // that we
    // read in a copy of what was written out.

    @Test
    void testWriterInvalidFile() {
        try {
            GoalHierarchy gr = new GoalHierarchy();
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterGoalHierarchy() {
        try {
            List<GoalHierarchy> gh = new ArrayList<>();
            GoalHierarchy gh1 = new GoalHierarchy();
            gh1.setTitle("Chinese");
            gh1.setPriority(false);
            Goal g = new Goal("Do Better", 1, true);
            gh1.addGoal(g);
            gh.add(gh1);
            JsonWriter writer = new JsonWriter("data/emptyGoalHierarchy.json");
            writer.open();
            writer.write(gh);
            writer.close();

            JsonReader reader = new JsonReader("data/notEmptyGoalHierarchy.json");
            gh = reader.read();
            assertEquals("Chinese", gh.get(0).getTitle());
            assertEquals(1, gh.size()); // not sure
            checkGoal("Do Better", 1, gh.get(0).getGoal(0));
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterEmptyGoalHierarchy() {
        try {
            List<GoalHierarchy> gh = new ArrayList<>();
            JsonWriter writer = new JsonWriter("data/emptyGoalHierarchy.json");
            writer.open();
            writer.write(gh);
            writer.close();

            JsonReader reader = new JsonReader("data/emptyGoalHierarchy.json");
            gh = reader.read();
            assertEquals(0, gh.size());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}
