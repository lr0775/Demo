package cc.stbl.demo.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import cc.stbl.demo.App;

/**
 * Created by tnitf on 2016/6/30.
 */
public class NetUtils {

    /**
     * 网络是否连接
     */
    public static boolean isNetworkAvailable() {
        ConnectivityManager connectivity = (ConnectivityManager) App.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                return true;
            }
        }
        return false;
    }


}
