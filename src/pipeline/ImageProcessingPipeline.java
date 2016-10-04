package pipeline;

import common.ImageAction;
import common.OpencvUser;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import services.ColorService;
import services.FilterService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class ImageProcessingPipeline extends OpencvUser {

    private FilterService filterService;
    private ColorService colorService;

    public ImageProcessingPipeline(FilterService filterService, ColorService colorService){

        this.filterService = filterService;
        this.colorService = colorService;
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

    public Mat ProcessForBinaryAnalysis(Mat src){
        List<ImageAction> list = new ArrayList<>();
        list.add(s -> filterService.ToGray(s));
        list.add(s -> filterService.ReduceNoise(s,1));
        list.add(s -> colorService.EqulizeHistogram(s));
        list.add(s -> filterService.ThresholdInv(s));
        list.add(s -> filterService.Erode(s,3,6));

        return Run(src, list);
    }

    public Mat GetMask(Mat src){
        double[] dc = colorService.GetDominantColor(src);

        List<ImageAction> actions = new ArrayList<>();
        actions.add(s -> filterService.ReduceNoise(s,5));
        actions.add(s -> colorService.GetMask(s,dc));
        actions.add(s -> filterService.Close(s,10,1));
        return Run(src, actions);
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
