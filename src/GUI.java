import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

    public class GUI extends JFrame {
        private JComboBox<ClueLevel> difficultyComboBox;
        private JCheckBox timerModeCheckBox;
        private JCheckBox clueLimitCheckBox; // Renamed from clueLimitingCheckBox
        private JTextField timeInputField;
        private JTextField clueLimitInputField; // Renamed from clueLimitInputField
        private int userResponse = -1;
        private ClueLevel selectedDifficulty;

        public GUI() {
            // Set up the JFrame properties
            setTitle("Difficulty Selector");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // Create the main content panel using BorderLayout
            JPanel mainPanel = new JPanel(new BorderLayout());

            // Create the vertically centered panel for difficulty selector, checkboxes, and number inputs
            JPanel centerPanel = new JPanel();
            centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

            // Create the JComboBox and add the enum values
            JPanel difficultyPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            difficultyPanel.add(new JLabel("Select Difficulty:"));
            difficultyComboBox = new JComboBox<>(ClueLevel.values());
            difficultyPanel.add(difficultyComboBox);
            centerPanel.add(difficultyPanel);

            // Create the "Timer mode" and "Number of clues" checkboxes
            JPanel checkboxPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            timerModeCheckBox = new JCheckBox("Timer mode");
            clueLimitCheckBox = new JCheckBox("Number of clues");
            checkboxPanel.add(timerModeCheckBox);
            checkboxPanel.add(clueLimitCheckBox); // Add clueLimitCheckBox to the checkboxPanel
            centerPanel.add(checkboxPanel);

            // Create the input fields for time in minutes and the number of clues
            JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            timeInputField = new JTextField(5);
            clueLimitInputField = new JTextField(5);
            inputPanel.add(new JLabel("Time in minutes:"));
            inputPanel.add(timeInputField);
            inputPanel.add(new JLabel("Number of clues to solve:"));
            inputPanel.add(clueLimitInputField);
            centerPanel.add(inputPanel);

            mainPanel.add(centerPanel, BorderLayout.CENTER);

            // Create the confirm button at the absolute bottom
            JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            JButton confirmButton = new JButton("Confirm");
            confirmButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    selectedDifficulty = (ClueLevel) difficultyComboBox.getSelectedItem();
                    userResponse = JOptionPane.YES_OPTION;

                    if (timerModeCheckBox.isSelected() && !isValidPositiveInteger(timeInputField.getText())) {
                        JOptionPane.showMessageDialog(null, "Improper Input: Time must be a positive integer.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (clueLimitCheckBox.isSelected() && !isValidPositiveInteger(clueLimitInputField.getText())) {
                        JOptionPane.showMessageDialog(null, "Improper Input: Number of clues must be a positive integer.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Fire the ConfirmButtonEvent when the "Confirm" button is clicked
                    fireConfirmButtonEvent();
                }
            });
            bottomPanel.add(confirmButton);
            mainPanel.add(bottomPanel, BorderLayout.SOUTH);

            // Add the main content panel to the JFrame
            add(mainPanel);

            // Set a larger size for the JFrame
            setSize(400, 300);
            setLocationRelativeTo(null); // Center the window on the screen
            setVisible(true); // Make the JFrame visible after adding components
        }

    public int getUserResponse() {
        return userResponse;
    }

    public ClueLevel getSelectedDifficulty() {
        return selectedDifficulty;
    }

    public boolean isTimerMode() {
        return timerModeCheckBox.isSelected();
    }

    public boolean isClueLimitMode() {
        return clueLimitCheckBox.isSelected();
    }

    public int getTimeInMinutes() {
        try {
            return Integer.parseInt(timeInputField.getText());
        } catch (NumberFormatException e) {
            return -1; // Invalid input, return -1
        }
    }

        public int getClueLimit() { // Renamed from getNumberOfClues
            try {
                return Integer.parseInt(clueLimitInputField.getText());
            } catch (NumberFormatException e) {
                return -1; // Invalid input, return -1
            }
        }

    // Create a custom event for ConfirmButtonEvent
    private void fireConfirmButtonEvent() {
        ConfirmButtonEvent event = new ConfirmButtonEvent(this);
        // Notify any listeners that the "Confirm" button is clicked
        for (ConfirmButtonListener listener : confirmButtonListeners) {
            listener.confirmButtonClicked(event);
        }
    }

    // Listeners and their handling
    private java.util.List<ConfirmButtonListener> confirmButtonListeners = new java.util.ArrayList<>();

    public void addConfirmButtonListener(ConfirmButtonListener listener) {
        confirmButtonListeners.add(listener);
    }

    // Helper method to check if the input is a positive integer
    private boolean isValidPositiveInteger(String input) {
        try {
            int value = Integer.parseInt(input);
            return value > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    // Listeners and their handling

    // Create a custom event for ConfirmButtonEvent
    class ConfirmButtonEvent extends java.util.EventObject {
        public ConfirmButtonEvent(Object source) {
            super(source);
        }
    }

    // Define the ConfirmButtonListener interface
    interface ConfirmButtonListener extends java.util.EventListener {
        void confirmButtonClicked(ConfirmButtonEvent event);
    }
}
