package ui;

import model.Goal;
import model.GoalHierarchy;
import persistance.JsonReader;
import persistance.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

// The planner app borrows code from the teller app and handles the user inputs, allowing one to
// add goals to a hierarchy, prioritize goals, view goals etc.
public class PlannerApp {
    private static final String JSON_PLANNER = "data/planner.json";
    private List<GoalHierarchy> mainList;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private Scanner input;
    private ArrayList<Integer> priority;
    private ArrayList<Integer> completedHiearchy;
    private ArrayList<Integer> completedTier;

    // EFFECT: runs the planner app and constructs the goalhierarchy
    public PlannerApp() throws FileNotFoundException {
        jsonWriter = new JsonWriter(JSON_PLANNER);
        jsonReader = new JsonReader(JSON_PLANNER);
        runPlanner();
    }

    // MODIFIES: this
    // EFFECT: processes user input
    public void runPlanner() {
        boolean keepGoing = true;
        String command = null;
        init();
        while (keepGoing) {
            displayMenu();
            command = input.next();
            command = command.toLowerCase();

            if (command.equals("q")) {
                keepGoing = false;
            } else {
                processCommand(command);
            }
        }

        System.out.println("The best time to start was yesterday..");
    }

    // EFFECTS: displays menu of options to user
    public void displayMenu() {
        System.out.println("\nMAIN MENU");
        System.out.println("\ta -> add goal hierarchy"); // then ask if you want to add a new goal tre
        System.out.println("\ts -> save goalHierarchies to file");
        System.out.println("\tl -> load goalHierarchies from file");
        System.out.println("\tv -> view goals"); // allow to make complete/ prioritze within the view
        System.out.println("\tq -> quit");

    }

    // EFFECT: initializes required objects and settings.
    public void init() {
        input = new Scanner(System.in);
        mainList = new ArrayList<>();
        priority = new ArrayList<>();
        completedHiearchy = new ArrayList<>();
        completedTier = new ArrayList<>();
        input.useDelimiter("\n");
    }

    // MODIFIES: this
    // EFFECTS: processes user command
    public void processCommand(String command) {
        if (command.equals("a")) {
            createHieararchy();
        } else if (command.equals("s")) {
            saveGoals();
        } else if (command.equals("l")) {
            loadGoals();
        } else if (command.equals("v")) {
            input.nextLine();
            viewGoals();
        } else {
            System.out.println("Selection not valid...");
        }
    }

    // MODIFIES: this
    // EFFECTS: saves the goal hiearchy to a file
    public void saveGoals() {
        try {
            jsonWriter.open();
            jsonWriter.write(mainList);
            jsonWriter.close();
            System.out.println("Saved to " + "./data/planner.json");
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + "./data/planner.json");
        }

    }

    // MODIFIES: this
    // EFFECTS: loads workroom from file
    private void loadGoals() {
        try {
            mainList = jsonReader.read();
            System.out.println("Loaded from " + JSON_PLANNER);

        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_PLANNER);
        }
    }

    // MODIFIES: this
    // EFFECTS: creates a new GoalHierarchy and adds a Goal to the Hiearchy
    public void createHieararchy() {
        GoalHierarchy hierarchy = new GoalHierarchy();
        mainList.add(hierarchy);
        addSubGoal(hierarchy);
    }

    // MODIFIES: this
    // EFFECTS: prints out the list of titles of the goals and allows for editing,
    // removing, and prioritizing goals
    public void viewGoals() {
        if (mainList.size() == 0) {
            System.out.println("No goals to see!");
        } else {
            divider();
            printGoals();
            divider();
            viewGoalsMenu();
        }

    }

    // REQUIRES: requires user to input a valid Goal position, position <
    // mainList.size()
    // EFFECT: prints out the menu for viewGoals() by handling the various commands
    public void viewGoalsMenu() {
        Boolean keepGoing = true;
        while (keepGoing) {
            goalMenu(); ///
            String command = input.nextLine();

            if (command.equals("r")) {
                // removeGoal();
            } else if (command.equals("a")) {
                System.out.println("Enter the goal number you want to add to");
                int x = Integer.parseInt(input.nextLine());
                x--;
                addSubGoal(mainList.get(x));
            } else if (command.equals("c")) {
                completed();
            } else if (command.equals("p")) {
                prioritize();
            } else if (command.equals("q")) {
                keepGoing = false;
            } else {
                System.out.println("Invalid input, please try again");
            }
        }
    }

    // REQUIRES: requires user to input a valid Goal position, position <
    // mainList.size()
    // MODIFIES: this
    // EFFECT: marks as complete the tier of goal the user wishes to prioritize
    public void completed() {
        System.out.println("Which Goal Hiearchy do you wish to complete");
        int select = Integer.parseInt(input.nextLine());
        System.out.println("Which tier of goal do you wish too choose");
        int chosen = Integer.parseInt(input.nextLine());
        completedHiearchy.add(select);
        completedTier.add(chosen);

    }

    // MODIFIES: this
    // EFFECT: adds the tier of goal the user wishes to prioritize
    public void prioritize() {
        System.out.println("Which goal would you like to prioritize");
        int next = Integer.parseInt(input.nextLine());
        priority.add(next);
    }

    // EFFECTS: prints out the title of the goals and their subsequent goals
    public void printGoals() {
        int i = 1;
        for (GoalHierarchy g : mainList) {
            String x = "";
            String check = "";
            if (priority.contains(i)) {
                g.setPriority(true);

                x = "☆";
            }
            if (completedHiearchy.contains(i)) {
                check = "✅";
            }
            System.out.println(x + " " + i + " " + g.getTitle() + " " + check);

            if (g.getSize() > 1) { // skip first goal as already present
                String check2 = "";
                for (int j = 1; j < g.getSize(); j++) {
                    if (completedTier.contains(i)) {
                        check2 = "✅";
                    }

                    System.out.println("     " + g.getGoal(j).getDescription() + " " + check2);
                }
            }
            i++;
        }
    }

    // REQUIRES: requires user to input a valid Goal position, position <
    // mainList.size()
    // MODIFIES: this
    // EFFECTS: add a Goal to the hierarchy, taking into account the
    // various inputs required by Goal
    public void addSubGoal(GoalHierarchy hierarchy) {
        System.out.println("Is this the first goal in the hierachy? y/n");
        input.nextLine().trim();
        String ans = input.nextLine();
        String title = "";
        if (ans.equals("y") | ans.equals("Y")) {
            System.out.println("What's the title?");
            title = input.nextLine().trim();
        }

        System.out.println("What is the goals's description");
        String description = input.nextLine();

        System.out.println("What's the goal's tier, must be a number");
        int tier = Integer.parseInt(input.nextLine()); // handle if not int
        if (ans.equals("y") | ans.equals("Y")) {
            hierarchy.setTitle(title);
            Goal newGoal = new Goal(description, tier, false);
            hierarchy.addGoal(newGoal); //

        } else {
            Goal newGoal = new Goal(description, tier, false);
            hierarchy.addGoal(newGoal);
        }

    }

    // REQUIRES: the user to input a valid Goal and position to be within
    // (0 <= choice) && (choice <= mainList.size())
    // MODIFIES: this
    // EFFECT: changes the selected goal description
    public void editGoal() {
        System.out.println("Which Goal Hierachy would you like to edit");
        int choice = Integer.parseInt(input.nextLine());
        if ((0 <= choice) && (choice <= mainList.size())) {
            System.out.println("Which Goal would you like to edit?");
            int selected = Integer.parseInt(input.nextLine());
            int position = choice--;
            Goal g = mainList.get(position).getGoal(selected);
            System.out.println("What is the new description?");
            String answer = input.nextLine();
            g.setDescription(answer);
        }

    }

    // MODIFIES: this, mainList
    // EFFECT: removes the specified goal from the goal hiearchy after user
    // indicates position and tier
    public void removeGoal() {
        System.out.println("Which goal do you wish to remove from?");
        int toGo = Integer.parseInt(input.nextLine());
        toGo--;
        System.out.println("Which tier?");
        int tierSub = Integer.parseInt(input.nextLine());
        tierSub--;
        if ((tierSub <= mainList.get(toGo).getSize()) && (toGo <= mainList.size())) {
            mainList.get(toGo).removeGoal(tierSub);
            System.out.println("Removed!");
        } else {
            System.out.println("Incorrect Input");
            removeGoal();
        }

    }

    // EFFECT: produces the goal Menu
    public void goalMenu() {
        System.out.println("What would you like to do?");
        System.out.println("\ta -> add subgoal");
        System.out.println("\tc -> complete goal");
        // System.out.println("\te -> edit goal");
        System.out.println("\tp -> prioritize goal");
        System.out.println("\tq -> quit");

    }

    // EFFECT: creates an line, borrowed from FlashCard
    private void divider() {
        System.out.println("----------------------------------");
    }

}
