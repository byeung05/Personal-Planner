package persistance;

import model.Goal;
import model.GoalHierarchy;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;
import java.util.*;

import org.json.*;

// Represents a reader that reads GoalHierarchy from JSON data stored in file. Borrowed from JsonSerializationDemo
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: read list of GoalHierarchy from file and returns it;
    // throws IOException if an error occurs reading data from file
    public List<GoalHierarchy> read() throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(source)));
        JSONArray jsonArray = new JSONArray(content);
        return parseGoalHierarchies(jsonArray);
    }

    // EFFECTS: parse a list of GoalHierarchy from JSON array and return it
    private List<GoalHierarchy> parseGoalHierarchies(JSONArray jsonArray) {
        List<GoalHierarchy> hierarchies = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            hierarchies.add(parseGoalHierarchy(jsonArray.getJSONObject(i)));
        }
        return hierarchies;

    }

    // EFFECTS: parses GoalHierarchy from JSON object and returns it
    private GoalHierarchy parseGoalHierarchy(JSONObject jsonObject) {
        String title = jsonObject.getString("Hierarchy Title");
        Boolean priority = jsonObject.getBoolean("priority");
        GoalHierarchy gr = new GoalHierarchy();
        gr.setPriority(priority);
        gr.setTitle(title);
        addGoals(gr, jsonObject);
        return gr;
    }

    // MODIFIES: gh
    // EFFECTS: parses Goals from JSON object and adds them to GoalHierarchy
    private void addGoals(GoalHierarchy gh, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("goals");
        for (Object json : jsonArray) {
            JSONObject nextGoal = (JSONObject) json;
            addGoal(gh, nextGoal);
        }
    }

    // MODIFIES: gh
    // EFFECTS: parses goal from JSON object and adds it to GoalHierarchy
    private void addGoal(GoalHierarchy gh, JSONObject jsonObject) {
        String description = jsonObject.getString("Goal Description");
        int tier = jsonObject.getInt("tier");
        Boolean completed = jsonObject.getBoolean("completed");
        Goal goal = new Goal(description, tier, completed);
        gh.addGoal(goal);
    }
}
