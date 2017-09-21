package img;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ByteImageBMP {
    private BufferedImage img = null;

    public ByteImageBMP(String imgPath) {
        try {
            img = ImageIO.read(new File(imgPath));
        } catch (IOException e) {
            System.out.println("Image not founded" + new File(".").getAbsolutePath() + imgPath);
        }
    }

    public ByteImageBMP(BufferedImage imgContainer) {
        img = imgContainer;
    }

    private Color getRGB(int x, int y) {
        return new Color(img.getRGB(x, y));
    }

    public short getColor(int x, int y) {
        //Color pixel = new Color(img.getRGB(x, y));
        int rgb = img.getRGB(x, y);
        int r = (rgb >> 16) & 0xFF;
        int g = (rgb >> 8) & 0xFF;
        int b = (rgb & 0xFF);
        //int temp = pixel.getBlue() + pixel.getGreen() + pixel.getRed();
        short result = (short) ((r + g + b) / 3);
        return result;
    }

    public int getRValue(int x, int y) {
        Color pixel = new Color(img.getRGB(x, y));
        //int rgb = img.getRGB(x, y);
        return pixel.getRed();
    }

    public int getNumberOfColumn() {
        return img.getWidth();
    }

    public int getNumberOfRow() {
        return img.getHeight();
    }

    public short[] getByteCodeOfImage() {
        short[] result = new short[this.getNumberOfColumn() * this.getNumberOfRow()];
        int k = 0;
        for (int i = 0; i < this.getNumberOfColumn(); i++) {
            for (int j = 0; j < this.getNumberOfRow(); j++) {
                result[k] = this.getColor(i, j);
                k++;
            }
        }
        return result;
    }

    public void saveImageFromByteCode(String path, short[] image) {
        int numberOfRow = this.getNumberOfRow();
        int numberOfColumn = this.getNumberOfColumn();
        BufferedImage encImage = new BufferedImage(numberOfColumn, numberOfRow, BufferedImage.TYPE_3BYTE_BGR);
        int k = 0;
        for (int x = 0; x < numberOfColumn; x++) {
            for (int y = 0; y < numberOfRow; y++) {
                Color pixel = new Color(image[k], image[k], image[k]);
                encImage.setRGB(x, y, pixel.getRGB());
                k++;
            }
        }
        try {
            ImageIO.write(encImage, "bmp", new File(path + ".bmp"));
        } catch (IOException e) {
            System.out.println("error " + e.getMessage());
        }

    }

    public static void saveImageFromMassive(String path, double[][] image, int numberOfColumn, int numberOfRow) {
        BufferedImage encImage = new BufferedImage(numberOfColumn, numberOfRow, BufferedImage.TYPE_3BYTE_BGR);
        for (int x = 0; x < numberOfColumn; x++) {
            for (int y = 0; y < numberOfRow; y++) {
                try {
                    Color pixel = new Color((short) image[x][y], (short) image[x][y], (short) image[x][y]);
                    encImage.setRGB(x, y, pixel.getRGB());}
                catch (IllegalArgumentException e)
                {
                    System.out.println("Error: " + image[x][y]);
                }
            }
        }
        try {
            ImageIO.write(encImage, "bmp", new File(path));
        } catch (IOException e) {
            System.out.println("error " + e.getMessage());
        }

    }

    public int divideImageOnPart(int width, int height, String outputPath) {
        int numberOfXSegments = this.getNumberOfColumn() / width;
        int numberOfYSegments = this.getNumberOfRow() / height;
        int numberOfSegments = numberOfXSegments * numberOfYSegments;
        int segmentNumber = 0;
        for (int i = 0; i < numberOfXSegments; i++) {
            for (int j = 0; j < numberOfYSegments; j++) {
                BufferedImage encImage = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
                for (int x = 0; x < width; x++)
                    for (int y = 0; y < height; y++) {
                        Color pixel = new Color(this.getColor(i * width + x, j * height + y), this.getColor(i * width + x, j * height + y), this.getColor(i * width + x, j * height + y));
                        encImage.setRGB(x, y, pixel.getRGB());
                    }
                try {
                    ImageIO.write(encImage, "bmp", new File(outputPath + segmentNumber + ".bmp"));
                } catch (IOException e) {
                    System.out.println("error " + e.getMessage());
                }
                segmentNumber++;
            }
        }
        return numberOfSegments;
    }

    public void combineImageFromParts (int width, int height, String inputPath, String outputPath) {
        int numberOfXSegments = this.getNumberOfColumn() / width;
        int numberOfYSegments = this.getNumberOfRow() / height;
        int numberOfSegments = numberOfXSegments * numberOfYSegments;
        int segmentNumber = 0;
        BufferedImage encImage = new BufferedImage(this.getNumberOfColumn(), this.getNumberOfRow(), BufferedImage.TYPE_3BYTE_BGR);
        for (int i = 0; i < numberOfXSegments; i++) {
            for (int j = 0; j < numberOfYSegments; j++) {
                ByteImageBMP segmentImage = new ByteImageBMP(inputPath + segmentNumber + ".bmp");
                for (int x = 0; x < width; x++)
                    for (int y = 0; y < height; y++) {
                        Color pixel = new Color(segmentImage.getColor(x, y), segmentImage.getColor(x, y), segmentImage.getColor(x, y));
                        encImage.setRGB(i * width + x, j * height + y, pixel.getRGB());
                    }
                segmentNumber++;
            }
        }
        try {
            ImageIO.write(encImage, "bmp", new File(outputPath + "resultedIMG" + ".bmp"));
        } catch (IOException e) {
            System.out.println("error " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        ByteImageBMP newC = new ByteImageBMP("images" + File.separator + "hologram.bmp");
        System.out.println(newC.divideImageOnPart(32, 16, "images"+ File.separator +"dividedIM"+ File.separator));
        //newC.combineImageFromParts(32,16,"images" + File.separator + "test" + File.separator + "90-","images" + File.separator + "090");
//        for (int i = 0; i < newC.getNumberOfRow(); i++) {
//            for (int j = 0; j < newC.getNumberOfColumn(); j++) {
//                System.out.print(newC.getColor(i, j) + " ");
//            }
//            System.out.println();
//        }

//        BufferedImage encImage = new BufferedImage(newC.getNumberOfRow(), newC.getNumberOfColumn(), BufferedImage.TYPE_BYTE_GRAY);
//        for (int x = 0; x < newC.getNumberOfRow(); x++) {
//            for (int y = 0; y < newC.getNumberOfColumn(); y++) {
//                    short tempPixelValue = newC.getColor(x, y);
//                    System.out.print(tempPixelValue + " ");
//                    Color pixel = new Color(tempPixelValue, tempPixelValue, tempPixelValue);
//                    encImage.setRGB(x, y, pixel.getRGB());
//                }
//            System.out.println();
//            }
//        try {
//            ImageIO.write(encImage, "png", new File("images" + File.separator + "test" + ".png"));
//            //ImageIO.write(encImage, "png", new File("images" + File.separator + "test.png"));
//        } catch (IOException e) {
//            System.out.println("error " + e.getMessage());
//        }

    }
}
