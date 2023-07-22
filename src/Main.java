import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Selects Difficulty using a GUI
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                GUI gui = new GUI(); // Create an instance of the GUI class

                gui.addConfirmButtonListener(e -> {
                    ClueLevel selectedDifficulty = gui.getSelectedDifficulty();
                    boolean timerMode = gui.isTimerMode();
                    boolean clueCountingMode = gui.isClueLimitMode();
                    int timeInMinutes = gui.getTimeInMinutes();
                    int clueLimit = gui.getClueLimit(); // Renamed from getNumberOfClues

                    // Pass the information to ClueSolver without printing anything
                    ClueSolver instance = new ClueSolver(selectedDifficulty, timerMode, timeInMinutes, clueCountingMode,clueLimit);

                    gui.dispose(); // Close the GUI
                });
            }
        });
    }
}
