package services;

import com.sun.org.apache.xpath.internal.operations.Bool;
import common.OpencvUser;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

import static org.opencv.core.CvType.CV_8UC3;

public class ColorService extends OpencvUser {

    private Mat ToHSV(Mat source){
        Mat result = NewMat(source);
        Imgproc.cvtColor(source, result, Imgproc.COLOR_RGB2HSV);
        return result;
    }

    public Mat GetHsHistogram(Mat source){
        return GetHsHistogram(source, 0,0);
    }

    public Mat GetHsHistogram(Mat rgbSource, int hBins, int sBins){
        List<Mat> histImages=new ArrayList<Mat>();
        histImages.add(ToHSV(rgbSource));

        /// Using 50 bins for hue and 60 for saturation
        hBins = hBins==0? 30:hBins;
        sBins = sBins ==0? 32:sBins;
        MatOfInt histSize = new MatOfInt( hBins,  sBins);

        // we compute the histogram from the 0-th and 1-st channels
        MatOfInt channels = new MatOfInt(0, 1);

        Mat histRef = new Mat();
        Mat histSource = new Mat();

        // hue varies from 0 to 179, saturation from 0 to 255
        MatOfFloat ranges =  new MatOfFloat( 0f,180f,0f,256f );

        Imgproc.calcHist(histImages,
                channels,
                new Mat(),
                histRef,
                histSize,
                ranges,
                false);

        return histRef;
    }

    public Mat DrawHist(Mat histRef){
        return DrawHist(histRef, 0,0,0);
    }

    public Mat DrawHist(Mat histRef, int hBins, int sBins, int scale){
        Core.MinMaxLocResult mmr = Core.minMaxLoc(histRef);
        scale  = scale == 0?5:scale;
        hBins = hBins== 0? 30:hBins;
        sBins = sBins ==0? 32:sBins;

        Mat histImg  = new Mat(sBins * scale, hBins * scale, CV_8UC3);
        Imgproc.rectangle( histImg, new Point(1, 1),
                new Point( histImg.width()-1,histImg.height()-1),
                new Scalar(204,74,95),4 );

        for( int h = 0; h < hBins; h++ ){
            for( int s = 0; s < sBins; s++ )
            {
                double binVal = histRef.get(h,s)[0];
                int intensity = (int) (binVal*255/mmr.maxVal);
                Imgproc.rectangle( histImg, new Point(h*scale, s*scale),
                        new Point( (h+1)*scale - 1, (s+1)*scale - 1),
                        new Scalar(intensity),5 );
            }
        }
        return histImg;
    }

    public Mat GenerateMask(Mat source){
        Mat result = new Mat(source.height()+2, source.width()+2, CvType.CV_8UC1);
        int connectivity=4;
        int newmaskval=255;
        Point point = new Point(8,23);
         Imgproc.floodFill(source, result, point,new Scalar(newmaskval));
        return result;
    }

    private Boolean IsEqual(double[] colorA, double[] colorB, double variation ){
        for(int i = 0; i< colorA.length; i++){
            if ( Math.abs(colorA[i] - colorB[i]) > variation)
            {
                return false;
            }
        }
        return true;
    }

    public Mat GetMask(Mat source, double[] rgbColor){
        Mat result = NewMat(source);

        int width = source.width();
        int height = source.height();

        double[] test = result.get(10,10);

        for(int r =0; r<height; r++){
            for(int c = 0; c<width; c++){
                if (IsEqual(rgbColor, source.get(r,c),15)){

                    result.put(r,c,new double[]{255, 255,255});
                }

            }
        }
        return result;
    }

    public double[] GetDominantColor(Mat source){
        List<Mat> channels = Split(source);

        double[] result = new double[3];
        for(int i = 0; i< channels.size();i++){
            Mat hist = GetOneChannel(channels.get(i));
            Core.MinMaxLocResult mmr = Core.minMaxLoc(hist);
            result[i] = mmr.maxLoc.y * 8;
            //LOGGER.info("Chanel: " + i  + "Max: " + mmr.maxVal + " / " + mmr.maxLoc);
        }
        return result;
    }

    public Mat GetOneChannel(Mat source){
        List<Mat> histImages=new ArrayList<Mat>();
        histImages.add(source);

        MatOfInt histSize = new MatOfInt(32);
        MatOfInt channels = new MatOfInt(0);

        Mat histRef = new Mat();
        Mat histSource = new Mat();

         MatOfFloat ranges =  new MatOfFloat( 0f,256f);

        Imgproc.calcHist(histImages,
                channels,
                new Mat(),
                histRef,
                histSize,
                ranges,
                false);

        return histRef;
    }

    public List<Mat> Split(Mat source){
        List<Mat> planes = new ArrayList<Mat>(3);
        Core.split(source, planes);
        return planes;
    }

}
