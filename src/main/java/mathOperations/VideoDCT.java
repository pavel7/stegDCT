package mathOperations;

import java.util.ArrayList;
import java.util.concurrent.Callable;


public class VideoDCT implements Callable {
    private int sizeOfSegments = 8;
    private int firstElem = 0;
    private int lastElem = 0;
    private ArrayList<short[][][]> input = null;
    private ArrayList<double[][][]> result = null;

    public ArrayList<double[][][]> call() {
        result = calculateSpectrCoefOfDCT(input);
        return result;
    }

    public VideoDCT(ArrayList<short[][][]> inputList, int startElem, int endElem) {
        input = new ArrayList<>();
        for (int i = startElem; i < endElem; i++) {
            input.add(inputList.get(i).clone());
        }
        result = new ArrayList<>();
    }

    public VideoDCT(ArrayList<short[][][]> inputList, int startElem, int endElem, int newSizeOfSigments) {
        input = new ArrayList<>();
        for (int i = startElem; i < endElem; i++) {
            input.add(inputList.get(i).clone());
        }
        result = new ArrayList<>();
        sizeOfSegments = newSizeOfSigments;
    }

    public int getFirstElem(){
        return firstElem;
    }

    public void setFirstElem (int newFirstElem){
        firstElem = newFirstElem;
    }

    public int getLastElem(){
        return lastElem;
    }

    public void setLastElem (int newLastElem){
        lastElem = newLastElem;
    }

    public ArrayList<double[][][]> getResult(){
        return result;
    }

    private ArrayList<double[][][]> calculateSpectrCoefOfDCT(ArrayList<short[][][]> listOfSegments) {
        ArrayList<double[][][]> listOfSpectrCoefOfDCT = new ArrayList(listOfSegments.size());
        int listOfSegmentSize = listOfSegments.size();
        for (int i = 0; i < listOfSegmentSize; i++) {
            double[][][] tempMassive = new double[sizeOfSegments][sizeOfSegments][sizeOfSegments];
            for (int t = 0; t < sizeOfSegments; t++) {
                for (int j = 0; j < sizeOfSegments; j++) {
                    for (int k = 0; k < sizeOfSegments; k++) {
                        double sum = 0;
                        for (int tempImage = 0; tempImage < sizeOfSegments; tempImage++) {
                            for (int tempColumn = 0; tempColumn < sizeOfSegments; tempColumn++) {
                                for (int tempRow = 0; tempRow < sizeOfSegments; tempRow++) {
                                    sum = sum +
                                            listOfSegments.get(i)[tempImage][tempColumn][tempRow] *
                                                    Math.cos(Math.PI * t * (2 * tempImage + 1) / (2 * sizeOfSegments)) *
                                                    Math.cos(Math.PI * j * (2 * tempColumn + 1) / (2 * sizeOfSegments)) *
                                                    Math.cos(Math.PI * k * (2 * tempRow + 1) / (2 * sizeOfSegments));
                                }
                            }
                        }
                        tempMassive[t][j][k] = sigmaCoefficient(t) * sigmaCoefficient(j) * sigmaCoefficient(k) * sum * Math.sqrt(8.0 / ((double) (sizeOfSegments * sizeOfSegments * sizeOfSegments)));
                    }
                }
            }
            System.out.println(i + "first");
            listOfSpectrCoefOfDCT.add(tempMassive);
        }
        return listOfSpectrCoefOfDCT;
    }

    private double sigmaCoefficient(int sigma) {
        if (sigma == 0) {
            return 1.0 / Math.sqrt(2);
        }
        return 1;
    }
}
