package common;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

public class OpencvUser extends BaseClass {

    static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }

    protected Mat NewMat(Mat img) {
        return new Mat(img.height(), img.width(), CvType.CV_8UC1);
    }


    protected void VerifyGray(Mat source){
        double[] pixel = source.get(0,0);
        if (pixel.length !=1){
            throw new IllegalArgumentException("Image should be converted to grayscale first");
        }

    }
}
