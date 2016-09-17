package services;

import org.opencv.core.Mat;
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

    public Mat LoadAsMatrix(String path) throws Exception{
        if (!IsFileExists(path)) throw new FileNotFoundException();

        Mat newImage = Imgcodecs.imread(path);
        if(newImage.dataAddr()==0){
            throw new Exception ("Couldn't open file "+path);
        }

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

    public  static File[] GetFileList(String dirPath) {
        File dir = new File(dirPath);

        File[] fileList = dir.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.endsWith(".png");
            }
        });
        return fileList;
    }


}
