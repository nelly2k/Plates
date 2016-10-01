package services;
import common.OpencvUser;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

import static org.opencv.core.CvType.CV_8U;
import static org.opencv.core.CvType.CV_8UC3;

public class FilterService extends OpencvUser {

    public Mat ToGray(Mat source){
        Mat result = NewMat(source);
        Imgproc.cvtColor(source, result, Imgproc.COLOR_RGB2GRAY);
        return result;
    }

    public Mat ReduceNoise(Mat source, int ksize ){
        Mat result = NewMat(source);
        Imgproc.medianBlur(source, result, ksize);
        //Imgproc.blur(result,result, new Size(5,5));
        return result;
    }

    public Mat PixelTransform(Mat source, double a, double b){
        VerifyGray(source);

        Mat result = NewMat(source);
        for(int row = 0; row < result.rows(); row++){
            for(int col = 0; col < result.cols(); col++){
                double[] pixel = source.get(row, col);
                pixel[0] = a * pixel[0] + b;

                result.put(row,col, pixel);
            }
        }

        return result;
    }

    public Mat EdgeDetection(Mat source){
        VerifyGray(source);
        Mat result = NewMat(source);
        Imgproc.Sobel(source, result,  CV_8U, 1, 0);
        return result;
    }

    public Mat Threshold(Mat source){
        VerifyGray(source);
        Mat result = NewMat(source);
        Imgproc.threshold(source, result,0,255, Imgproc.THRESH_BINARY + Imgproc.THRESH_OTSU);
        return result;
    }

    public Mat Close(Mat source){
        VerifyGray(source);
        Mat structuralElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(26,2));
        Mat result = NewMat(source);
        Imgproc.morphologyEx(source, result, Imgproc.MORPH_CLOSE,structuralElement);
        return result;
    }

    public Mat Close(Mat source, int hor, int ver){
        VerifyGray(source);
        Mat structuralElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(hor,ver));
        Mat result = NewMat(source);
        Imgproc.morphologyEx(source, result, Imgproc.MORPH_CLOSE,structuralElement);
        return result;
    }


}
