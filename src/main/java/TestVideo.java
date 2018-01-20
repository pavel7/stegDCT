import java.io.File;


import img.ByteImageBMP;
import img.Hologram;
import mathOperations.BitOperations;
import steganographicMethods.DCTMethodVideo;
import steganographicMethods.DCTMethodVideoFloat;
import video.DecodeVideo;

public class TestVideo {

    public static void main(String a[]) throws Exception {
        String filenameInput = "video" + File.separator + "IMG_0065.mp4";
        String temproryImages = "video" + File.separator + "test_decoded" + File.separator;
        String filenameOutput = "images" + File.separator + "EncodedPictures" + File.separator;
        DCTMethodVideoFloat test = new DCTMethodVideoFloat(filenameOutput, 25);
        test.setNumberOfPicturesAll(8);
        String copyrightSymbol = "images" + File.separator + "test.bmp";
        String hologramOfCopyrightSymbol = "images" + File.separator + "hologram.bmp";
        String pathToPartsOfCopyrightSymbol = "images" + File.separator + "dividedIM" + File.separator;
        Hologram.useHologram(copyrightSymbol, hologramOfCopyrightSymbol, 128.0, 3, 128, 128);
        //Hologram.useHologram(hologramOfCopyrightSymbol + ".bmp", "images\\testFromHologram", 0.0, 3, 128, 128);
        ByteImageBMP newCopyright = new ByteImageBMP(hologramOfCopyrightSymbol);
        //ByteImageBMP newCopyright = new ByteImageBMP(copyrightSymbol);

        int numberOfSegments = newCopyright.divideImageOnPart(32, 16, pathToPartsOfCopyrightSymbol, "BMP");
        int startSegment = 0;

        for (int i = 0; i < numberOfSegments; i++) {
            startSegment = 8 * i;
            ByteImageBMP newCopyrightPart = new ByteImageBMP(pathToPartsOfCopyrightSymbol + i + ".bmp");
            short[] tempPix = newCopyrightPart.getByteCodeOfImage();
            byte[] testByte = BitOperations.shortToBit(tempPix);
//        BinaryImageBMP newC = new BinaryImageBMP("images\\test.bmp");
//        byte[] testByte = newC.getByteCodeOfImage();
            test.insertByteCode(testByte, startSegment, temproryImages);
        }

    }
}
