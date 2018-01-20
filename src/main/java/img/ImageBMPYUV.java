package img;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class ImageBMPYUV {
    private BufferedImage img = null;

    public ImageBMPYUV(String imgPath) {
        try {
            img = ImageIO.read(new File(imgPath));
        } catch (IOException e) {
            System.out.println("Image not founded" + new File(".").getAbsolutePath() + imgPath);
        }
    }

    public ImageBMPYUV(BufferedImage imgContainer) {
        img = imgContainer;
    }


    public int getNumberOfColumn() {
        return img.getWidth();
    }

    public int getNumberOfRow() {
        return img.getHeight();
    }

    public static double difference(ArrayList<double[][]> one, ArrayList<short[][]> two) {
        double max = Double.MIN_VALUE;
        for (int i = 0; i < one.size(); i++) {
            for (int j = 0; j < 8; j++) {
                for (int k = 0; k < 8; k++) {
                    if (max < Math.abs(one.get(i)[j][k] - two.get(i)[j][k]))
                        max = Math.abs(one.get(i)[j][k] - two.get(i)[j][k]);
                }
            }
        }
        return max;
    }

    public float getY(int x, int y) {
        Color tempCol = new Color(img.getRGB(x, y));
        RGB tempRGB = new RGB((short)tempCol.getRed(),(short)tempCol.getGreen(), (short)tempCol.getBlue());
        YUV tempYUV = new YUV();
        YuvConverter.rgb2YUV(tempRGB, tempYUV);
        return tempYUV.getY();
    }
}