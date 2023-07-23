import java.awt.*;
import javax.swing.*;
import java.time.Duration;
import java.time.Instant;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
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
    private FileWriter outputFileWriter; // FileWriter for output file

    public ClueSolver(ClueLevel difficulty, boolean timerMode, int timeLimitInMinutes, boolean clueLimitMode, int clueLimit) {
        this.difficulty = difficulty;
        this.timerMode = timerMode;
        this.timeLimitInMinutes = timeLimitInMinutes;
        this.clueLimitMode = clueLimitMode;
        this.clueLimit = clueLimit;

        previousClueCompletionTime = Instant.now();

        try {
            // Create the output file and FileWriter
            String outputFilePath = "ClueSolver_Output.txt";
            File outputFile = new File(outputFilePath);
            outputFileWriter = new FileWriter(outputFile);

            // Write the initial settings to the output file
            outputFileWriter.write("Difficulty: " + difficulty + "\n");
            outputFileWriter.write("Timer Mode: " + (timerMode ? "On" : "Off") + "\n");
            outputFileWriter.write("Time Limit: " + getTimeLimitInMinutes() + " minutes\n");
            outputFileWriter.write("Clue Limit Mode: " + (clueLimitMode ? "On" : "Off") + "\n");
            outputFileWriter.write("Clue Limit: " + clueLimit + "\n");
            outputFileWriter.write("\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
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
        // TODO this is the while loop and increments cluecounter
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


    private void displayProgress() {
        String modeInfo = "Difficulty: " + difficulty + "\n";
        modeInfo += "Timer Mode: " + (timerMode ? "On" : "Off") + "\n";
        modeInfo += "Time Limit: " + getTimeLimitInMinutes() + " minutes\n";
        modeInfo += "Clue Limit Mode: " + (clueLimitMode ? "On" : "Off") + "\n";
        modeInfo += "Clue Limit: " + clueLimit + "\n";

        Instant currentClueCompletionTime = Instant.now();
        Duration timeSincePreviousClue = Duration.between(previousClueCompletionTime, currentClueCompletionTime);

        String progressInfo = "Clues done: " + cluesDone + "\n";
        progressInfo += "Time Elapsed Since Previous Clue: " + timeSincePreviousClue.toMinutes() + " minutes\n";
        progressInfo += "Total Time Elapsed: " + totalTimeElapsed.toMinutes() + " minutes\n";
        progressInfo += "\n";

        // Append the progress details to the output file
        try {
            outputFileWriter.write(modeInfo);
            outputFileWriter.write(progressInfo);
            outputFileWriter.flush(); // Flush the buffer to ensure data is written immediately
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Show the progress in a popup
        // JOptionPane.showMessageDialog(null, modeInfo + progressInfo);
    }

    public void setDifficulty(ClueLevel difficulty) {
        this.difficulty = difficulty;
    }

    public void setTimerMode(boolean timerMode) {
        this.timerMode = timerMode;
    }

    public void setTimeLimitInMinutes(int timeLimitInMinutes) {
        this.timeLimitInMinutes = timeLimitInMinutes;
    }

    public void setClueLimitMode(boolean clueLimitMode) {
        this.clueLimitMode = clueLimitMode;
    }

    public void setClueLimit(int clueLimit) {
        this.clueLimit = clueLimit;
    }

    // Nested class to represent TimerExpiredException and ClueSolverErrorException
    private static class TimerExpiredException extends RuntimeException {
    }

    public static class ClueSolverErrorException extends RuntimeException {
        private final Exception originalException;

        public ClueSolverErrorException(Exception originalException) {
            this.originalException = originalException;
        }

        public Exception getOriginalException() {
            return originalException;
        }
    }
}

