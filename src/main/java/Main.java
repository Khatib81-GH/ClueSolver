import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.videoio.VideoCapture;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.SwingUtilities;
public class Main {
    public static void main(String[] args) {
        // Load the OpenCV native library
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // Create a window to display the video frames
        String windowName = "Object Detection";
        HighGui.namedWindow(windowName);

        // Create an instance of the ObjectDetection class
        ObjectDetection objectDetection = new ObjectDetection("yolov3/yolov3.weights", "yolov3/yolov3.cfg");

        // Process each frame in real-time
        Mat frame = new Mat();
        while (true) {
            // Capture a screenshot of the computer screen
            BufferedImage screenshot = captureScreen();

            // Convert the screenshot to OpenCV Mat format
            frame = bufferedImageToMat(screenshot);

            // Perform object detection
            objectDetection.detectObjects(frame);

            // Display the frame in the named window
            HighGui.imshow(windowName, frame);

            // Check for key press to exit
            if (HighGui.waitKey(1) == 27) {
                break;
            }
        }

        // Destroy the window
        HighGui.destroyAllWindows();
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

    // Method to convert a BufferedImage to an OpenCV Mat object
    private static Mat bufferedImageToMat(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int[] pixels = image.getRGB(0, 0, width, height, null, 0, width);

        Mat mat = new Mat(height, width, CvType.CV_8UC3);
        byte[] data = new byte[width * height * (int) mat.elemSize()];
        int channels = mat.channels();
        int pixelLength = channels * width;

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int pixel = pixels[i * width + j];
                for (int k = 0; k < channels; k++) {
                    data[(i * pixelLength) + (j * channels) + k] = (byte) ((pixel >> (channels - k - 1) * 8) & 0xFF);
                }
            }
        }

        mat.put(0, 0, data);
        return mat;
    }
}
