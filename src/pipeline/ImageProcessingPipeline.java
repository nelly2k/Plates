package pipeline;

import common.ImageAction;
import common.OpencvUser;
import org.opencv.core.Mat;
import services.FilterService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class ImageProcessingPipeline extends OpencvUser {

    private FilterService filterService;

    public ImageProcessingPipeline(FilterService filterService){

        this.filterService = filterService;
    }

    public List<Mat> ProcessSimple(Mat source){
        return RunReturnAll(source, GetSimple());
    }

    public  Mat PricessSimple(Mat source){
        return Run(source, GetSimple());
    }

    private List<ImageAction> GetSimple(){
        List<ImageAction> list = new ArrayList<>();
        list.add(s -> filterService.ToGray(s));
        list.add(s -> filterService.ReduceNoise(s,3));
        list.add(s -> filterService.PixelTransform(s,0.80,0));
        list.add(s -> filterService.EdgeDetection(s));
        list.add(s -> filterService.Threshold(s));
        list.add(s -> filterService.Close(s));
        return list;
    }

    private List<Mat> RunReturnAll(Mat source, List<ImageAction> actions){
        List<Mat> result = new ArrayList<>();
        Mat img =  source.clone();
        result.add(img.clone());
        for (ImageAction action: actions) {

            img = action.Do(img);
            result.add(img.clone());
        }

        return result;
    }

    private Mat Run(Mat source, List<ImageAction> actions){
        Mat result =  source.clone();

        for (ImageAction action: actions) {
            result = action.Do(result);
        }

        return result;
    }

}
