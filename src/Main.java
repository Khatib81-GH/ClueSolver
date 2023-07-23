import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Create and display the GUI to get user input
        GUI gui = new GUI();
        gui.addConfirmButtonListener(new GUI.ConfirmButtonListener() {
            @Override
            public void confirmButtonClicked(GUI.ConfirmButtonEvent event) {
                // Get user input from the GUI
                ClueLevel difficulty = gui.getSelectedDifficulty();
                boolean timerMode = gui.isTimerMode();
                int timeLimitInMinutes = gui.getTimeInMinutes();
                boolean clueLimitMode = gui.isClueLimitMode();
                int clueLimit = gui.getClueLimit();

                // Close the GUI after getting the input
                gui.dispose();

                // Create and run the ClueSolver with user input
                ClueSolver clueSolver = new ClueSolver(difficulty, timerMode, timeLimitInMinutes, clueLimitMode, clueLimit);
                clueSolver.runClueSolver();
            }
        });
    }
}
