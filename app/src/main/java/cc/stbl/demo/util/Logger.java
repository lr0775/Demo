package cc.stbl.demo.util;

import android.util.Log;

/**
 * Created by tnitf on 2016/6/30.
 */
public class Logger {

    private static final String TAG = "Logger";
    private static boolean sDebug = true;

    public static void setDebug(boolean debug) {
        sDebug = debug;
    }

    public static void i(String msg) {
        i(TAG, msg);
    }

    public static void i(String tag, String msg) {
        if (sDebug) {
            Log.i(tag, msg);
        }
    }

    public static void e(String msg) {
        e(TAG, msg);
    }

    public static void e(String tag, String msg) {
        if (sDebug) {
            Log.e(tag, msg);
        }
    }

}
