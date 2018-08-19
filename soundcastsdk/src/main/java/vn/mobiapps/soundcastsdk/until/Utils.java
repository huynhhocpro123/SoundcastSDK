package vn.mobiapps.soundcastsdk.until;

import android.app.Activity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.text.SimpleDateFormat;

/**
 * Created by NguyenTanHuynh on 7/30/2018.
 */

public class Utils {

    public static void hideKeyboard(Activity context) {
        try {
            InputMethodManager imm = (InputMethodManager) context.getBaseContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
            View focusedView = context.getCurrentFocus();
            if (focusedView != null) {
                imm.hideSoftInputFromWindow(focusedView.getWindowToken(), 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String TimeFormat(int time) {
        String t = "";
        SimpleDateFormat timeFormat = new SimpleDateFormat("mm:ss");
        t = timeFormat.format(time);
        return t;
    }
}
