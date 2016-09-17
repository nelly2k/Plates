package pipeline;

import common.DetectedManyException;
import common.DetectedNothingException;
import org.junit.Before;
import org.junit.Test;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import services.DetectionService;
import services.FileService;
import services.FilterService;
import services.ImageService;

import java.io.File;
import java.util.logging.Logger;

public class IntegrationTests {

    protected static final Logger LOGGER = Logger.getLogger( "Info");

    private final String TEST_DATA_RED = "testData/red.png";
    private final String TEST_DATA_TRAIN_1 = "testData/train/plate_type_a_1ABC000.png";
    private final String TEST_OUTPUT = "outputData/";


    private FilterService filterService;
    private FileService fileService;
    private DetectionService detectionService;
    private ImageService imageService;

    private ImageProcessingPipeline imageProcessingPipeline;
    private FeaturesDetectionPipeline featuresDetectionPipeline;


    @Before
    public void setUp() throws Exception {
        fileService = new FileService();
        filterService = new FilterService();
        detectionService = new DetectionService();
        imageService = new ImageService();

        imageProcessingPipeline = new ImageProcessingPipeline(filterService);
        featuresDetectionPipeline = new FeaturesDetectionPipeline(detectionService);
    }

    @Test
    public void processSimple_red() throws Exception {
        Mat red= fileService.LoadAsMatrix(TEST_DATA_RED);
        Mat result = imageProcessingPipeline.PricessSimple(red);
        fileService.Save(TEST_OUTPUT + "red_simple.png", result);
    }

    @Test
    public void processSimple_real() throws Exception {
        Mat red= fileService.LoadAsMatrix(TEST_DATA_TRAIN_1);
        Mat result = imageProcessingPipeline.PricessSimple(red);
        fileService.Save(TEST_OUTPUT + "real_simple.png", result);
    }

    @Test
    public void detectPlate() throws  Exception{
        Mat img= fileService.LoadAsMatrix(TEST_DATA_TRAIN_1);
        Mat result = imageProcessingPipeline.PricessSimple(img);

        Rect rect = featuresDetectionPipeline.DetectPlate(result);
        Mat plate = imageService.Crop(img, rect);

        fileService.Save(TEST_OUTPUT + "plate.png", plate);
    }

    @Test
    public void detectPlateBulk() throws Exception{
        File[] files = fileService.GetFileList("testData/train/", ".png");
        int filesCount = 0;
        int manyCount =0;
        int nothingCount = 0;

        for(File file : files){
            filesCount++;
            String fileName = file.getName();
            String filePath = "testData/train/" + fileName;
            Mat img= fileService.LoadAsMatrix(filePath);
            Rect trainRect = fileService.LoadRect(filePath);

            Mat result = imageProcessingPipeline.PricessSimple(img);
            try{
                Rect rect = featuresDetectionPipeline.DetectPlate(result);
                LOGGER.info("Found [w:%1$d] [h:%1$d] ");
                Mat plate = imageService.Crop(img, rect);
                fileService.Save(TEST_OUTPUT + fileName , plate);
            }
            catch (DetectedManyException mex){
                LOGGER.warning("Detected many on " + fileName);
                manyCount++;
            }
            catch(DetectedNothingException nex){
                LOGGER.warning("Detected nothing on " + fileName);
                nothingCount++;
            }

        }

        LOGGER.info("Files " + filesCount);
        LOGGER.info("Many " + manyCount);
        LOGGER.info("Nothing " + nothingCount);

    }
}
