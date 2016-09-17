package services;

import org.junit.Before;
import org.junit.Test;
import org.opencv.core.Mat;
import org.opencv.core.Rect;

import static org.junit.Assert.*;

public class ImageServiceTest {
    private final String TEST_DATA_TRAIN_1 = "testData/train/plate_type_a_1ABC000.png";
    private final String TEST_OUTPUT = "outputData/";

    ImageService imageService;
    FileService fileService;

    @Before
    public void setup(){
        imageService = new ImageService();
        fileService = new FileService();
    }

    @Test
    public void crop() throws Exception {
        Mat img = fileService.LoadAsMatrix(TEST_DATA_TRAIN_1);
        Mat result = imageService.Crop(img, new Rect(10,10,100,100));
        fileService.Save(TEST_OUTPUT + "cropped.png", result);
    }

}