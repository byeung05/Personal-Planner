package model;

import java.util.*;

import org.json.JSONArray;
import org.json.JSONObject;
import persistance.Writable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// This GoalHierarchy represents the current goals within a list and allows for manipulation of said goals.
// Position refers to the position the Goal has in a hierarchy and increments for each additional goal. It starts at 1
// The first Goal is typically described as tier.
// BORROWED CODE FROM JsonSerializationDemo
public class GoalHierarchy implements Writable {

    private ArrayList<Goal> goals;
    private String title;
    private int position;
    private boolean priority;

    // REQUIRES: position > 0
    // EFFECTS: creates an empty list of goals with a default position of 0 and an
    // empty title.
    // priority set to false
    public GoalHierarchy() {
        goals = new ArrayList();
        title = "";
        position = 0;
        this.priority = false;
    }

    // MODIFIES: this
    // EFFECTS: adds a goal to a list of Goals and increases the position of the
    // goal in the hierarchy.
    // the first position starts at 1
    public void addGoal(Goal g) {
        goals.add(g);
        position++;

    }

    // REQUIRES: position >= this.position > 0, goals.size() > 1
    // MODIFIES: this
    // EFFECTS: removes the goal at the indicated position -1 in the hierarchy
    public void removeGoal(int index) {
        goals.remove(index);
    }

    // MODIFIES: this
    // EFFECTS: modifies the Title
    public void changeTitle(String title) {
        this.title = title;
    }

    // REQUIRES: position >= this.position > 0, goals.size() > 1
    // MODIFIES: this
    // EFFECTS: edits the goal at the indicated position -1 in the hierarchy
    public void editGoal(int position, String description) {
        getGoal(position).setDescription(description);
    }

    // REQUIRES: position >= this.position > 0, goals.size() > 1
    // MODIFIES: this
    // EFFECTS: marks the goal at the indicated position -1 in the hierarchy as
    // complete
    public void markAsComplete(int position) {
        getGoal(position).setCompleted(true);
    }

    // REQUIRES: position >= this.position > 0, goals.size() > 1
    // MODIFIES: this
    // EFFECTS: marks the goal at the indicated position -1 in the hierarchy as
    // incomplete
    public void markAsIncomplete(int position) {
        getGoal(position).setCompleted(false);
    }

    public void setPriority(boolean priority) {
        this.priority = priority;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    // EFFECTS: converts the GoalHierarchy to JSON

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("Hierarchy Title", title); // watchout
        json.put("priority", priority);
        json.put("goals", goalsToJson());
        return json;
    }

    // EFFECTS: returns Goals in this GoalHierarchy as a JSON array
    private JSONArray goalsToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Goal g : goals) {
            jsonArray.put(g.toJson());
        }

        return jsonArray;
    }

    public int getSize() {
        return goals.size();

    }

    public ArrayList<Goal> getGoals() {
        return goals;
    }

    public int getPosition() {
        return position;
    }

    public Goal getGoal(int index) {

        return goals.get(index);
    }

}
