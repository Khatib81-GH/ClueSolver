import java.awt.*;
import javax.swing.*;
import java.time.Duration;
import java.time.Instant;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.imageio.ImageIO;

public class ClueSolver {
    private ClueLevel difficulty;
    private boolean timerMode;
    private int timeLimitInMinutes;
    private boolean clueLimitMode;
    private int clueLimit;
    private int cluesDone = 0;
    private Instant previousClueCompletionTime;
    private Duration totalTimeElapsed = Duration.ZERO;

    public ClueSolver(ClueLevel difficulty, boolean timerMode, int timeLimitInMinutes, boolean clueLimitMode, int clueLimit) {
        this.difficulty = difficulty;
        this.timerMode = timerMode;
        this.timeLimitInMinutes = timeLimitInMinutes;
        this.clueLimitMode = clueLimitMode;
        this.clueLimit = clueLimit;

        previousClueCompletionTime = Instant.now();
    }

    public void runClueSolver() {
        while (true) {
            try {
                // ... (code to solve clues)

                if (clueLimitMode && cluesDone >= clueLimit) {
                    displayClueLimitPopup();
                    break;
                }

                if (timerMode && totalTimeElapsed.toMinutes() >= getTimeLimitInMinutes()) {
                    throw new TimerExpiredException();
                }

                solveClue();

                // Update clue count and time elapsed
                cluesDone++;
                Instant currentClueCompletionTime = Instant.now();
                Duration timeSincePreviousClue = Duration.between(previousClueCompletionTime, currentClueCompletionTime);
                previousClueCompletionTime = currentClueCompletionTime;
                totalTimeElapsed = totalTimeElapsed.plus(timeSincePreviousClue);

                // Display progress after solving a clue
                displayProgress();

            } catch (TimerExpiredException e) {
                displayTimerExpiredPopup();
                break;
            } catch (Exception e) {
                displayErrorPopup();
                break;
            }
        }
    }

    private int getTimeLimitInMinutes() {
        // Implement this method to get the time limit in minutes for timer mode
        return this.timeLimitInMinutes > 0 ? this.timeLimitInMinutes : 0 ;
    }

    private void solveClue() {
        // Implement this method to solve a clue
    }

    private void displayClueLimitPopup() {
        JOptionPane.showMessageDialog(null,
                "All requested clues have been solved.\n"
                        + "Clues done: " + cluesDone + "\n"
                        + "Total time elapsed: " + totalTimeElapsed.toMinutes() + " minutes\n"
                        + "Average clue solve time: " + getAverageClueSolveTime() + " minutes");
    }

    private void displayTimerExpiredPopup() {
        JOptionPane.showMessageDialog(null,
                "Time limit has been exceeded.\n"
                        + "Clues done: " + cluesDone + "\n"
                        + "Total time elapsed: " + totalTimeElapsed.toMinutes() + " minutes\n"
                        + "Average clue solve time: " + getAverageClueSolveTime() + " minutes");
    }

    private void displayErrorPopup() {
        String screenshotPath = takeScreenshot();
        JOptionPane.showMessageDialog(null,
                "An ERROR occurred while solving clues. Please submit the following screenshot for further debugging:\n"
                        + screenshotPath + "\n"
                        + "Clues done: " + cluesDone + "\n"
                        + "Total time elapsed: " + totalTimeElapsed.toMinutes() + " minutes\n"
                        + "Average clue solve time: " + getAverageClueSolveTime() + " minutes");
    }

    private String takeScreenshot() {
        try {
            // Create a Robot to capture the screen
            Robot robot = new Robot();
            Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            BufferedImage screenshot = robot.createScreenCapture(screenRect);

            // Get the user's home directory and create the Images folder path
            String userHomeDir = System.getProperty("user.home");
            String imagesFolderPath = userHomeDir + File.separator + "Images";

            // Create the Images folder if it doesn't exist
            File imagesFolder = new File(imagesFolderPath);
            if (!imagesFolder.exists()) {
                imagesFolder.mkdir();
            }

            // Create a filename for the screenshot with a timestamp
            String timestamp = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss").format(LocalDateTime.now());
            String filename = "ClueSolverERROR_" + timestamp + ".png";

            // Save the screenshot to the Images folder
            File outputFile = new File(imagesFolderPath + File.separator + filename);
            ImageIO.write(screenshot, "png", outputFile);

            // Display a popup message with the screenshot save location
            JOptionPane.showMessageDialog(null, "An ERROR occurred. Please submit the screenshot for further debugging.\nScreenshot saved to: " + outputFile.getAbsolutePath(), "ERROR", JOptionPane.ERROR_MESSAGE);
            return outputFile.getAbsolutePath();
        } catch (AWTException | IOException ex) {
            // In case of any exception while taking the screenshot, display a popup message
            JOptionPane.showMessageDialog(null, "An ERROR occurred while capturing the screenshot.", "ERROR", JOptionPane.ERROR_MESSAGE);
            return "ERROR SCREENSHOT NOT TAKEN";
        }
    }

    public Duration getAverageClueSolveTime() {
        if (cluesDone == 0) {
            return Duration.ZERO;
        } else {
            return totalTimeElapsed.dividedBy(cluesDone);
        }
    }

    // Nested class to represent TimerExpiredException
    private static class TimerExpiredException extends RuntimeException {
    }


    private void displayProgress() {
        String modeInfo = "Difficulty: " + difficulty + "\n";

        if (timerMode) {
            modeInfo += "Timer Mode: On\n";
            modeInfo += "Time Limit: " + getTimeLimitInMinutes() + " minutes\n";
        } else {
            modeInfo += "Timer Mode: Off\n";
        }

        if (clueLimitMode) {
            modeInfo += "Clue Limit Mode: On\n";
            modeInfo += "Clue Limit: " + clueLimit + "\n";
        } else {
            modeInfo += "Clue Limit Mode: Off\n";
        }

        Instant currentClueCompletionTime = Instant.now();
        Duration timeSincePreviousClue = Duration.between(previousClueCompletionTime, currentClueCompletionTime);

        String progressInfo = "Clues done: " + cluesDone + "\n";
        progressInfo += "Time Elapsed Since Previous Clue: " + timeSincePreviousClue.toMinutes() + " minutes\n";
        progressInfo += "Total Time Elapsed: " + totalTimeElapsed.toMinutes() + " minutes";

        JOptionPane.showMessageDialog(null, modeInfo + progressInfo);
    }

}

