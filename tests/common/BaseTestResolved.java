package common;


import org.junit.Before;
import pipeline.FeaturesDetectionPipeline;
import pipeline.ImageProcessingPipeline;
import services.*;

public class BaseTestResolved extends BaseTest {
    protected FileService fileService;
    protected FilterService filterService;
    protected ExtractionService extractionService;
    protected ImageService imageService;
    protected DetectionService detectionService;
    protected ColorService colorService;
    protected FeaturesDetectionPipeline featuresDetectionPipeline;
    protected ImageProcessingPipeline imageProcessingPipeline;

    @Before
    public void setup(){
        fileService = Container.Resolve(FileService.class);
        filterService = Container.Resolve(FilterService.class);
        extractionService =Container.Resolve(ExtractionService.class);
        imageService =Container.Resolve(ImageService.class);
        detectionService = Container.Resolve(DetectionService.class);
        colorService = Container.Resolve(ColorService.class);
        featuresDetectionPipeline = new FeaturesDetectionPipeline(detectionService, colorService,filterService, imageService);
        imageProcessingPipeline = new ImageProcessingPipeline(filterService, colorService);
    }
}
