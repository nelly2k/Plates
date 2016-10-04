package pipeline;

import org.opencv.core.Mat;
import org.opencv.core.Rect;

public class MaskExtractionResult{
    Mat image;
    Rect offset;
    public MaskExtractionResult (Mat image, Rect offset){
        this.image= image;
        this.offset = offset;
    }

    public Mat Image() {
        return image;
    }

    public Rect Offset(){
        return offset;
    }
}
