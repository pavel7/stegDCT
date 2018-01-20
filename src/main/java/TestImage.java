import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class TestImage {
    public static void main(String[] args) throws AWTException, IOException {
        BufferedImage image = new Robot().createScreenCapture( new
                Rectangle (Toolkit.getDefaultToolkit().getScreenSize()) );

        BufferedImage screencapture = new BufferedImage(image.getWidth
                (),image.getHeight(), BufferedImage.TYPE_3BYTE_BGR);

        screencapture.getGraphics().drawImage(image, 0, 0, null);

        File imageFile = new File("video" + File.separator + "test.png");
        ImageIO.write(screencapture, "png", imageFile);

        BufferedImage afterReading = ImageIO.read(imageFile);

        Color testColor = new Color(screencapture.getRGB(512,512));
        System.out.println(" R" + testColor.getRed() + " G" + testColor.getGreen() + " B" + testColor.getBlue());
        Color testColorAfter = new Color(afterReading.getRGB(512,512));
        System.out.println(" R" + testColorAfter.getRed() + " G" + testColorAfter.getGreen() + " B" + testColorAfter.getBlue());
    }
}
