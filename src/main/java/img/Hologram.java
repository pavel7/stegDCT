package img;

import java.io.File;

/**
 * Created by Павел on 31.01.2016.
 */
public class Hologram {

    private static void normFunctionTemp(double[][] input, int numberOfColumn, int numberOfRow) {
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        for (int i = 0; i < numberOfColumn; i++) {
            for (int j = 0; j < numberOfRow; j++) {
                if (input[i][j] > max) max = input[i][j];
                if (input[i][j] < min) min = input[i][j];
            }
        }
        for (int i = 0; i < numberOfColumn; i++) {
            for (int j = 0; j < numberOfRow; j++) {
                input[i][j] = 255 * (input[i][j] + Math.abs(min)) / (max + Math.abs(min));
            }
        }
    }

    private static void normFunction(double[][] input, double intensivity, int numberOfColumn, int numberOfRow) {
        double max = 0;
        for (int i = 0; i < numberOfColumn; i++) {
            for (int j = 0; j < numberOfRow; j++) {
                if (max < Math.abs(input[i][j])) {
                    max = Math.abs(input[i][j]);
                }
            }
        }
        if (max != 0) {
            for (int i = 0; i < numberOfColumn; i++) {
                for (int j = 0; j < numberOfRow; j++) {
                    {
                        input[i][j] = Math.abs(Math.pow((input[i][j] / max * intensivity * 127.5 + 127.5), 2) / 255.0);
                    }
                    if (input[i][j] > 255)
                        input[i][j] = 255;
                }
            }
        }
    }

    private static void normFunctionBack(double[][] input, int numberOfColumn, int numberOfRow) {
        double max = 0;
        for (int i = 0; i < numberOfColumn; i++) {
            for (int j = 0; j < numberOfRow; j++) {
                if (max < input[i][j]) {
                    max = input[i][j];
                }
            }
        }
        for (int i = 0; i < numberOfColumn; i++) {
            for (int j = 0; j < numberOfRow; j++) {
                if (max != 0) {
                    input[i][j] = input[i][j] / max * 255;
                }
            }
        }
    }

    public static int useHologram(String inputImage, String outputHologram, double valueOfLightOnPhotographicPlate, double intesivity, int numberOfColumnHolog, int numberOfRowHolog) {
        ByteImageBMP newC = new ByteImageBMP(inputImage);
        double wavelengthOgLight = 0.001; //длина волны источника света
        // вычислим яркость каждой точки искомого изображения
        int numberOfColumn = newC.getNumberOfColumn();
        int numberOfRow = newC.getNumberOfRow();
        if ((numberOfColumn != numberOfRow) || (numberOfColumnHolog != numberOfRowHolog))
            return -1;
        double ratioHolog = numberOfColumnHolog / (double) numberOfColumn;
        double[][] newHologramm = new double[numberOfColumnHolog][numberOfRowHolog];
        for (int x1 = 0; x1 < numberOfColumnHolog; x1++) {
            for (int y1 = 0; y1 < numberOfRowHolog; y1++) {
                //опорный свет - считаем, что источник света излучает свет с силой достаточной для освещения каждого пикселя с
                // силой 256 единиц, но половину света мы отправили на объект, а половину света направили на фотопластинку (128 единиц).
                double lightOnPhotographicPlate = valueOfLightOnPhotographicPlate; //эта часть света уже достигла фотопластинки, минуя объект (д.б. =0 при декодировании)
                // яркость каждой точки искомого изображения - равна сумме света пришедшего ОТО ВСЕХ точек исходного изображения
                for (int x0 = 0; x0 < numberOfColumn; x0++) {
                    for (int y0 = 0; y0 < numberOfRow; y0++) {
                        //расстояние от точки (x0;y0) до точки (x1;y1) считаем что везде z=1, плоская голограмма
                        double d = Math.sqrt(1 + (x0 * ratioHolog - x1) * (x0 * ratioHolog - x1) + (y0 * ratioHolog - y1) * (y0 * ratioHolog - y1));
                        // Фаза волны достигшей этой точки
                        double phase = d / wavelengthOgLight;
                        //доля света от очередной точки объекта (по поводу коэффициента 0.3 - будет написано далее)
                        double obj_light = (newC.getColor(x0, y0)) * 3;
                        //суммируем свет от очередной точки (в соответствии с фазой световой волны пришедшей от этой точки)
                        lightOnPhotographicPlate += obj_light * Math.cos(phase);
                    }
                }
                //волна оставляет пятно в любом случае, плюс она в этой точке или минус.
                //lightOnPhotographicPlate = Math.abs(lightOnPhotographicPlate);
                //на выходе имеем - интенсивность света в точке (x1;y1)
                newHologramm[x1][y1] = lightOnPhotographicPlate;
            }
        }
        normFunction(newHologramm, intesivity, numberOfColumnHolog, numberOfRowHolog);
        ByteImageBMP.saveImageFromMassive(outputHologram, newHologramm, numberOfColumnHolog, numberOfRowHolog);
        return 1;
    }

    public static int useHologramBack(String inputImage, String outputHologram, int numberOfColumnHolog, int numberOfRowHolog) {
        ByteImageBMP newC = new ByteImageBMP(inputImage);
        double wavelengthOgLight = 0.001; //длина волны источника света
        // вычислим яркость каждой точки искомого изображения
        int numberOfColumn = newC.getNumberOfColumn();
        int numberOfRow = newC.getNumberOfRow();
        if ((numberOfColumn != numberOfRow) || (numberOfColumnHolog != numberOfRowHolog))
            return -1;
        double ratioHolog = numberOfColumn / (double) numberOfColumnHolog;
        double[][] newHologramm = new double[numberOfColumnHolog][numberOfRowHolog];
        for (int x1 = 0; x1 < numberOfColumnHolog; x1++) {
            for (int y1 = 0; y1 < numberOfRowHolog; y1++) {
                //опорный свет - считаем, что источник света излучает свет с силой достаточной для освещения каждого пикселя с
                // силой 256 единиц, но половину света мы отправили на объект, а половину света направили на фотопластинку (128 единиц).
                double lightOnPhotographicPlate = 0.0; //эта часть света уже достигла фотопластинки, минуя объект (д.б. =0 при декодировании)
                // яркость каждой точки искомого изображения - равна сумме света пришедшего ОТО ВСЕХ точек исходного изображения
                for (int x0 = 0; x0 < numberOfColumn; x0++) {
                    for (int y0 = 0; y0 < numberOfRow; y0++) {
                        //расстояние от точки (x0;y0) до точки (x1;y1) считаем что везде z=1, плоская голограмма
                        double d = Math.sqrt(1 + (x1 * ratioHolog - x0) * (x1 * ratioHolog - x0) + (y1 * ratioHolog - y0) * (y1 * ratioHolog - y0));
                        // Фаза волны достигшей этой точки
                        double phase = d / wavelengthOgLight;
                        //доля света от очередной точки объекта (по поводу коэффициента 0.3 - будет написано далее)
                        double obj_light = (newC.getColor(x0, y0));
                        //суммируем свет от очередной точки (в соответствии с фазой световой волны пришедшей от этой точки)
                        lightOnPhotographicPlate += obj_light * Math.cos(phase);
                    }
                }
                //волна оставляет пятно в любом случае, плюс она в этой точке или минус.
                //lightOnPhotographicPlate = Math.abs(lightOnPhotographicPlate);
                //на выходе имеем - интенсивность света в точке (x1;y1)
                newHologramm[x1][y1] = lightOnPhotographicPlate * lightOnPhotographicPlate;
            }
        }
        normFunctionBack(newHologramm, numberOfColumnHolog, numberOfRowHolog);
        ByteImageBMP.saveImageFromMassive(outputHologram, newHologramm, numberOfColumnHolog, numberOfRowHolog);
        return 1;
    }

    public static void main(String[] args) {
        //useHologram("images"+ File.separator +"test.bmp", "images"+ File.separator +"hologram.bmp", 128.0, 3, 128, 128);
        useHologramBack("images" + File.separator + "hologram.bmp", "images" + File.separator + "extractedImage.bmp", 64, 64);

    }
}
