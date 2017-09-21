package mathOperations;

public class BitOperations {
    public static final byte numberOfBit = 8;

    public static byte[] decToBit(short number) {
        short tempNumber = number;
        byte[] bitRepresentation = new byte[numberOfBit];
        for (int i = 0; i < numberOfBit; i++) {
            bitRepresentation[numberOfBit - i - 1] = (byte) (tempNumber % 2);
            tempNumber = (short) (tempNumber / 2);
        }
        return bitRepresentation;
    }

    public static short bitToDec(byte[] number) {
        short result = 0;
        for (int i = 0; i < numberOfBit; i++) {
            result = (short)(result + number[numberOfBit - i - 1]*Math.pow(2,i));
        }
        return result;
    }

    public static byte[] shortToBit(short[] number) {
        byte[] result = new byte[number.length*numberOfBit];
        for (int i = 0, k = 0; i < number.length; i++, k++){
            byte[] symbolInDec = BitOperations.decToBit(number[i]);
            System.arraycopy(symbolInDec, 0, result, k*8, numberOfBit);
//            for(int j = 0; j < 8; j++){
//                result[k*8 + j] = symbolInDec[j];
//            }
        }
        return result;
    }

    public static short[] bitToShort(byte[] number) {
        short[] result = new short[number.length/8];
        for (int j = 0, k = 0; j < result.length; j++, k++){
            byte[] testByte = new byte[8];
            System.arraycopy(number, k*8, testByte, 0, numberOfBit);
//            for(int i = 0; i < 8; i++){
//                testByte[i] = number[k*8+i];
//            }
            result[j]= BitOperations.bitToDec(testByte);
        }
        return result;
    }
}
