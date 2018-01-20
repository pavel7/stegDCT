package img;

public class YuvConverter {
    public static void rgb2YUV (RGB rgbPixel, YUV yuvPixel)
    {
        yuvPixel.y = (float)0.299 * rgbPixel.r + (float)0.587 * rgbPixel.g + (float)0.114 * rgbPixel.b;
        yuvPixel.u = (float) -0.14713 * rgbPixel.r - (float) 0.28886 * rgbPixel.g + (float) 0.436 * rgbPixel.b + 128;
        yuvPixel.v = (float) 0.615 * rgbPixel.r - (float) 0.51499 * rgbPixel.g - (float) 0.10001 * rgbPixel.b + 128;
    }

    public static void yuv2RGB (YUV yuvPixel, RGB rgbPixel)
    {
        rgbPixel.r = (short)(yuvPixel.y + 1.13983 * (yuvPixel.v - 128));
        rgbPixel.g = (short)(yuvPixel.y - 0.39465 * (yuvPixel.u - 128) - 0.58060 * (yuvPixel.v - 128));
        rgbPixel.b = (short)(yuvPixel.y + 2.03211 * (yuvPixel.u - 128));
        normalize(rgbPixel);
    }

    private static void normalize (RGB rgbPixel)
    {
        if (rgbPixel.r > 255)
            rgbPixel.r = 255;
        if (rgbPixel.r < 0)
            rgbPixel.r = 0;
        if (rgbPixel.g > 255)
            rgbPixel.g = 255;
        if (rgbPixel.g < 0)
            rgbPixel.g = 0;
        if (rgbPixel.b > 255)
            rgbPixel.b = 255;
        if (rgbPixel.b < 0)
            rgbPixel.b = 0;
    }
}
