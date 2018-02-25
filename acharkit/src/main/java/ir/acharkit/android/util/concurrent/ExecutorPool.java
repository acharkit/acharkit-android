package ir.acharkit.android.util.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Author:  Alireza Tizfahm Fard
 * Date:    2/16/18
 * Email:   alirezat775@gmail.com
 */
public class ExecutorPool {

    private static final String TAG = ExecutorPool.class.getName();
    public static int CORE_POOL_SIZE = 16; //Runtime.getRuntime().availableProcessors();
    private ExecutorService executorPool;

    /**
     * @param executorNumber
     */
    public ExecutorPool(int executorNumber) {
        executorPool = Executors.newFixedThreadPool(executorNumber);
    }

    /**
     * @param runnable
     */
    public void submit(Runnable runnable) {
        getExecutor().submit(runnable);
    }

    /**
     * @return
     */
    private ExecutorService getExecutor() {
        return executorPool;
    }

    /**
     *
     */
    public void onDestroy() {
        getExecutor().shutdown();
    }
}
