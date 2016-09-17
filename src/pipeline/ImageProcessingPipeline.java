package pipeline;

import common.ImageAction;
import common.OpencvUser;
import org.opencv.core.Mat;
import services.FilterService;

import java.util.ArrayList;
import java.util.List;


public class ImageProcessingPipeline extends OpencvUser {

    private FilterService filterService;

    public ImageProcessingPipeline(FilterService filterService){

        this.filterService = filterService;
    }

    public  Mat PricessSimple(Mat source){
        List<ImageAction> list = new ArrayList<>();
        list.add(s -> filterService.ToGray(s));
        list.add(s -> filterService.ReduceNoise(s,3));
        list.add(s -> filterService.PixelTransform(s,0.80,0));
        list.add(s -> filterService.EdgeDetection(s));
        list.add(s -> filterService.Threshold(s));
        list.add(s -> filterService.Close(s));
        return Run(source, list);
    }

    private Mat Run(Mat source, List<ImageAction> actions){
        Mat result =  source.clone();

        for (ImageAction action: actions) {
            result = action.Do(result);
        }

        return result;
    }

}
