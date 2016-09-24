package services;

import common.OpencvUser;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.imgcodecs.Imgcodecs;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class FileService extends OpencvUser {

    public BufferedImage Load(String path) throws FileNotFoundException {
        if (!IsFileExists(path)) throw new FileNotFoundException();

        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(path));
        } catch (IOException ignored) {

        }
        return img;
    }

    public String LoadText(String path) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(path));
        StringBuilder sb = new StringBuilder();
        String line = br.readLine();

        while (line != null) {
            sb.append(line);
            sb.append(System.lineSeparator());
            line = br.readLine();
        }
        return sb.toString();

    }
    public int[] LoadCoordinates(String path) throws IOException{
        String txtPath = path.substring(0, path.length() - 4) + ".txt";
        String[]params =  LoadText(txtPath).split(" ");
        int[] result = new int[4];
        if (params.length <= 1){
            return result;
        }

        for(int i = 0; i<4;i++){
            result[i] = Integer.parseInt(params[i].trim());
        }
        return result;
    }


    public Rect LoadRect(String path) throws IOException{
        String txtPath = path.substring(0, path.length() - 4) + ".txt";
        String[]params =  LoadText(txtPath).split(" ");
        int x1 = Integer.parseInt(params[0].trim());
        int y1 = Integer.parseInt(params[1].trim());
        int x2 = Integer.parseInt(params[2].trim());
        int y2 = Integer.parseInt(params[3].trim());

        return new Rect(x1,y1, x2-x1, y2-y1);
    }

    public Mat LoadAsMatrix(String path) throws Exception{
        if (!IsFileExists(path)) throw new FileNotFoundException();

        Mat newImage = Imgcodecs.imread(path);
        if(newImage.dataAddr()==0){
            throw new Exception ("Couldn't open file "+path);
        }
      //  LOGGER.info("Loading file " + path);

        return newImage;
    }

    private boolean IsFileExists(String path) {

        File f = new File(path);
        return f.exists() && !f.isDirectory();
    }

    public void Save(String path,Mat mat){
        Imgcodecs.imwrite(path, mat);
    }

    public void Save(String path, BufferedImage  img ) throws IOException {
        File outputfile = new File(path);
        ImageIO.write(img, "png", outputfile);
    }

    public  File[] GetFileList(String dirPath, String extention) {
        File dir = new File(dirPath);
        return dir.listFiles((dir1, name) ->extention=="" || name.endsWith(extention));
    }

    public  File[] GetFileList(String dirPath) {
        return GetFileList(dirPath, "");
    }

    public void ClearFolder(String path){
        File[] files = GetFileList(path);
        if (files == null)
            return;
        for(File file: files){
            file.delete();
        }
    }


}
