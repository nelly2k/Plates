package services;


import common.OpencvUser;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class FilterService extends OpencvUser {

    public Mat ToGray(Mat source){
        Mat result = NewMat(source);
        Imgproc.cvtColor(source, result, Imgproc.COLOR_RGB2GRAY);
        return result;
    }

    public Mat ReduceNoise(Mat source, int ksize ){
        Mat result = NewMat(source);
        Imgproc.medianBlur(source, result, ksize);

        return result;
    }

    private Mat NewMat(Mat img) {
        return new Mat(img.height(), img.width(), CvType.CV_8UC1);
    }
}
