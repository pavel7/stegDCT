import io.humble.video.MediaPicture;
import io.humble.video.PixelFormat;
import io.humble.video.awt.MediaPictureConverter;
import io.humble.video.awt.MediaPictureConverterFactory;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;


public class TestConverter
{
    public static void main(String args[]) throws
            AWTException,IOException
    {
        // capture the whole screen
        BufferedImage image = new Robot().createScreenCapture( new
                Rectangle (Toolkit.getDefaultToolkit().getScreenSize()) );

        BufferedImage screencapture = new BufferedImage(image.getWidth
                (),image.getHeight(), BufferedImage.TYPE_3BYTE_BGR);

        screencapture.getGraphics().drawImage(image, 0, 0, null);

        File beforeConvert = new File("video" + File.separator + "BeforeConverter.png");
        ImageIO.write(screencapture, "png", beforeConvert);

        System.out.println(MediaPictureConverterFactory.findDescriptor(screencapture));


        MediaPictureConverter converterToYUV420P = MediaPictureConverterFactory.createConverter(screencapture, PixelFormat.Type.PIX_FMT_YUV444P);
//        System.out.println(converterToYUV420P.getDescription());
        MediaPicture picture = converterToYUV420P.toPicture(null, screencapture, 1);


        MediaPictureConverter converterToBGR24 = MediaPictureConverterFactory.createConverter(MediaPictureConverterFactory.HUMBLE_BGR_24, picture);
//        System.out.println(converterToBGR24.getDescription());
        BufferedImage afterImageConverting = converterToBGR24.toImage(null, picture);


        // Save as JPEG
        File afterConvert = new File("video" + File.separator + "AfterConverter.png");
        ImageIO.write(afterImageConverting, "png",afterConvert);

        int number = 0;

        System.out.println(screencapture.getHeight()*screencapture.getWidth());

        for (int x = 0; x < screencapture.getWidth(); x++)
            for (int y = 0; y < screencapture.getHeight(); y++)
            {
        Color testColor = new Color(screencapture.getRGB(x,y));
        Color testColorAfter = new Color(afterImageConverting.getRGB(x,y));
                if ((testColor.getBlue()!= testColorAfter.getBlue()))//||(testColor.getGreen()!= testColorAfter.getGreen())||(testColor.getRed()!= testColorAfter.getRed()))
                {
                    number++;
                }
        //System.out.println(" R" + testColor.getRed() + " G" + testColor.getGreen() + " B" + testColor.getBlue());

        //System.out.println(" R" + testColorAfter.getRed() + " G" + testColorAfter.getGreen() + " B" + testColorAfter.getBlue());
            }

        System.out.println(number);

    }

    public void createType()
    {
        //MediaPictureConverterFactory.Type YUVBGR = new MediaPictureConverterFactory.Type("3BYTE_BGR", TestConverter,PixelFormat.Type.PIX_FMT_YUV420P, BufferedImage.TYPE_3BYTE_BGR);
    }

}