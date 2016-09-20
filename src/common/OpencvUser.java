package common;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

public class OpencvUser extends BaseClass {

    static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }

    protected Mat NewMat(Mat img) {
        return new Mat(img.height(), img.width(), CvType.CV_8UC1);
    }

}
