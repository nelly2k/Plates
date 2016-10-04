package services;

import common.BaseTestResolved;
import common.Constant;
import org.junit.Assert;
import org.junit.Test;
import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;
import pipeline.MaskExtractionResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class ExtractionServiceTest extends BaseTestResolved {

    @Test
    public void getOrbKeypoints() throws Exception {
        Mat src = fileService.LoadAsMatrix(TEST_DATA + "04/prac04ex01img01.png");
        Assert.assertNotNull(src);
        Mat gray = filterService.ToGray(src);
        MatOfKeyPoint mapKeyPoints = extractionService.GetOrbKeypoints(gray);
        Mat result = imageService.Draw(src,mapKeyPoints);

        fileService.Save(OUTPUT_PATH + "orbExtraction.png", result);
    }

    @Test
    public void MatchFound() throws Exception {
        Mat scene = filterService.ToGray(fileService.LoadAsMatrix(TEST_DATA + "04/prac04ex01img02.png"));
        Mat obj = filterService.ToGray(fileService.LoadAsMatrix(TEST_DATA + "04/prac04ex01img01.png"));

        Boolean result = extractionService.IsObjectOnScene(scene, obj);

        Assert.assertTrue(result);
    }

    @Test
    public void MatchNotFound() throws Exception {
        Mat scene = filterService.ToGray(fileService.LoadAsMatrix(TEST_DATA + "04/prac04ex01img02.png"));
        Mat obj = filterService.ToGray(fileService.LoadAsMatrix(TEST_DATA + "square.jpg"));

        Boolean result = extractionService.IsObjectOnScene(scene, obj);

        Assert.assertFalse(result);
    }

    @Test
    public void whichSupported(){
        extractionService.SupportedDetector();
    }

    @Test
    public void FindLines() throws  Exception{
        Mat src = fileService.LoadAsMatrix(TEST_DATA + "task2/plate02.png");
        Mat gray = filterService.ToGray(src);
        Mat lines = detectionService.DetectLines(gray);

        for (int i =0; i<lines.size().height;i++){
            double rho = lines.get(i,0)[0];
            double theta  = lines.get(i,0)[1];

            LOGGER.info("Rho " + rho + " theta " + theta);
        }

    }

    @Test
    public void BinaryShapeAnalysis() throws Exception {
        Mat src = fileService.LoadAsMatrix(TEST_DATA + "task2/plate01.png");

        Mat mask = imageProcessingPipeline.GetMask(src);
        MaskExtractionResult extractionResult = featuresDetectionPipeline.ExtractMask(src, mask);

        Mat processed = imageProcessingPipeline.ProcessForBinaryAnalysis(extractionResult.Image());

        fileService.Save(OUTPUT_PATH +"/task2_mask.png", processed);

        List<Rect> rects = featuresDetectionPipeline.DetectChar(processed);

        Rect offset = extractionResult.Offset();
        imageService.Rect(src, rects, offset.x -2, offset.y - 3, 2,3);

        fileService.Save(OUTPUT_PATH +"/task2_bsa.png", src);
    }

    @Test
    public void InPipeline() throws Exception {
        Mat src = fileService.LoadAsMatrix(TEST_DATA + "04/prac04ex02img01.png");
        Mat processedImg = imageProcessingPipeline.ProcessForBinaryAnalysis(src);
        List<Rect> rects = featuresDetectionPipeline.DetectChar(processedImg);

        List<Mat> chars = imageService.Crop(src, rects);

        fileService.Save(OUTPUT_PATH +"chrs/04.png", chars);

    }


}