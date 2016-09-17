package services;

import common.OpencvUser;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import java.util.List;

public class ImageService extends OpencvUser {

    public void Contours(Mat source, List<MatOfPoint> contours){
        Imgproc.drawContours(source, contours, -1, new Scalar(0,0,255),4);
    }

    public Mat Crop(Mat source, Rect rect){
    //    Mat result = source.clone();

      // Imgproc.rectangle(result, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height),
       //         new Scalar(0, 255, 0));

       // return new Rect(rect.x, rect.y, rect.width, rect.height);

        return new Mat(source,rect);
    }
}
