package services;

import org.junit.Before;
import org.junit.Test;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Filter;

import static org.junit.Assert.*;

/**
 * Created by Nelli on 17/09/2016.
 */
public class DetectionServiceTest {
    private final String TEST_DATA_RECTANGLES = "testData/rects.png";

    FileService fileService;
    DetectionService detectionService;
    FilterService filterService;

    @Before
    public void runBeforeEveryTest(){

        fileService = new FileService();
        detectionService = new DetectionService();
        filterService = new FilterService();
    }


    @Test
    public void detectRectangles() throws Exception {

        List<MatOfPoint> contours = getMatOfPoints();
        assertEquals(2, contours.size());
    }

    @Test
    public void filterConours_no_filters() throws Exception {
        List<MatOfPoint> contours = getMatOfPoints();
        List<BoundingFilter> filters = new ArrayList<>();
        List<MatOfPoint> filteredCounours = detectionService.Filter(contours, filters);
        assertEquals(2, filteredCounours.size());
    }

    @Test
    public void filterConours_width() throws Exception {
        List<MatOfPoint> contours = getMatOfPoints();
        List<BoundingFilter> filters = new ArrayList<>();
        filters.add(e-> e.width < 14 && e.width > 10);

        List<MatOfPoint> filteredCounours = detectionService.Filter(contours, filters);
        assertEquals(1, filteredCounours.size());
    }


    private List<MatOfPoint> getMatOfPoints() throws Exception {
        Mat rects = fileService.LoadAsMatrix(TEST_DATA_RECTANGLES);
        rects = filterService.ToGray(rects);
        return detectionService.DetectRectangles(rects);
    }

}