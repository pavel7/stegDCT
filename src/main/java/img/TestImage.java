package img;

import string.StringUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TestImage {
    private static Map<String, File> imageMap = new HashMap<String, File>();


    public static void main(String[] args) throws IOException {
        File folder = new File("video" + File.separator + "test_decoded2" + File.separator);
        File[] listOfFiles = folder.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return !name.equals(".DS_Store");
            }
        });

        int indexVal = 0;
        if ((listOfFiles != null) && (listOfFiles.length != 0)) {
            for (File file : listOfFiles) {
                if (file.isFile()) {
                    indexVal++;
                    int indexOfFile = StringUtils.getImageIndex(file.getName());
                    String convertedNameOfFileNumber = String.valueOf(indexOfFile);
                    imageMap.put(convertedNameOfFileNumber, file);
                }
            }
        } else {
            System.out.println("Empty folder");
            return;
        }
        int numberOfColumn = 512;
        int numberOfRow = 512;
        for (int fileIn = 0; fileIn < indexVal; fileIn++) {
            ImageBMP emptyContainer = new ImageBMP(getImage (fileIn));

                  System.out.println("Red:" + emptyContainer.getRGB(250,250).getRed() + ", Green:"+ emptyContainer.getRGB(250,250).getGreen()
                          + ", Blue:" + emptyContainer.getRGB(250,250).getBlue() + ", Alpha:" + emptyContainer.getRGB(250,250).getAlpha());
//            BufferedImage encImage = new BufferedImage(numberOfColumn, numberOfRow, BufferedImage.TYPE_3BYTE_BGR);
//            for (int x = 0; x < numberOfColumn; x++) {
//                for (int y = 0; y < numberOfRow; y++) {
//                    if (fileIn <= 255) {
//                            Color pixel = new Color(fileIn, fileIn, fileIn, fileIn);
//                            encImage.setRGB(x, y, pixel.getRGB());
//                    }
//                    else
//                    {
//                        Color pixel = new Color(0, 0, 0, 0);
//                        encImage.setRGB(x, y, pixel.getRGB());
//                    }
//                }
//            }
//                File file = new File("video" + File.separator + "test_decoded1" + File.separator + getImageName(fileIn));
//                ImageIO.write(encImage, "png", file);
            }

        }


    private static BufferedImage getImage(int index) {

        try {
            String fileIndex = String.valueOf(index);
            //System.out.println("fileName :" + fileName);
            File img = imageMap.get(fileIndex);

            BufferedImage in = null;
            if (img != null) {
                //System.out.println("img :"+img.getName());
                in = ImageIO.read(img);
            } else {
                System.out.println("++++++++++++++++++++++++++++++++++++++index :" + index);
                img = imageMap.get(1);
                in = ImageIO.read(img);
            }
            return in;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    private static String getImageName(int index) {

        try {
            String fileIndex = String.valueOf(index);
            //System.out.println("fileName :" + fileName);
            File img = imageMap.get(fileIndex);

            if (img != null) {
                //System.out.println("img :"+img.getName());
                return img.getName();
            } else {
                return Integer.toString(index);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
}
