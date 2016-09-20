package services;

import org.junit.Before;
import org.junit.Test;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import static org.junit.Assert.assertEquals;

public class filterServiceTest {

    private final String TEST_DATA_RED = "testData/red.png";
    private final String TEST_DATA_HALF = "testData/half.png";

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

    @Test(expected=IllegalArgumentException.class)
    public void PixelTransform_Pass_Color() throws Exception{
        Mat red = fileService.LoadAsMatrix(TEST_DATA_RED);
        filterService.PixelTransform(red, 1,1);
    }

    @Test
    public void PixelTransform_operation() throws Exception{
        Mat red = fileService.LoadAsMatrix(TEST_DATA_RED);
        Mat grayed =filterService.ToGray(red);
        Mat result = filterService.PixelTransform(grayed, 1.2,6);

        double[] data = result.get(0,0);
        assertEquals(1,data.length);
        assertEquals(71.0,data[0], 0.1);
    }


    @Test
    public void EdgeDetection() throws Exception{
        Mat half = fileService.LoadAsMatrix(TEST_DATA_HALF);
        Mat grayed =filterService.ToGray(half);
        Mat result = filterService.EdgeDetection(grayed);
        fileService.Save("testData/edge.png", result);
    }

}