package services;

import common.OpencvUser;
import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.FeatureDetector;

/**
 * Created by Nelli on 1/10/2016.
 */
public class ExtractionService extends OpencvUser {


    public MatOfKeyPoint GetSiftKeypoints(Mat src){
        VerifyGray(src);
        FeatureDetector detector = FeatureDetector.create(FeatureDetector.SURF);

        System.out.println("Detecting key points...");
        MatOfKeyPoint objectKeyPoints = new MatOfKeyPoint();
        detector.detect(src, objectKeyPoints);


        MatOfKeyPoint objectDescriptors = new MatOfKeyPoint();
        DescriptorExtractor extractor =  DescriptorExtractor.create(DescriptorExtractor.SURF);
        extractor.compute(src, objectKeyPoints,objectDescriptors);

        return objectKeyPoints;
    }

}
