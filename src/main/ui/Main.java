package ui;

import java.io.FileNotFoundException;

//executes the PlannerApp
public class Main {
    public static void main(String[] args) {
        try {

            new GUI().setVisible(true);
            new PlannerApp();
        } catch (FileNotFoundException e) {
            System.out.println("Unable to run application: file not found");
        }
    }
}
