package img;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class ImageBMP {
    private BufferedImage img = null;

    public ImageBMP(String imgPath) {
        try {
            img = ImageIO.read(new File(imgPath));
        } catch (IOException e) {
            System.out.println("Image not founded" + new File(".").getAbsolutePath() + imgPath);
        }
    }

    public ImageBMP(BufferedImage imgContainer) {
        img = imgContainer;
    }

    public Color getRGB(int x, int y) {
        return new Color(img.getRGB(x, y));
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

    public static void main(String[] args) {
        String pathToImage = "images" + File.separator + "newpics" + File.separator + "2c";
        ImageBMP firstImage = new ImageBMP(pathToImage+".bmp");
        int numberOfColumn = firstImage.getNumberOfColumn();
        int numberOfRow = firstImage.getNumberOfRow();
        BufferedImage encImage = new BufferedImage(numberOfColumn, numberOfRow, BufferedImage.TYPE_3BYTE_BGR);

        for (int x = 0; x < numberOfColumn; x++) {
            for (int y = 0; y < numberOfRow; y++) {
                int bluePixelValue = 255;
                Color pixel = new Color(bluePixelValue - firstImage.getRGB(x,y).getRed(),
                        bluePixelValue - firstImage.getRGB(x,y).getGreen(),
                        bluePixelValue - firstImage.getRGB(x,y).getBlue(),
                        bluePixelValue - firstImage.getRGB(x,y).getAlpha());
                encImage.setRGB(x, y, pixel.getRGB());
            }
        }


        try {
            ImageIO.write(encImage, "bmp", new File(pathToImage + "1.bmp"));
            //ImageIO.write(encImage, "png", new File("images\\test.png"));
        } catch (IOException e) {
            System.out.println("error " + e.getMessage());
        }
        //        ImageBMP test = new ImageBMP("images\\Lenna.bmp");
//        ArrayList<short[][]> testBluePixel = DCTMethodImage.imageToListOfBlueSegments(test);
//        ArrayList<double[][]> listOfSpectrCoefOfDCT = DCTMethodImage.calculateSpectrCoefOfDCT(testBluePixel);
//        //DCTMethodImage.insertMassage(listOfSpectrCoefOfDCT, "Green 2015");
//        DCTMethodImage.insertMassage(listOfSpectrCoefOfDCT, "Notepad++ v6.8.3 bug-fixes:\n" +
//                "1.  Fix a crash issue by using wild card (*) to open files on command line.\n" +
//                "2.  Fix the problem of display refresh missing on exit.\n" +
//                "3.  Fix plugin shortcut configuration lost problem by using option -noPlugin.\n" +
//                "4.  Fix Norwegian localization bad display and wrong encoding.\n" +
//                "5.  Fix functionList display problem under high DPI.\n" +
//                "6.  Fix Norwegian localization bad display and wrong encoding.\n" +
//                "Included plugins:\n" +
//                "1.  NppFTP 0.26.3\n" +
//                "2.  NppExport v0.2.8\n" +
//                "3.  Plugin Manager 1.3.5\n" +
//                "Notepad++ v6");
////        System.out.println(DCTMethodImage.decodeMessage(listOfSpectrCoefOfDCT));
//        ArrayList<double[][]> listOfInvertSpectrCoefOfDCT = DCTMethodImage.invertDCT(listOfSpectrCoefOfDCT);
//        double dif = ImageBMP.difference(listOfInvertSpectrCoefOfDCT, testBluePixel);
//        double[][] listOfBluePixels = DCTMethodImage.invertDCTToBluePixelsMassive(listOfInvertSpectrCoefOfDCT, test);
//        DCTMethodImage.encodeImage(test, listOfBluePixels);
//        ImageBMP testDecode = new ImageBMP("images" + File.separator + "test.png");
//        ArrayList<short[][]> testDecodeBluePixel = DCTMethodImage.imageToListOfBlueSegments(testDecode);
//        ArrayList<double[][]> listOfDecodeSpectrCoefOfDCT = DCTMethodImage.calculateSpectrCoefOfDCT(testDecodeBluePixel);
//        System.out.println(DCTMethodImage.decodeMessage(listOfDecodeSpectrCoefOfDCT));
    }

}