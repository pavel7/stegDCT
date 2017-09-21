package string;

import java.util.regex.Pattern;

/**
 * Created by pavelcherepanov on 14.05.17.
 */
public class StringUtils {

    public static int getImageIndex (String imageName) {
        Pattern nameDelimiter = Pattern.compile("-");
        String[] allPartsOfImageName = nameDelimiter.split(imageName, 2);
        return Integer.parseInt(allPartsOfImageName[0]);
    }

    public static long getImageTimeframe (String imageName){
        Pattern nameDelimiter = Pattern.compile("-");
        String[] allPartsOfImageName = nameDelimiter.split(imageName);
        return Long.parseLong(allPartsOfImageName[1]);
    }

}
