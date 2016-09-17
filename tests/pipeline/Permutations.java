package pipeline;

public class Permutations {

    public void Params(){
        int[] median = getArray(1,7,2);
        double[] pixel_brightness= getArray(0,2,0.2);
        double[] pixel_contrast= getArray(0,2,0.2);
        int[] threshold_thresh = getArray(1,200,2);
        int[] threshold_max= getArray(1,255,1);
        int[]close_x = getArray(1,50,1);
        int[]close_y = getArray(1,50,1);

    }

    private double[] getArray(double from, double to, double step){
        int size = (int) ((to - from)/step);

        double[] result = new double[size];

        for(int i = 0; i<size; i++){
            result[0] = to + (step * i);
        }

        return result;
    }

    private int[] getArray(int from, int to, int step){
        int size = (to - from)/step;

        int[] result = new int[size];

        for(int i = 0; i<size; i++){
            result[0] = to + (step * i);
        }

        return result;
    }
}
