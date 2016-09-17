package pipeline;

import org.junit.Before;
import org.junit.Test;
import org.opencv.core.Mat;
import services.FileService;
import services.FilterService;

public class IntegrationTests {

    private final String TEST_DATA_RED = "testData/red.png";
    private final String TEST_OUTPUT = "outputData/";


    private FilterService filterService;
    private ImageProcessingPipeline imageProcessingPipeline;
    private FileService fileService;

    @Before
    public void setUp() throws Exception {
        fileService = new FileService();
        filterService = new FilterService();

        imageProcessingPipeline = new ImageProcessingPipeline(filterService);
    }


    @Test
    public void pricessSimple_red() throws Exception {
        Mat red= fileService.LoadAsMatrix(TEST_DATA_RED);
        Mat result = imageProcessingPipeline.PricessSimple(red);
        fileService.Save(TEST_OUTPUT + "red_simple.png", result);
    }

}
