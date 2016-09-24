package services;

import common.BaseTest;
import org.junit.Before;
import org.junit.Test;
import org.opencv.core.Core;
import org.opencv.core.Mat;


import java.util.List;

import static org.junit.Assert.assertEquals;

public class ColorServiceTest extends BaseTest {

    private final String TEST_DATA_BLUE_PLATE = "testData/plate_blue.png";
    private ColorService colorService;
    private FileService fileService;
    private FilterService filterService;
    private Mat plateImage;

    @Before
    public void setup() throws Exception {
        colorService = new ColorService();
        fileService = new FileService();
        filterService = new FilterService();
        plateImage = fileService.LoadAsMatrix(TEST_DATA_PLATE);
    }

    @Test
    public void GetHist(){
        Mat hist = colorService.GetHsHistogram(plateImage, 120,20);
        assertEquals(1, hist.get(0,0).length);
        Core.MinMaxLocResult mmr = Core.minMaxLoc(hist);
        LOGGER.info("Max val: " + mmr.maxVal);
        LOGGER.info("Min val: " + mmr.minVal);
        LOGGER.info("Max loc: " + mmr.maxLoc);
        LOGGER.info("Min loc: " + mmr.minLoc);

    }

    @Test
    public void DrawHist(){
        Mat hist = colorService.GetHsHistogram(plateImage, 360,100);
        Mat draw = colorService.DrawHist(hist, 360,100,5);
        fileService.Save("outputData/plateHist.png", draw);
        Core.MinMaxLocResult mmr = Core.minMaxLoc(hist);
        LOGGER.info("Max val: " + mmr.maxVal);

        LOGGER.info("Max loc: " + mmr.maxLoc);
        LOGGER.info("Min loc: " + mmr.minLoc);
    }

    @Test
    public void split() throws Exception {
        List<Mat> channels = colorService.Split(plateImage);
        for(int i =0; i<channels.size();i++){
            fileService.Save(OUTPUT_PATH + "channel" + i + ".png", channels.get(i));
        }

    }

    @Test
    public void Dominant(){
        colorService.GetDominantColor(plateImage);
    }

    @Test
    public void GetMask(){
       //double[] color = new double[]{240, 171,63};
        double [] color= colorService.GetDominantColor(plateImage);
        Mat plate = filterService.ReduceNoise(plateImage, 5);
        double[] dominantColor = colorService.GetDominantColor(plateImage);
        Mat result = colorService.GetMask(plate,color);
        fileService.Save(OUTPUT_PATH + "mask1.png", result);
    }
}