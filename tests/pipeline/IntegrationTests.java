package pipeline;

import common.BaseTestResolved;
import common.DetectedManyException;
import common.DetectedNothingException;
import org.junit.Test;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IntegrationTests extends BaseTestResolved {

    private final String TEST_DATA_RED = "testData/red.png";
    private final String TEST_DATA_TRAIN_1 = "testData/train/plate_type_a_1ABC000.png";
    private final String TEST_OUTPUT = "outputData/";

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
                boolean isOneDetected = false;

                for (Rect rect:rects) {

                    Mat plate = imageService.Crop(img, rect);
                    try{
                        Rect identifiedRect = featuresDetectionPipeline.IdentifyPlateByDominantColor(plate);
                        Mat identifiedPlate  = imageService.Crop(plate, identifiedRect);
                        isOneDetected= true;
                        Rect newRect = new Rect(rect.x + identifiedRect.x, rect.y + identifiedRect.y, identifiedRect.width, identifiedRect.height);
                        if (Math.abs(expectedPlace[0] - newRect.x) <= maxOffset
                                && Math.abs(expectedPlace[1] - newRect.y) <= maxOffset
                                && Math.abs(expectedPlace[2] - (newRect.x + newRect.width)) <= maxOffset
                                && Math.abs(expectedPlace[3] - (newRect.y + newRect.height)) <= maxOffset
                                ){
                            metrics.put(CORRECT_ALL,(metrics.get(CORRECT_ALL) + 1));

                        }else if (Math.abs(expectedPlace[0] - newRect.x) <= maxOffset
                                && Math.abs(expectedPlace[1] - newRect.y) <= maxOffset){
                            metrics.put(CORRECT_PLACE,(metrics.get(CORRECT_PLACE) + 1));
                        }


                        fileService.Save(path + (counter==0?"":counter) + ".png" , identifiedPlate);

                    }catch (DetectedManyException mex){
                        fileService.Save(TEST_OUTPUT  + "many/" +  fileName , img);
                            metrics.put(MANY,(metrics.get(MANY) + 1));


                        //LOGGER.warning("Identified many on " + fileName);
                    }catch (DetectedNothingException nex){
                        fileService.Save(TEST_OUTPUT  + "nothing/" +  fileName , img);
                        metrics.put(NONE,(metrics.get(NONE) + 1));

                    }

                    counter++;
                }

            }
            catch(DetectedNothingException nex){
                metrics.put(NONE,(metrics.get(NONE) + 1));
             //   fileService.Save(TEST_OUTPUT  + "nothing/" +  fileName , img);
              //
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
            LOGGER.info(fileName);
            try{
                List<Rect> detectedPlates = featuresDetectionPipeline.DetectPlate(lastImg);
            }
            catch(DetectedNothingException mes){
                LOGGER.info("not detected");
            }

            List<MatOfPoint> contours = detectionService.DetectContours(lastImg);
            for(MatOfPoint contour: contours ){
                Rect rect = Imgproc.boundingRect(contour);
                imageService.Rect(source, rect);
            }
            //imageService.Draw(source, contours);
            fileService.Save(filePath + fileName.substring(0, fileName.length() -4) + "contours.png", source );
        }

    }

    @Test
    public void Task2() throws Exception {

        String sourcePath = "testData/task2/";
        String targetPath = "outputData/task2/";

        fileService.ClearFolder(targetPath);
        File[] files = fileService.GetFileList(sourcePath, ".png");
        for (File file : files) {
            Mat src = fileService.LoadAsMatrix(sourcePath + file.getName());

            try {


                Mat processedImg = imageProcessingPipeline.ProcessForBinaryAnalysis(src);
                List<Rect> rects = featuresDetectionPipeline.DetectChar(processedImg);

                List<Mat> chars = imageService.Crop(src, rects);

                fileService.Save(targetPath + file.getName(), chars);
            } catch (DetectedNothingException ex) {
                LOGGER.info("Nothing detected on " + file.getName());
            }

        }
    }

}
