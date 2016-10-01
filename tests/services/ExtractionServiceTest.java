package services;

import common.BaseTest;
import common.BaseTestResolved;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;

import static org.junit.Assert.*;

/**
 * Created by Nelli on 1/10/2016.
 */
public class ExtractionServiceTest extends BaseTestResolved {

    @Test
    public void getSiftKeypoints() throws Exception {
        Mat src = fileService.LoadAsMatrix(TEST_DATA + "04/prac04ex01img01.png");
        Assert.assertNotNull(src);
        Mat gray = filterService.ToGray(src);
        MatOfKeyPoint mapKeyPoints = extractionService.GetSiftKeypoints(gray);
        Mat result = imageService.Draw(src,mapKeyPoints);

        fileService.Save(OUTPUT_PATH + "siftExtraction.png", result);

    }

}