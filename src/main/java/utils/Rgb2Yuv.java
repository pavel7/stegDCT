package utils;

public class Rgb2Yuv {
    private static int r;
    private static int g;
    private static int b;
    private static double y;
    private static double u;
    private static double v;


    public static void convertRgb2Yuv (int r, int g, int b){
        y = 0.299 * r + 0.587 * g + 0.114 * b;
        u = -0.14713 * r + -0.28886 * g + 0.436 * b + 128;
        v = 0.615 * r - 0.51499 * g - 0.10001 * b + 128;

    }

    public static void convertYuv2Rgb (double y, double u, double v){
        r = (int) (y + 1.13983 * (v - 128));
        g = (int) (y - 0.39465 * (u - 128) - 0.58060 * (v - 128));
        b = (int) (y + 2.03211 * (u - 128));
    }

    public static void main(String[] args) {

        r = 0;
        g = 0;
        b = 0;
        convertRgb2Yuv(r, g, b);
        System.out.println("y="+y+" u="+u+" v="+v);
        convertYuv2Rgb(y, u, v);
        System.out.println("r="+r+" g="+g+" b="+b);
        r = 255;
        g = 255;
        b = 255;
        convertRgb2Yuv(r, g, b);
        System.out.println("y="+y+" u="+u+" v="+v);
        convertYuv2Rgb(y, u, v);
        System.out.println("r="+r+" g="+g+" b="+b);
        r = 236;
        g = 236;
        b = 236;
        convertRgb2Yuv(r, g, b);
        System.out.println("y="+y+" u="+u+" v="+v);
        convertYuv2Rgb(y, u, v);
        System.out.println("r="+r+" g="+g+" b="+b);

    }
}
