package services;

import org.junit.Before;
import org.junit.Test;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import static org.junit.Assert.assertEquals;

public class filterServiceTest {

    private final String TEST_DATA_RED = "testData/red.png";

    FilterService filterService;
    FileService fileService;

    @Before
    public void runBeforeEveryTest(){

        filterService = new FilterService();
        fileService = new FileService();
    }

    @Test
    public void toGray() throws Exception {
        Mat red = fileService.LoadAsMatrix(TEST_DATA_RED);

        Mat grayed =filterService.ToGray(red);
        double[] data = grayed.get(0,0);

        assertEquals(1,data.length);
        assertEquals(54.0,data[0], 0.1); //new red
        assertEquals(255,grayed.get(5,5)[0], 0.1); //white
        assertEquals(0,grayed.get(11,9)[0], 0.1); //black
    }

    @Test
    public void NoiseReduction() throws Exception{
        Mat red = fileService.LoadAsMatrix(TEST_DATA_RED);
        Mat grayed =filterService.ToGray(red);
        Mat result = filterService.ReduceNoise(grayed,3);

        assertEquals(54,result.get(5,5)[0], 0.1); //white
        assertEquals(54,result.get(11,9)[0], 0.1); //black
    }

}