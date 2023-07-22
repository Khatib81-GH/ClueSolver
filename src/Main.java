import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        //Selects Difficulty using a GUI
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                GUI gui = new GUI(); // Create an instance of the GUI class

                gui.addConfirmButtonListener(e -> {
                    ClueLevel selectedDifficulty = gui.getSelectedDifficulty();
                    System.out.println("Selected Difficulty: " + selectedDifficulty);
                    gui.dispose(); // Close the GUI
                    ClueSolver instance = new ClueSolver(selectedDifficulty);
                });
            }
        });
    }
}
