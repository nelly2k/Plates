package services;

import common.OpencvUser;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

public class DetectionService   extends OpencvUser {


    public  List<MatOfPoint> DetectRectangles(Mat source){
        List<MatOfPoint> contours = new ArrayList<>();
        Mat im2 = new Mat();
        Imgproc.findContours(source, contours, im2, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_NONE);
        return contours;
    }

    public List<MatOfPoint> Filter(List<MatOfPoint> contours, List<BoundingFilter> filters){
        List<MatOfPoint> result = new ArrayList<>();

        for(MatOfPoint contour :contours){
            Rect rect = Imgproc.boundingRect(contour);
            Boolean good = true;
                for(BoundingFilter filter : filters){
                    good = good && filter.Filter(rect);
                }
            if (good){
                result.add(contour);
            }
        }
        return result;
    }
}

