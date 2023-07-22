import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI extends JFrame {
    private JComboBox<ClueLevel> difficultyComboBox;
    private int userResponse = -1;
    private ClueLevel selectedDifficulty;

    public GUI() {
        // Set up the JFrame properties
        setTitle("Difficulty Selector");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        // Create the JComboBox and add the enum values
        difficultyComboBox = new JComboBox<>(ClueLevel.values());
        add(difficultyComboBox);

        // Create the "Confirm" button
        JButton confirmButton = new JButton("Confirm");
        add(confirmButton);

        // Add the ActionListener to the "Confirm" button
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedDifficulty = (ClueLevel) difficultyComboBox.getSelectedItem();
                userResponse = JOptionPane.YES_OPTION;
                // Fire the ConfirmButtonEvent when the "Confirm" button is clicked
                fireConfirmButtonEvent();
            }
        });

        // Pack the components to set the appropriate size
        pack();
        setSize(400, 200);
        setLocationRelativeTo(null); // Center the window on the screen
        setVisible(true); // Make the JFrame visible after adding components
    }

    public int getUserResponse() {
        return userResponse;
    }

    public ClueLevel getSelectedDifficulty() {
        return selectedDifficulty;
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
}

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
