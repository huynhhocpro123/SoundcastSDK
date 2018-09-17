package vn.mobiapps.soundcastsdk.until;

import java.util.Map;

/**
 * Created by soundcast on 7/22/2018.
 */

public class Contanst {

    public static String TOKEN;
    public static Map DATA = null;
    public static String URLADVERTISEMENT;
    public static int SECTIONTIMEOUT = 5000;
    public static int TIMEOUT = 0;
    public static final String URLRQUEST = "https://e-stg.api.soundcast.fm/e?token=";
    public static final String ERROR = "Error Resource NotFound";
    public static final String ERRORTIMEOUT = "Connection timeout";
    public static final String ERRORParserXML = "Can not ParserXML";
    public static final int MILISECONDS = 1000;
    public static final String URLRQ = "https://delivery-stg.api.soundcast.fm/network/";
    public static final String START = "&name=start";
    public static final String SKIP = "&name=skip";
    public static final String CRREATIVEVIEW = "&name=creativeView";
    public static final String FIRSTQUARTILE = "&name=firstquartile";
    public static final String MIDPOINT = "&name=midpoint";
    public static final String THIRDQUARTILE = "&name=thirdquartile";
    public static final String COMPLETE = "&name=complete";
    public static boolean CHECKAUDIO = false;
    public static int SECONDS = 0;
}
