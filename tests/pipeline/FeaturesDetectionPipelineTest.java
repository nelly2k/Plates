package pipeline;

import common.BaseTest;
import common.DetectedNothingException;
import org.junit.Before;
import org.junit.Test;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import services.*;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Nelli on 26/09/2016.
 */
public class FeaturesDetectionPipelineTest extends BaseTest {

    private final String TEST_DATA_NO_PLATE = TEST_DATA + "plate_type_a_1DUG719.png";
    private final String TEST_DATA_PLATE = TEST_DATA + "plate_type_a_1DUG7191.png";
    private final String TEST_DATA_PLATE1 = TEST_DATA + "plate_type_a_1BEI491.png";
    FeaturesDetectionPipeline featuresDetectionPipeline;

    FileService fileService;
    ImageService imageService;
    FilterService filterService;
    ColorService colorService;
DetectionService detectionService;

    @Before
    public void setup(){
        fileService = new FileService();
        imageService= new ImageService();
        filterService = new FilterService();
        colorService = new ColorService();
        detectionService = new DetectionService();
        featuresDetectionPipeline = new FeaturesDetectionPipeline(new DetectionService(), new ColorService(),
                new FilterService(), new ImageService());
    }

    @Test
    public void identifyPlateByDominantColor() throws Exception {
        Mat mat = fileService.LoadAsMatrix(TEST_DATA_PLATE);
        Rect rect = featuresDetectionPipeline.IdentifyPlateByDominantColor(mat);
        Mat plate = imageService.Crop(mat, rect);
        fileService.Save(OUTPUT_PATH + "identifiedPlate.png", plate);
    }


    @Test
    public void identifyPlateByDominantColor1() throws Exception {
        Mat mat = fileService.LoadAsMatrix(TEST_DATA_PLATE1);
//        Rect rect = featuresDetectionPipeline.IdentifyPlateByDominantColor(mat);
//        Mat plate = imageService.Crop(mat, rect);
//        fileService.Save(OUTPUT_PATH + "identifiedPlate1.png", plate);

        Mat img = filterService.ReduceNoise(mat,5);

        double[] dominantColor = colorService.GetDominantColor(img);
        Mat mask = colorService.GetMask(img,dominantColor);
        mask= filterService.Close(mask,4,1);
        fileService.Save(OUTPUT_PATH + "identifiedPlateMask.png", mask);
        List<Rect> rects = featuresDetectionPipeline.DetectPlate(mask);

        imageService.Rect(mat, rects);
        fileService.Save(OUTPUT_PATH + "identifiedPlateRects.png", mat);
    }

    @Test(expected = DetectedNothingException.class)
    public void identifyPlateByDominantColorNoPlate() throws Exception {
        Mat mat = fileService.LoadAsMatrix(TEST_DATA_NO_PLATE);
        Rect rect = featuresDetectionPipeline.IdentifyPlateByDominantColor(mat);
        Mat plate = imageService.Crop(mat, rect);
        fileService.Save(OUTPUT_PATH + "identifiedNoPlate.png", plate);
    }


}