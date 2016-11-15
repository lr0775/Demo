package cc.stbl.demo;

import android.app.Application;

/**
 * Created by Administrator on 2016/10/17.
 */

public class App extends Application {

    private static App sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
    }

    public static App getContext() {
        return sContext;
    }
}
