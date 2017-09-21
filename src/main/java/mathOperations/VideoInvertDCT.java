package mathOperations;

import java.util.ArrayList;
import java.util.concurrent.Callable;

public class VideoInvertDCT implements Callable {
    private int sizeOfSegments = 8;
    private int firstElem = 0;
    private int lastElem = 0;
    private ArrayList<double[][][]> input = null;
    private ArrayList<double[][][]> result = null;

    public ArrayList<double[][][]> call() {
        result = invertDCT(input);
        return result;
    }

    public VideoInvertDCT(ArrayList<double[][][]> inputList, int startElem, int endElem) {
        input = new ArrayList<>();
        for (int i = startElem; i < endElem; i++) {
            input.add(inputList.get(i).clone());
        }
        result = new ArrayList<>();
    }

    public VideoInvertDCT(ArrayList<double[][][]> inputList, int startElem, int endElem, int newSizeOfSigments) {
        input = new ArrayList<>();
        for (int i = startElem; i < endElem; i++) {
            input.add(inputList.get(i).clone());
        }
        result = new ArrayList<>();
        sizeOfSegments = newSizeOfSigments;
    }

    public int getFirstElem() {
        return firstElem;
    }

    public void setFirstElem(int newFirstElem) {
        firstElem = newFirstElem;
    }

    public int getLastElem() {
        return lastElem;
    }

    public void setLastElem(int newLastElem) {
        lastElem = newLastElem;
    }

    public ArrayList<double[][][]> getResult() {
        return result;
    }

    private ArrayList<double[][][]> invertDCT(ArrayList<double[][][]> spectrCoefOfDCT) {
        ArrayList<double[][][]> listOfInvertSpectrCoefOfDCT = new ArrayList(spectrCoefOfDCT.size());
        for (int i = 0; i < spectrCoefOfDCT.size(); i++) {
            double[][][] tempMassive = new double[sizeOfSegments][sizeOfSegments][sizeOfSegments];
            for (int t = 0; t < sizeOfSegments; t++) {
                for (int j = 0; j < sizeOfSegments; j++) {
                    for (int k = 0; k < sizeOfSegments; k++) {
                        double sum = 0;
                        for (int tempImage = 0; tempImage < sizeOfSegments; tempImage++) {
                            for (int tempColumn = 0; tempColumn < sizeOfSegments; tempColumn++) {
                                for (int tempRow = 0; tempRow < sizeOfSegments; tempRow++) {
                                    sum = sum + sigmaCoefficient(tempImage) *
                                            sigmaCoefficient(tempColumn) *
                                            sigmaCoefficient(tempRow) *
                                            spectrCoefOfDCT.get(i)[tempImage][tempColumn][tempRow] *
                                            Math.cos(Math.PI * tempImage * (2 * t + 1) / (2 * sizeOfSegments)) *
                                            Math.cos(Math.PI * tempColumn * (2 * j + 1) / (2 * sizeOfSegments)) *
                                            Math.cos(Math.PI * tempRow * (2 * k + 1) / (2 * sizeOfSegments));
                                }
                            }
                        }
                        tempMassive[t][j][k] = sum * Math.sqrt(8.0 / ((double) (sizeOfSegments * sizeOfSegments * sizeOfSegments)));
                    }
                }
            }
            System.out.println(i + "second");
            listOfInvertSpectrCoefOfDCT.add(tempMassive);
        }
        return listOfInvertSpectrCoefOfDCT;
    }

    private double sigmaCoefficient(int sigma) {
        if (sigma == 0) {
            return 1.0 / Math.sqrt(2);
        }
        return 1;
    }
}
