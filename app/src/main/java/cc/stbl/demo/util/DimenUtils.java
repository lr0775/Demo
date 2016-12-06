package cc.stbl.demo.util;

import android.content.Context;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;

import cc.stbl.demo.App;

/**
 * Created by lr on 2015/12/9.
 */
public class DimenUtils {

    /**
     * dp转px
     */
    public static int dp2px(float dpVal) {
        final Context context = App.getContext();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }

    /**
     * sp转px
     */
    public static int sp2px(float spVal) {
        final Context context = App.getContext();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, context.getResources().getDisplayMetrics());
    }

    /**
     * px转dp
     */
    public static float px2dp(float pxVal) {
        final Context context = App.getContext();
        final float scale = context.getResources().getDisplayMetrics().density;
        return (pxVal / scale);
    }

    /**
     * px转sp
     */
    public static float px2sp(float pxVal) {
        final Context context = App.getContext();
        return (pxVal / context.getResources().getDisplayMetrics().scaledDensity);
    }

    /**
     * 屏幕宽度
     */
    public static int getScreenWidth() {
        WindowManager wm = (WindowManager) App.getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        return display.getWidth();
    }

    /**
     * 屏幕高度
     */
    public static int getScreenHeight() {
        WindowManager wm = (WindowManager) App.getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        return display.getHeight();
    }

}
