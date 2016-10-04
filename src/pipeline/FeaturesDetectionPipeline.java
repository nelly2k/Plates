package pipeline;

import common.*;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;
import services.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class FeaturesDetectionPipeline extends OpencvUser {

    private DetectionService detectionService;
    private ColorService colorService;
    private FilterService filterService;
    private ImageService imageService;

    public FeaturesDetectionPipeline(DetectionService detectionService, ColorService colorService,
                                     FilterService filterService, ImageService imageService){

        this.detectionService = detectionService;
        this.colorService = colorService;
        this.filterService = filterService;
        this.imageService = imageService;
    }

    public List<Rect> DetectPlate(Mat source) throws DetectedNothingException {
        List<MatOfPoint> contours = detectionService.DetectContours(source);
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

    public List<Rect> DetectChar(Mat source) throws DetectedNothingException {
        List<MatOfPoint> contours = detectionService.DetectContours(source);
        List<BoundingFilter> filters = new ArrayList<>();
        filters.add(e->e.height > e.width * Constant.MIN_CHR_ASPECT_RATIO );
        filters.add(e->e.height < e.width * Constant.MAX_CHR_ASPECT_RATIO );

        filters.add(e->e.width >  Constant.MIN_CHR_WIDTH);
        filters.add(e->e.width <  Constant.MAX_CHR_WIDTH);
        contours = detectionService.Filter(contours,filters);

        if (contours.size() < 1){
            throw new DetectedNothingException();
        }
        return contours.stream().map(Imgproc::boundingRect).sorted((f1,f2)-> Integer.compare(f1.x,f2.x)).collect(Collectors.toList());
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

    public MaskExtractionResult ExtractMask(Mat src, Mat mask) throws Exception {
        List<Rect> myPlatesRect = DetectPlate(mask);

        Optional<Rect> widest = myPlatesRect.stream().sorted((f1, f2)->Integer.compare(f1.width,f2.width)).findFirst();
        if (!widest.isPresent()){
            throw new Exception("Need to think about it : " + myPlatesRect.size());
        }
        Mat cropped = imageService.Crop(src, widest.get());

        return new MaskExtractionResult(cropped, widest.get());
    }
}

