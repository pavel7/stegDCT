package tests;

import string.StringUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.Map;

public class VideoDifIndicatorTests {
    private static Map<String, File> emptyImageMap = new HashMap<String, File>();
    private static Map<String, File> resultedImageMap = new HashMap<String, File>();


    public static double[] calculateNC (String pathToEmptyContainer, String pathToResultedContainer){
        int numberfOfImagesInEmptyContainer = readAllImages(pathToEmptyContainer, emptyImageMap);
        int numberfOfImagesInResultedContainer = readAllImages(pathToResultedContainer, resultedImageMap);
        if (numberfOfImagesInEmptyContainer != numberfOfImagesInResultedContainer)
        {
            System.out.println("Folders contains different number of images");
            return null;
        }
        int length = numberfOfImagesInEmptyContainer;
        double[] sumMSE = new double[length];
        for (int i = 0; i < length; i++){
            sumMSE[i] = 0;
            BufferedImage imageFromEmptyContainer = getImage(i, emptyImageMap);
            int imageWidth = imageFromEmptyContainer.getWidth();
            int imageHeight = imageFromEmptyContainer.getHeight();
            BufferedImage imageFromResultedContainer = getImage(i, resultedImageMap);
            double total1 = 0;
            double total2 = 0;
            for (int j = 0; j < imageWidth; j++) {
                for (int k = 0; k < imageHeight; k++){
                    sumMSE[i] = sumMSE[i] + imageFromEmptyContainer.getRGB(j,k)*imageFromEmptyContainer.getRGB(j,k);
                    total1 = total1 + imageFromEmptyContainer.getRGB(j,k)*imageFromEmptyContainer.getRGB(j,k);
                    total2 = total2 + imageFromResultedContainer.getRGB(j,k)*imageFromResultedContainer.getRGB(j,k);
                }
            }
            sumMSE[i] = sumMSE[i]/(Math.sqrt(total1)*Math.sqrt(total2));
            System.out.println("Completed #"+i);

        }
        return sumMSE;
    }

    private static int readAllImages(String pathToFolder, Map<String, File> imageMap){
        File folder = new File(pathToFolder);
        File[] listOfFiles = folder.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return !name.equals(".DS_Store");
            }
        });
        int numberOfImages = 0;
        if ((listOfFiles != null) && (listOfFiles.length != 0)) {
            for (File file : listOfFiles) {
                if (file.isFile()) {
                    int indexOfFile = StringUtils.getImageIndex(file.getName());
                    String convertedNameOfFileNumber = String.valueOf(indexOfFile);
                    imageMap.put(convertedNameOfFileNumber, file);
                    numberOfImages++;
                }
            }
        } else {
            System.out.println("Empty folder");
            return 0;
        }
        return numberOfImages;
    }

    private static BufferedImage getImage(int index, Map<String, File> imageMap) {

        try {
            String fileIndex = String.valueOf(index);
            File img = imageMap.get(fileIndex);

            BufferedImage in = null;
            if (img != null) {
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

    public static void main(String[] args) {
        String emptyDirectory = "video" + File.separator + "test_decoded" + File.separator;
        String resultedDirectory = "video" + File.separator + "test_decoded2" + File.separator;
        double[] testNC = calculateNC(emptyDirectory, resultedDirectory);
        for (int i = 0; i < testNC.length; i++)
        {
            System.out.println(testNC[i]);
        }
    }
}
