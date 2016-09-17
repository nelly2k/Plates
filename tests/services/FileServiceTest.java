package services;

import org.junit.Before;
import org.junit.Test;
import org.opencv.core.Mat;

import java.awt.image.BufferedImage;

import static org.junit.Assert.*;

public class FileServiceTest {
    public static final String TEST_DATA_SQUARE_JPG = "testData/square.jpg";
    public static final String TEST_DATA_TEXT = "testData/someTest.txt";
    public static final String OUTPUT_DATA = "outputData/fileServiceTestResult.jpg";

    FileService fileService;

    @Before
    public void runBeforeEveryTest(){
        fileService = new FileService();
    }

    @Test
    public void image_loaded() throws Exception {

        BufferedImage img = fileService.Load(TEST_DATA_SQUARE_JPG);
        assertNotNull(img);
    }

    @Test
    public void text_file_loaded() throws Exception{
        String txt = fileService.LoadText(TEST_DATA_TEXT).trim();
        assertEquals("some text here", txt);
    }

    @Test
    public void image_loaded_as_mat() throws Exception{
        Mat file = fileService.LoadAsMatrix(TEST_DATA_SQUARE_JPG);
        assertNotNull(file);
    }

    @Test
    public void save_img() throws Exception{
        Mat file = fileService.LoadAsMatrix(TEST_DATA_SQUARE_JPG);
        fileService.Save(OUTPUT_DATA, file);
    }

}