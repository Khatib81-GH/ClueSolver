import org.opencv.core.*;
import org.opencv.dnn.*;
import org.opencv.imgproc.Imgproc;

public class ObjectDetection {
    private Net net;
    private Mat currentFrame;

    public ObjectDetection(String modelWeights, String modelConfig) {
        // Load the YOLOv3 model
        net = Dnn.readNetFromDarknet(modelConfig, modelWeights);
    }

    public void detectObjects(Mat frame) {
        // Preprocess the frame
        Mat blob = Dnn.blobFromImage(frame, 1.0, new Size(416, 416), new Scalar(0, 0, 0), true, false);
        net.setInput(blob);

        // Run the forward pass to get the output
        Mat detections = net.forward();

        // Get the number of detections (number of rows in the detections Mat)
        int numDetections = detections.rows();

        // Get the number of elements per detection (number of columns in the detections Mat)
        int numElementsPerDetection = detections.cols();

        // Process the detections and draw bounding boxes
        for (int i = 0; i < numDetections; i++) {
            // Get the confidence value at index 5 for each detection
            double confidence = detections.get(i, 5)[0];

            // Rest of the code remains unchanged
            if (confidence > 0.5) {
                // Extract bounding box coordinates and draw the bounding box on the frame
                double[] data = new double[numElementsPerDetection];
                detections.get(i, 0, data);
                int centerX = (int) (data[0] * frame.cols());
                int centerY = (int) (data[1] * frame.rows());
                int width = (int) (data[2] * frame.cols());
                int height = (int) (data[3] * frame.rows());

                Point topLeft = new Point(centerX - width / 2, centerY - height / 2);
                Point bottomRight = new Point(centerX + width / 2, centerY + height / 2);
                Imgproc.rectangle(frame, topLeft, bottomRight, new Scalar(0, 255, 0), 2);
            }
        }

        // Update the currentFrame with the processed frame
        currentFrame = frame;
    }

    public Mat getCurrentFrame() {
        return currentFrame;
    }
}
