import org.opencv.core.*;
import org.opencv.dnn.*;
import org.opencv.imgproc.Imgproc;

public class ObjectDetection {
    private Net net;

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

        // Process the detections and draw bounding boxes
        for (int i = 0; i < detections.rows(); i++) {
            double confidence = detections.get(i, 5)[0]; // Use double for confidence (32-bit floating-point)

            // Rest of the code remains unchanged
            if (confidence > 0.5) {
                double[] data = new double[4];
                detections.get(i, 0, data);
                int centerX = (int) (data[0] * frame.cols());
                int centerY = (int) (data[1] * frame.rows());
                int width = (int) (data[2] * frame.cols());
                int height = (int) (data[3] * frame.rows());

                // Draw the bounding box on the frame
                Point topLeft = new Point(centerX - width / 2, centerY - height / 2);
                Point bottomRight = new Point(centerX + width / 2, centerY + height / 2);
                Imgproc.rectangle(frame, topLeft, bottomRight, new Scalar(0, 255, 0), 2);
            }
        }
    }

}
