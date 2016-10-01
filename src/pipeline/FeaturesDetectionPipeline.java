package pipeline;

import common.*;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;
import services.BoundingFilter;
import services.ColorService;
import services.DetectionService;
import services.FilterService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FeaturesDetectionPipeline extends OpencvUser {

    private DetectionService detectionService;
    private ColorService colorService;
    private FilterService filterService;

    public FeaturesDetectionPipeline(DetectionService detectionService, ColorService colorService,
                                     FilterService filterService){

        this.detectionService = detectionService;
        this.colorService = colorService;
        this.filterService = filterService;
    }

    public List<Rect> DetectPlate(Mat source) throws DetectedNothingException {
        List<MatOfPoint> contours = detectionService.DetectRectangles(source);
        List<BoundingFilter> filters = new ArrayList<>();
        filters.add(e->e.width > e.height * Constant.MIN_ASPECT_RATIO );
        filters.add(e->e.width < e.height * Constant.MAX_ASPECT_RATIO );
        filters.add(e->e.width >  Constant.MIN_WIDTH );
        contours = detectionService.Filter(contours,filters);

        if (contours.size() < 1){
            throw new DetectedNothingException();
        }
        return contours.stream().map(Imgproc::boundingRect).collect(Collectors.toList());
    }

    public Rect IdentifyPlateByDominantColor(Mat source) throws DetectedNothingException,DetectedManyException{
        Mat img = filterService.ReduceNoise(source,5);

        double[] dominantColor = colorService.GetDominantColor(img);
        Mat mask = colorService.GetMask(img,dominantColor);
        mask= filterService.Close(mask,4,1);
        List<Rect> rects = DetectPlate(mask);
        if (rects.size() > 1){
            throw new DetectedManyException();
        }
        return rects.get(0);
    }
}
