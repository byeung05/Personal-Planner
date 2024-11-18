package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import model.Goal;
import model.GoalHierarchy;
import persistance.JsonWriter;
import persistance.JsonReader;
import java.io.IOException;

// This class represents the GUI interface of the program
public class GUI extends JFrame {

    private JProgressBar progressBar;
    private List<GoalHierarchy> hierarchies;
    private JList<String> hierarchyList;
    private DefaultListModel<String> goalListModel;
    private DefaultListModel<String> hierarchyListModel;
    private JList<String> goalList;
    private JTextField hierarchyTitle;
    private JTextField goalDescriptionField;
    private JTextField goalTierField;
    private JTextField tierField;
    private JCheckBox goalCompletedCheckBox;
    private static final String JSON_STORE = "./data/planner.json";
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    // EFFECTS: constructs and initalizes the program
    public GUI() {
        hierarchies = new ArrayList<>();

        init();

        // DO I MAKE A NEW CLASS fo
    }

    // EFFECTS: initializes and provides the settings for the displays, and data
    // persistance
    public void init() {
        setTitle("Planner App");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(100, 100));
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        display();
        inputDisplay();
        setSize(1000, 1000);
        setLocationRelativeTo(null);

    }

    // MODIFIES: this
    // EFFECTS: renders the overall Display and functionality of the planner app
    public void display() {
        JPanel leftPanel = new JPanel(new BorderLayout(10, 10));

        hierarchyListModel = new DefaultListModel<>();
        hierarchyList = new JList<>(hierarchyListModel);
        hierarchyList.addListSelectionListener(e -> updateGoalList());
        JScrollPane hierarchyScrollPane = new JScrollPane(hierarchyList);
        leftPanel.add(hierarchyScrollPane, BorderLayout.NORTH);

        goalListModel = new DefaultListModel<>();
        goalList = new JList<>(goalListModel);
        goalList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                toggleGoalCompletion(e);
            }
        });
        JScrollPane goalScrollPane = new JScrollPane(goalList);
        leftPanel.add(goalScrollPane, BorderLayout.CENTER);

        add(leftPanel, BorderLayout.CENTER);

    }

    // MODIFIES: this, Goal
    // EFFECTS: toggles a goal's completion from mouseclick
    public void toggleGoalCompletion(MouseEvent e) {
        int index = goalList.locationToIndex(e.getPoint());
        if (index != -1) {
            int hierarchyIndex = hierarchyList.getSelectedIndex();
            if (hierarchyIndex != -1) {
                GoalHierarchy selected = hierarchies.get(hierarchyIndex);
                Goal goal = selected.getGoals().get(index);
                goal.setCompleted(!goal.isCompleted());
                updateGoalList();
            }

        }

    }

    // MODIFIES: this
    // EFFECTS: creates the display to display Progress, goals, goal hiearchies
    public void inputDisplay() {
        JPanel inputPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 5));

        JPanel hierarchyInputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        hierarchyTitle = new JTextField(15);
        JButton addHierarchyButton = new JButton("Add Goal Hierarchy");
        addHierarchyButton.addActionListener(e -> addHierarchy());
        hierarchyInputPanel.add(new JLabel("Hierarchy Title: "));
        hierarchyInputPanel.add(hierarchyTitle);
        createProgressBar(inputPanel);
        hierarchyInputPanel.add(addHierarchyButton);
        inputPanel.add(hierarchyInputPanel);

        createGoalDisplay(inputPanel);

        JPanel saveLoadPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton saveButton = new JButton("Save");
        JButton loadButton = new JButton("Load");
        saveButton.addActionListener(e -> saveGoals());
        loadButton.addActionListener(e -> loadGoals());
        saveLoadPanel.add(saveButton);
        saveLoadPanel.add(loadButton);
        inputPanel.add(saveLoadPanel);

        add(inputPanel, BorderLayout.EAST);
        setSize(800, 600);
        setLocationRelativeTo(null);

    }

    // MODIFIES: this
    // EFFECTS: writes the existing goals and hierarchies into JSON_STORE
    private void saveGoals() {
        try {
            jsonWriter.open();
            jsonWriter.write(hierarchies);
            jsonWriter.close();
            JOptionPane.showMessageDialog(this, "saved Goals to: " + JSON_STORE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Unable to write to file: " + JSON_STORE);
        }
    }

    // MODIFIES: this
    // EFFECTS: writes the existing goals and hierarchies into the JSON_STORE
    private void loadGoals() {
        try {
            hierarchies = jsonReader.read();
            displayHierarchy();
            updateGoalList();
            JOptionPane.showMessageDialog(this, "Loaded goals from" + JSON_STORE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Unable to read from file:" + JSON_STORE);
        }
    }

    // MODIFIES: this
    // EFFECTS: generates a progress bar
    private void createProgressBar(JPanel inputPanel) {
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        inputPanel.add(new JLabel("Overall Progress: "));
        inputPanel.add(progressBar);
    }

    // MODIFIES: this
    // EFFECTS: updates the progress bar based on total goals and completed goals
    private void updateProgressBar() {
        int hierarchyIndex = hierarchyList.getSelectedIndex();
        if (hierarchyIndex != -1) {
            GoalHierarchy selected = hierarchies.get(hierarchyIndex);
            int totalGoals = selected.getGoals().size();
            int completedGoals = 0;
            for (Goal g : selected.getGoals()) {
                if (g.isCompleted()) {
                    completedGoals++;
                }
            }
            if (totalGoals > 0) {
                int progress = (completedGoals * 100) / totalGoals;
                progressBar.setValue(progress);
            } else {
                progressBar.setValue(0);
            }
        }

    }

    // MODIFIES: this
    // EFFECTS: creates the display and input for goals
    private void createGoalDisplay(JPanel inputPanel) {
        goalDescriptionField = new JTextField(4);
        goalTierField = new JTextField(5);
        goalCompletedCheckBox = new JCheckBox();
        JButton addGoalButton = new JButton("Add Goal");
        addGoalButton.addActionListener(e -> addGoal());

        inputPanel.add(new JLabel("Goal Description"));
        inputPanel.add(goalDescriptionField);
        inputPanel.add(new JLabel("Tier: "));
        inputPanel.add(goalTierField);
        inputPanel.add(new JLabel("Completed: "));
        inputPanel.add(goalCompletedCheckBox);
        inputPanel.add(addGoalButton);
    }

    // MODIFIES: this, hierarchy, hierarchies
    // EFFECTS: adds a hierarchy to the list of hiearchies
    public void addHierarchy() {
        String title = hierarchyTitle.getText();
        if (!title.isEmpty()) {
            GoalHierarchy hierarchy = new GoalHierarchy();
            hierarchy.setTitle(title);
            hierarchies.add(hierarchy);
            displayHierarchy();
        }

    }

    // MODIFIES: this, hierarchyListModel, goalListModel
    // EFFECTS: clears the list of hierarchies, updates the list of goals and
    // displays them in the frame
    public void displayHierarchy() {
        hierarchyListModel.clear();
        for (GoalHierarchy hierarchy : hierarchies) {
            hierarchyListModel.addElement(hierarchy.getTitle());
        }
        if (!hierarchies.isEmpty()) {
            hierarchyList.setSelectedIndex(0);
        }
        updateGoalList();
    }

    // MODIFIES: this
    // EFFECTS: adds the goal to the selected hierarchy if it exists, then clears
    // the input fields
    // and updates the list of goals
    public void addGoal() {
        int selected = hierarchyList.getSelectedIndex();
        if (selected != -1) {
            String description = goalDescriptionField.getText().trim();
            try {
                int tier = Integer.parseInt(goalTierField.getText().trim());
                boolean completed = goalCompletedCheckBox.isSelected();
                Goal newGoal = new Goal(description, tier, completed);
                hierarchies.get(selected).addGoal(newGoal);
                updateGoalList();
                clearGoalInputField();
                ;
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Enter Valid Number");
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: updates the list of goals, when a new hierarchy is selected. Clears
    // old list and
    // adds new goals to the frame
    public void updateGoalList() {
        goalListModel.clear();
        int selected = hierarchyList.getSelectedIndex();
        if (selected != -1) {
            GoalHierarchy selectedHierarchy = hierarchies.get(selected);
            for (Goal goal : selectedHierarchy.getGoals()) {
                goalListModel.addElement(goalToString(goal));
            }
        }
        updateProgressBar();

    }

    // MODIFIES: this
    // EFFECT: resets the input fields
    public void clearGoalInputField() {
        goalDescriptionField.setText("");
        tierField.setText("");
        goalCompletedCheckBox.setSelected(false);
    }

    // MODIFIES: this
    // EFFECTS: converts a goal to display the desired elements of a goal
    private String goalToString(Goal g) {
        String indent = "";
        if (g.getTier() != 1) {
            for (int i = 0; i < g.getTier() - 1; i++) {
                indent += "   ";
            }
        }
        return String.format(indent + "tier %d: %s (Completed: %s)", g.getTier(), g.getDescription(),
                g.isCompleted() ? "✅" : "❌");
    }

}
