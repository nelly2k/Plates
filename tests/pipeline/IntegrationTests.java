package pipeline;

import common.DetectedManyException;
import common.DetectedNothingException;
import org.junit.Before;
import org.junit.Test;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import services.DetectionService;
import services.FileService;
import services.FilterService;
import services.ImageService;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public void detectPlateBulk() throws Exception{
        String ONE = "foundOne";
        String MANY = "foundMany";
        String NONE = "foundNone";

        String CORRECT_PLACE = "correctPlace";
        String CORRECT_ALL = "correctAll";
        String MANY_CORRECT = "foundManyCorrect";

        int maxOffset = 30;

        fileService.ClearFolder(TEST_OUTPUT + "detected");
        fileService.ClearFolder(TEST_OUTPUT + "nothing");
        fileService.ClearFolder(TEST_OUTPUT + "many");

        File[] files = fileService.GetFileList("testData/train/", ".png");

        Map<String, Integer> metrics = new HashMap<String, Integer>();

        metrics.put(ONE,0);
        metrics.put(MANY,0);
        metrics.put(NONE,0);
        metrics.put(CORRECT_PLACE,0);
        metrics.put(CORRECT_ALL,0);

        for(File file : files){

            String fileName = file.getName();
            String filePath = "testData/train/" + fileName;
            Mat img= fileService.LoadAsMatrix(filePath);
            Mat result = imageProcessingPipeline.PricessSimple(img);
            int[] expectedPlace = fileService.LoadCoordinates(filePath);
            try{
                List<Rect> rects = featuresDetectionPipeline.DetectPlate(result);
                String path =  TEST_OUTPUT+"detected/" +  fileName.substring(0, fileName.length() - 4);
                int counter = 0;
                if (rects.size() == 1){

                    metrics.put(ONE,(metrics.get(ONE) + 1));
                }
                if (rects.size() > 1){
                    metrics.put(MANY,(metrics.get(MANY) + 1));
                }

                for (Rect rect:rects) {
                    if (Math.abs(expectedPlace[0] - rect.x) <= maxOffset
                            && Math.abs(expectedPlace[1] - rect.y) <= maxOffset
                            && Math.abs(expectedPlace[2] - (rect.x + rect.width)) <= maxOffset
                            && Math.abs(expectedPlace[3] - (rect.y + rect.height)) <= maxOffset
                            ){
                        metrics.put(CORRECT_ALL,(metrics.get(CORRECT_ALL) + 1));

                    }else if (Math.abs(expectedPlace[0] - rect.x) <= maxOffset
                            && Math.abs(expectedPlace[1] - rect.y) <= maxOffset){
                        metrics.put(CORRECT_PLACE,(metrics.get(CORRECT_PLACE) + 1));
                    }

                    Mat plate = imageService.Crop(img, rect);
                    fileService.Save(path + (counter==0?"":counter) + ".png" , plate);
                    counter++;
                }

            }
            catch(DetectedNothingException nex){
                LOGGER.warning("Detected nothing on " + fileName);
                fileService.Save(TEST_OUTPUT  + "nothing/" +  fileName , img);
                metrics.put(NONE,(metrics.get(NONE) + 1));
            }

        }

        LOGGER.info("One " + metrics.get(ONE));
        LOGGER.info("Many " + metrics.get(MANY));
        LOGGER.info("Nothing " + metrics.get(NONE));
        LOGGER.info("Correct place " + metrics.get(CORRECT_PLACE));
        LOGGER.info("Correct everything " + metrics.get(CORRECT_ALL));

    }

    @Test
    public void replayNothing() throws Exception{
        String nothingPath = TEST_OUTPUT +"nothing/";
        File[] files = fileService.GetFileList(nothingPath, ".png");

        for(File file : files) {
            String fileName = file.getName();
            int counter = 0;

            Mat source= fileService.LoadAsMatrix(nothingPath + fileName);

            List<Mat> imgs = imageProcessingPipeline.ProcessSimple(source);
            String filePath = nothingPath + fileName.substring(0, fileName.length() -4)  + "/";
            new File(filePath).mkdirs();
            for (Mat img: imgs) {

                fileService.Save(filePath + fileName.substring(0, fileName.length() -4) + counter + ".png", img );
                counter++;
            }

            Mat lastImg =  imageProcessingPipeline.PricessSimple(source);
            List<MatOfPoint> contours = detectionService.DetectRectangles(lastImg);

            imageService.Contours(source, contours);
            fileService.Save(filePath + fileName.substring(0, fileName.length() -4) + "contours.png", source );
        }

    }
}
