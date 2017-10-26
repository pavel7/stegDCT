import img.ByteImageBMP;
import mathOperations.BitOperations;
import steganographicMethods.DCTMethodVideo;

import java.io.File;
import java.util.concurrent.ExecutionException;

public class TestVideoDecode {


    public static void main(String[] args) throws ExecutionException, InterruptedException {
        String filenameInput = "video" + File.separator + "IMG_0065.mp4";
        //String temproryImages = "images" + File.separator + "EncodedPictures" + File.separator;
        String temproryImages = "video" + File.separator + "test_decoded1" + File.separator;
        String filenameOutput = "";
        String hologramOfCopyrightSymbol = "images" + File.separator + "hologram.bmp";
        String pathToPartsOfCopyrightSymbol = "images" + File.separator + "dividedIM" + File.separator;
        DCTMethodVideo test = new DCTMethodVideo(filenameInput, filenameOutput, 170);
        test.setNumberOfPicturesAll(8);

        ByteImageBMP newCopyright = new ByteImageBMP(hologramOfCopyrightSymbol);
        int numberOfSegments = newCopyright.divideImageOnPart(32, 16, pathToPartsOfCopyrightSymbol, "BMP");
        int startSegment = 0;


        for (int j = 0; j < numberOfSegments; j++) {
            startSegment = 8*j;
            ByteImageBMP newCopyrightPart = new ByteImageBMP(pathToPartsOfCopyrightSymbol + j + ".bmp");
            byte[] outputByte = test.decodeByteCodeFromImage(startSegment, temproryImages);
            newCopyrightPart.saveImageFromByteCode("images" + File.separator + "test" + File.separator + j, BitOperations.bitToShort(outputByte), "BMP");
        }
    }


}
