package utils;

import io.humble.video.MediaPicture;
import io.humble.video.PixelFormat;

import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;

public class CustomBgrConverter extends AMediaPictureConverter {
    // band offsets requried by the sample model

    private static final int[] mBandOffsets = { 2, 1, 0 };

    // color space for this converter

    private static final ColorSpace mColorSpace = ColorSpace
            .getInstance(ColorSpace.CS_sRGB);

    // a private copy we use as the resample buffer when converting back and forth. saves time.
    private MediaPicture mResampleMediaPicture;

    public CustomBgrConverter(PixelFormat.Type pictureType, int pictureWidth,
                        int pictureHeight, int imageWidth, int imageHeight) {
        super(pictureType, PixelFormat.Type.PIX_FMT_BGR24,
                BufferedImage.TYPE_3BYTE_BGR, pictureWidth, pictureHeight, imageWidth,
                imageHeight);
        mResampleMediaPicture = willResample() ? MediaPicture.make(imageWidth,
                imageHeight, getRequiredPictureType()) : null;
    }

    @Override
    public MediaPicture toPicture(MediaPicture output, BufferedImage input, long timestamp) {

        validateImage(input);

        if (output == null) {
            output = MediaPicture.make(mPictureWidth,
                    mPictureHeight, getPictureType());
        }

        // get the image byte buffer buffer

        DataBuffer imageBuffer = input.getRaster().getDataBuffer();
        byte[] imageBytes = null;
        int[] imageInts = null;

        // handle byte buffer case

        if (imageBuffer instanceof DataBufferByte) {
            imageBytes = ((DataBufferByte) imageBuffer).getData();
        }

        // handle integer buffer case

        else if (imageBuffer instanceof DataBufferInt) {
            imageInts = ((DataBufferInt) imageBuffer).getData();
        }

        // if it's some other type, throw

        else {
            throw new IllegalArgumentException(
                    "Unsupported BufferedImage data buffer type: "
                            + imageBuffer.getDataType());
        }




        return null;
    }

    @Override
    public BufferedImage toImage(BufferedImage output, MediaPicture input) {
        return null;
    }

    public void delete() {
        if (mResampleMediaPicture != null)
            mResampleMediaPicture.delete();
        mResampleMediaPicture = null;

        super.delete();
    }
}
