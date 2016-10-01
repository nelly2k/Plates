package common;


import org.junit.Before;
import services.*;

public class BaseTestResolved extends BaseTest {
    protected FileService fileService;
    protected FilterService filterService;
    protected ExtractionService extractionService;
    protected ImageService imageService;


    @Before
    public void setup(){
        fileService = Container.Resolve(FileService.class);
        filterService = Container.Resolve(FilterService.class);
        extractionService =Container.Resolve(ExtractionService.class);
        imageService =Container.Resolve(ImageService.class);
    }
}
