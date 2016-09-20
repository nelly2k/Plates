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
         return new Mat(source,rect);
    }
}
