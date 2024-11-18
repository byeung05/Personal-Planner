package persistance;

import model.Goal;
import model.GoalHierarchy;
import persistance.JsonReader;
import persistance.JsonWriter;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//code borrowed from JsonSerializationDemo, writes the implementation to test JSON functions
class JsonReaderTest extends JsonTest {

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("data/noSuchFile.json");
        try {
            List<GoalHierarchy> gr = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyGoalHierarchy() {
        JsonReader reader = new JsonReader("data/emptyGoalHierarchy.json");
        try {
            List<GoalHierarchy> gr = reader.read();
            assertEquals(0, gr.size());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    // @Test
    // void testReaderGoalHierarchy() {
    // JsonReader reader = new JsonReader("bin/data/notEmptyGoalHierarchy.json");
    // try {
    // List<GoalHierarchy> gr = reader.read();
    // assertEquals("My work room", gr.getName());
    // List<Thingy> thingies = gr.getThingies();
    // assertEquals(2, thingies.size());
    // checkThingy("needle", Category.STITCHING, thingies.get(0));
    // checkThingy("saw", Category.WOODWORK, thingies.get(1));
    // } catch (IOException e) {
    // fail("Couldn't read from file");
    // }
    // }
}