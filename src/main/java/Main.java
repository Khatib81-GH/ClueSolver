import org.opencv.core.*;
import org.opencv.highgui.HighGui;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Load the OpenCV native library
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // Capture a screenshot of the computer screen
        BufferedImage screenshot = captureScreen();

        // Create and display the GUI to get user input
        GUI gui = new GUI();
        gui.addConfirmButtonListener(new GUI.ConfirmButtonListener() {
            @Override
            public void confirmButtonClicked(GUI.ConfirmButtonEvent event) {
                // Get user input from the GUI (omitted for brevity)
                // ...

                // Close the GUI after getting the input
                gui.dispose();

                // Create an instance of the ObjectDetectionThread and start the thread
                ObjectDetectionThread objectDetectionThread = new ObjectDetectionThread(screenshot);
                Thread thread = new Thread(objectDetectionThread);
                thread.start();
            }
        });


    }

    // Method to capture the computer screen as a BufferedImage
    private static BufferedImage captureScreen() {
        try {
            return new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
        } catch (AWTException e) {
            e.printStackTrace();
            return null;
        }
    }
}
