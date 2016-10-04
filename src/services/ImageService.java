package services;

import common.OpencvUser;
import org.opencv.core.*;
import org.opencv.features2d.Features2d;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

public class ImageService extends OpencvUser {

    public void Draw(Mat source, List<MatOfPoint> contours){
        Imgproc.drawContours(source, contours, -1, new Scalar(0,0,255),2);
    }

    public void Rect(Mat source, List<Rect> rects){
        Rect(source,rects, 0,0,0,0);
//        for(Rect rect : rects){
//            Imgproc.rectangle(source, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0,255,0));
//
//        }
    }

    public void Rect(Mat source, List<Rect> rects, int xOffset, int yOffset, int widthOffset, int heightOffset){
        for(Rect rect : rects){
            Imgproc.rectangle(source, new Point(rect.x + xOffset, rect.y + yOffset),
                    new Point(rect.x + rect.width + xOffset + widthOffset, rect.y + rect.height + yOffset + heightOffset), new Scalar(0,255,0));

        }
    }

    public void Rect(Mat source, Rect rect){
            Imgproc.rectangle(source, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0,0,255));
            Imgproc.putText(source, rect.width + "," + rect.height,  new Point(rect.x, rect.y + 5), 0, 0.3, new Scalar(255,0,255));
    }

    public Mat Crop(Mat source, Rect rect){
         return new Mat(source,rect);
    }

    public List<Mat> Crop(Mat source, List<Rect> rects){
        List<Mat> result = new ArrayList<>();
        for (Rect rect:rects){
            result.add(new Mat(source, rect));
        }

        return result;
    }

    public Mat Draw(Mat src, MatOfKeyPoint points){
        Mat result = NewMat(src);
        Features2d.drawKeypoints(src, points, result);

        return result;
    }
}
