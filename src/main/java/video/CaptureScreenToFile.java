package video; /*******************************************************************************
 * Copyright (c) 2014, Art Clarke.  All rights reserved.
 *
 * This file is part of Humble-Video.
 *
 * Humble-Video is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Humble-Video is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with Humble-Video.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/

import io.humble.video.*;
import io.humble.video.awt.MediaPictureConverter;
import io.humble.video.awt.MediaPictureConverterFactory;
import string.StringUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Records the contents of your computer screen to a media file for the passed in duration.
 * This is meant as a demonstration program to teach the use of the Humble API.
 * <p>
 * Concepts introduced:
 * </p>
 * <ul>
 * <li>Muxer: A {@link Muxer} object is a container you can write media data to.</li>
 * <li>Encoders: An {@link Encoder} object lets you convert {@link MediaAudio} or {@link MediaPicture} objects into {@link MediaPacket} objects
 * so they can be written to {@link Muxer} objects.</li>
 * </ul>
 * <p>
 * <p>
 * To run from maven, do:
 * </p>
 * <pre>
 * mvn install exec:java -Dexec.mainClass="io.humble.video.demos.RecordAndEncodeVideo" -Dexec.args="filename.mp4"
 * </pre>
 *
 * @author aclarke
 */
public class CaptureScreenToFile {
    private static Map<String, File> imageMap = new HashMap<String, File>();

    /**
     * Records the screen
     */
    private static void recordScreen(String filename, String inputFolder, String formatname,
                                     String codecname, int framesPerSecond) throws AWTException, InterruptedException, IOException {
        /**
         * Set up the AWT infrastructure to take screenshots of the desktop.
         */
        final Rectangle screenbounds = new Rectangle(512, 512);

        final Rational framerate = Rational.make(1, framesPerSecond);

        /** First we create a muxer using the passed in filename and formatname if given. */
        final Muxer muxer = Muxer.make(filename, null, formatname);

        /** Now, we need to decide what type of codec to use to encode video. Muxers
         * have limited sets of codecs they can use. We're going to pick the first one that
         * works, or if the user supplied a codec name, we're going to force-fit that
         * in instead.
         */
        final MuxerFormat format = muxer.getFormat();
        final Codec codec;
        if (codecname != null) {
            codec = Codec.findEncodingCodecByName(codecname);
        } else {
            codec = Codec.findEncodingCodec(format.getDefaultVideoCodecId());
        }

        /**
         * Now that we know what codec, we need to create an encoder
         */
        Encoder encoder = Encoder.make(codec);

        /**
         * Video encoders need to know at a minimum:
         *   width
         *   height
         *   pixel format
         * Some also need to know frame-rate (older codecs that had a fixed rate at which video files could
         * be written needed this). There are many other options you can set on an encoder, but we're
         * going to keep it simpler here.
         */
        encoder.setWidth(screenbounds.width);
        encoder.setHeight(screenbounds.height);
        // We are going to use 420P as the format because that's what most video formats these days use
//        final PixelFormat.Type pixelformat = PixelFormat.Type.PIX_FMT_YUV420P;
        final PixelFormat.Type pixelformat = PixelFormat.Type.PIX_FMT_YUVJ420P;
        encoder.setPixelFormat(pixelformat);
        encoder.setTimeBase(framerate);

        /** An annoynace of some formats is that they need global (rather than per-stream) headers,
         * and in that case you have to tell the encoder. And since Encoders are decoupled from
         * Muxers, there is no easy way to know this beyond
         */
        if (format.getFlag(MuxerFormat.Flag.GLOBAL_HEADER))
            encoder.setFlag(Encoder.Flag.FLAG_GLOBAL_HEADER, true);

        /** Open the encoder. */
        encoder.open(null, null);


        /** Add this stream to the muxer. */
        muxer.addNewStream(encoder);

        /** And open the muxer for business. */
        muxer.open(null, null);

        /** Next, we need to make sure we have the right MediaPicture format objects
         * to encode data with. Java (and most on-screen graphics programs) use some
         * variant of Red-Green-Blue image encoding (a.k.a. RGB or BGR). Most video
         * codecs use some variant of YCrCb formatting. So we're going to have to
         * convert. To do that, we'll introduce a MediaPictureConverter object later. object.
         */
        final MediaPicture picture = MediaPicture.make(
                encoder.getWidth(),
                encoder.getHeight(),
                pixelformat);
        picture.setTimeBase(framerate);

        File folder = new File(inputFolder);
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

        /** Now begin our main loop of taking screen snaps.
         * We're going to encode and then write out any resulting packets. */
        final MediaPacket packet = MediaPacket.make();
        try {

            for (int index = 0; index < listOfFiles.length; index++) {
                MediaPictureConverter converter = null;
                /** Make the screen capture && convert image to TYPE_3BYTE_BGR */
                final BufferedImage screen = convertToType(getImage(index), BufferedImage.TYPE_3BYTE_BGR);

                /** This is LIKELY not in YUV420P format, so we're going to convert it using some handy utilities. */
                converter = MediaPictureConverterFactory.createConverter(screen, picture);
                converter.toPicture(picture, screen, index);

                do {
                    encoder.encode(packet, picture);
                    if (packet.isComplete())
                        muxer.write(packet, false);
                } while (packet.isComplete());
                System.out.println(index);
            }

        } catch (RuntimeException var6) {
            System.err.println("we can't get permission to capture the screen");
        }

        /** Encoders, like decoders, sometimes cache pictures so it can do the right key-frame optimizations.
         * So, they need to be flushed as well. As with the decoders, the convention is to pass in a null
         * input until the output is not complete.
         */
        do {
            encoder.encode(packet, null);
            if (packet.isComplete())
                muxer.write(packet, false);
        } while (packet.isComplete());

        /** Finally, let's clean up after ourselves. */
        muxer.close();
    }

    public static void main(String[] args) throws AWTException, InterruptedException, IOException {

        int framesPerSecond = 30;
        String filename = "video" + File.separator + "output.mp4";
        String inputFolder = "video" + File.separator + "test_decoded1" + File.separator;
//        String inputFolder = "images" + File.separator + "EncodedPictures" + File.separator;
        String formatname = "mp4";
        String codecname = "libx264";

        recordScreen(filename, inputFolder, formatname, codecname, framesPerSecond);

    }

    /**
     * Convert a {@link BufferedImage} of any type, to {@link BufferedImage} of a
     * specified type. If the source image is the same type as the target type,
     * then original image is returned, otherwise new image of the correct type is
     * created and the content of the source image is copied into the new image.
     *
     * @param sourceImage the image to be converted
     * @param targetType  the desired BufferedImage type
     * @return a BufferedImage of the specifed target type.
     * @see BufferedImage
     */

    public static BufferedImage convertToType(BufferedImage sourceImage,
                                              int targetType) {
        BufferedImage image;

        // if the source image is already the target type, return the source image

        if (sourceImage.getType() == targetType) {
            image = sourceImage;
            System.out.println("++");
        }
            // otherwise create a new image of the target type and draw the new
            // image

        else {
            image = new BufferedImage(sourceImage.getWidth(),
                    sourceImage.getHeight(), targetType);
            image.getGraphics().drawImage(sourceImage, 0, 0, null);
            System.out.println("--");
        }

        return image;
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

    private static long getImageTimeStamp(int index) {
        long timeStamp = -1;
        try {
            String fileIndex = String.valueOf(index);
            //System.out.println("fileName :" + fileName);
            File img = imageMap.get(fileIndex);

            if (img != null) {
                String fileName = img.getName();
                timeStamp = StringUtils.getImageTimeframe(fileName);
            } else {
                System.out.println("++++++++++++++++++++++++++++++++++++++index :" + index);
            }
            return timeStamp;
        } catch (Exception e) {
            e.printStackTrace();
            return timeStamp;
        }

    }


}