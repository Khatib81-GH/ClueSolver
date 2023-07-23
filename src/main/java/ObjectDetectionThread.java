import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;

public class ObjectDetectionThread implements Runnable {
    private ObjectDetection objectDetection;
    private BufferedImage currentFrame;

    public ObjectDetectionThread(BufferedImage initialFrame) {
        this.currentFrame = initialFrame;
        objectDetection = new ObjectDetection("yolov3/yolov3.weights", "yolov3/yolov3.cfg");

        // Start the timer to capture a new frame every 100 ms
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                captureScreen();
                detectObjects();
            }
        }, 0, 100);
    }

    @Override
    public void run() {
        // Do any initialization or setup here if needed
    }

    public void updateFrame(BufferedImage newFrame) {
        this.currentFrame = newFrame;
    }

    private void captureScreen() {
        try {
            this.currentFrame = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    private void detectObjects() {
        // Convert the BufferedImage to Mat format
        Mat frame = bufferedImageToMat(currentFrame);

        // Perform object detection on the current frame
        objectDetection.detectObjects(frame);

        // Display the frame in the named window
        HighGui.imshow("Object Detection", objectDetection.getCurrentFrame());

        // Check for key press to exit
        if (HighGui.waitKey(1) == 27) {
            HighGui.destroyAllWindows();
        }
    }
    public Mat getCurrentProcessedFrame() {
        return bufferedImageToMat(currentFrame);
    }

    private Mat bufferedImageToMat(BufferedImage image) {
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

    private BufferedImage matToBufferedImage(Mat mat) {
        int width = mat.cols();
        int height = mat.rows();
        int channels = mat.channels();
        byte[] data = new byte[width * height * channels];
        mat.get(0, 0, data);

        int[] pixels = new int[width * height];
        int index = 0;
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                int b = data[index] & 0xff;
                int g = data[index + height * width] & 0xff;
                int r = data[index + 2 * height * width] & 0xff;
                pixels[index] = 0xff000000 | (r << 16) | (g << 8) | b;
                index++;
            }
        }

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        image.setRGB(0, 0, width, height, pixels, 0, width);
        return image;
    }
}
