package pipeline;

import org.junit.Before;
import org.junit.Test;
import org.opencv.core.Mat;
import services.FilterService;
import static org.mockito.Mockito.*;

public class ImageProcessingPipelineTest {

    private FilterService mockFilterService;
    private ImageProcessingPipeline imageProcessingPipeline;

    @Before
    public void setUp() throws Exception {
        mockFilterService = mock(FilterService.class);
        imageProcessingPipeline = new ImageProcessingPipeline(mockFilterService);
    }

    @Test
    public void pricessSimple_grayed() throws Exception {
        Mat mat = new Mat();
        imageProcessingPipeline.PricessSimple(mat);
        verify(mockFilterService, times(1)).ToGray(anyObject());
    }

    @Test
    public void pricessSimple_do_noise() throws Exception {
        Mat mat = new Mat();
        imageProcessingPipeline.PricessSimple(mat);
        verify(mockFilterService, times(1)).ReduceNoise(anyObject(), anyInt());
    }

    @Test
    public void pricessSimple_contrast() throws Exception {
        Mat mat = new Mat();
        imageProcessingPipeline.PricessSimple(mat);
        verify(mockFilterService, times(1)).PixelTransform(anyObject(), anyInt(),anyInt());
    }

    @Test
    public void pricessSimple_edge() throws Exception {
        Mat mat = new Mat();
        imageProcessingPipeline.PricessSimple(mat);
        verify(mockFilterService, times(1)).EdgeDetection(anyObject());
    }
}