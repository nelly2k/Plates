package services;

import common.Constant;
import common.OpencvUser;
import org.opencv.core.*;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;

import java.util.LinkedList;
import java.util.List;

public class ExtractionService extends OpencvUser {

    public MatOfKeyPoint GetOrbKeypoints(Mat src){
        MatOfPoint descriptor = new MatOfPoint();
        MatOfKeyPoint keypoints = new MatOfKeyPoint();
        DoOrb(src, keypoints, descriptor);
        return keypoints;
    }

    public MatOfPoint GetOrbDescriptor(Mat src){
        MatOfPoint descriptor = new MatOfPoint();
        MatOfKeyPoint keypoints = new MatOfKeyPoint();

       DoOrb(src, keypoints, descriptor);

        return descriptor;
    }

    private void DoOrb(Mat src, MatOfKeyPoint keypoints, MatOfPoint descriptor){
        VerifyGray(src);
        FeatureDetector detector = FeatureDetector.create(FeatureDetector.ORB);

        detector.detect(src, keypoints);

        DescriptorExtractor extractor =  DescriptorExtractor.create(DescriptorExtractor.ORB);
        extractor.compute(src, keypoints,descriptor);
    }

    public boolean IsObjectOnScene(Mat scene, Mat obj){

        MatOfPoint objDescriptor = GetOrbDescriptor(obj);
        MatOfPoint sceneDescriptor= GetOrbDescriptor(scene);
        List<MatOfDMatch> allMatches = GetAllMatches(sceneDescriptor, objDescriptor);
        LinkedList<DMatch> filterByDistance = FilterMatchesByDistance(allMatches);

        return filterByDistance.size() >= Constant.EXTRACTION_MIN_MATCH_SIZE;
    }

    private List<MatOfDMatch> GetAllMatches(MatOfPoint sceneDescriptor, MatOfPoint objDescriptor){
        List<MatOfDMatch> matches = new LinkedList<>();
        DescriptorMatcher descriptorMatcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE);
        descriptorMatcher.knnMatch(objDescriptor, sceneDescriptor, matches, 2);

        return matches;
    }

    private LinkedList<DMatch> FilterMatchesByDistance(List<MatOfDMatch> matches){
        LinkedList<DMatch> result = new LinkedList<DMatch>();

        for (MatOfDMatch matofDMatch : matches) {

            DMatch[] dmatcharray = matofDMatch.toArray();

            DMatch m1 = dmatcharray[0];

            DMatch m2 = dmatcharray[1];

            if (m1.distance <= m2.distance * Constant.EXTRACTION_DISTANCE) {
                result.addLast(m1);
            }

        }
        return result;
    }



    void SupportedDetector(){
        for(int i=1; i<=12; i++){
            try{
                FeatureDetector detector = FeatureDetector.create(i);
                LOGGER.info("Number " + i + " supported");
            }catch (Exception ex){
                continue;
            }

        }
    }
}
