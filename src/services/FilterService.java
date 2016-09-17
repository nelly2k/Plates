package services;


import common.ImageAction;
import common.OpencvUser;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
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
        Imgproc.Sobel(source, result, -1, 1, 0);
        return result;
    }

    public Mat EdgeDetection(Mat source, int dx, int dy){
        VerifyGray(source);
        Mat result = NewMat(source);
        Imgproc.Sobel(source, result, -1, dx, dy);
        return result;
    }

    public Mat Threshold(Mat source){
        VerifyGray(source);
        Mat result = NewMat(source);
        Imgproc.threshold(source, result,127,255, Imgproc.THRESH_BINARY);
        return result;
    }

    public Mat Threshold(Mat source,int threshold , int maxValue){
        VerifyGray(source);
        Mat result = NewMat(source);
        Imgproc.threshold(source, result,threshold,maxValue, Imgproc.THRESH_BINARY);
        return result;
    }

    public Mat Close(Mat source){
        VerifyGray(source);
        Mat structuralElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(18,18));
        Mat result = NewMat(source);
        Imgproc.morphologyEx(source, result, Imgproc.MORPH_CLOSE,structuralElement);
        return result;
    }


    public Mat Close(Mat source, int kx, int ky){
        VerifyGray(source);
        Mat structuralElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(kx,ky));
        Mat result = NewMat(source);
        Imgproc.morphologyEx(source, result, Imgproc.MORPH_CLOSE,structuralElement);
        return result;
    }

    private void VerifyGray(Mat source){
        double[] pixel = source.get(0,0);
        if (pixel.length !=1){
            throw new IllegalArgumentException("Image should be converted to grayscale first");
        }

    }
    private Mat NewMat(Mat img) {
        return new Mat(img.height(), img.width(), CvType.CV_8UC1);
    }
}
