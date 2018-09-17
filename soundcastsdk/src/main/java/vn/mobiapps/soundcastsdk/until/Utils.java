package vn.mobiapps.soundcastsdk.until;

import java.text.SimpleDateFormat;

/**
 * Created by soundcast on 7/30/2018.
 */

public class Utils {
    public static String TimeFormat(int time) {
        String t = "";
        SimpleDateFormat timeFormat = new SimpleDateFormat("mm:ss");
        t = timeFormat.format(time);
        return t;
    }
}
