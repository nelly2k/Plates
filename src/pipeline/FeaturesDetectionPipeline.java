package pipeline;

import common.*;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;
import services.BoundingFilter;
import services.DetectionService;
import services.ImageService;

import java.util.ArrayList;
import java.util.List;

public class FeaturesDetectionPipeline extends OpencvUser {

    private DetectionService detectionService;

    public FeaturesDetectionPipeline(DetectionService detectionService){

        this.detectionService = detectionService;
    }

    public Rect DetectPlate(Mat source) throws DetectedManyException, DetectedNothingException {
        List<MatOfPoint> contours = detectionService.DetectRectangles(source);
        List<BoundingFilter> filters = new ArrayList<>();
        filters.add(e->e.width > e.height * Constant.MIN_ASPECT_RATIO );
        filters.add(e->e.width < e.height * Constant.MAX_ASPECT_RATIO );
        filters.add(e->e.width <  Constant.MAX_WIDTH );
        filters.add(e->e.width >  Constant.MIN_WIDTH );
        contours = detectionService.Filter(contours,filters);

        if (contours.size() > 1)
        {
            throw new DetectedManyException();
        }

        if (contours.size() < 1){
            throw new DetectedNothingException();
        }

        return Imgproc.boundingRect(contours.get(0));
    }
}
