package model;

import java.util.*;

import org.json.JSONArray;
import org.json.JSONObject;
import persistance.Writable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// This class represents a goal and the various traits that a Goal has such as a title and tier.
public class Goal implements Writable {
    private String description;
    private boolean completed;
    private int progress;
    private int tier;

    // REQUIRES String, description is not "", tier > 0
    // EFFECTS: Creates the first Goal in a hiearchy of goals.
    // The first Goal in the hiearchy must have a Title
    // Sets the Goal description, to assigned values and
    // determines completion of goal. Progress set to 0, priority set to false
    public Goal(String description, int tier, boolean completed) {
        this.description = description;
        this.completed = completed;
        progress = 0;
        this.tier = tier;

    }

    public String getDescription() {
        return description;
    }

    public int getTier() {
        return this.tier;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public int getProgress() {
        return progress;
    }

    // EFFECTS: converts Goals into JSON

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("Goal Description", description);
        json.put("completed", completed);
        json.put("tier", tier);
        return json;
    }

}
