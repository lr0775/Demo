package cc.stbl.demo.weapon;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by lr on 2016/5/30.
 */
public class ThreadPool {

    private static volatile ThreadPool sInstance;

    public static ThreadPool getInstance() {
        if (sInstance == null) {
            synchronized (ThreadPool.class) {
                if (sInstance == null) {
                    sInstance = new ThreadPool();
                }
            }
        }
        return sInstance;
    }

    private ExecutorService mExecutor;

    private ThreadPool() {
        mExecutor = Executors.newCachedThreadPool();
    }

    public void execute(Runnable runnable) {
        mExecutor.execute(runnable);
    }

}
