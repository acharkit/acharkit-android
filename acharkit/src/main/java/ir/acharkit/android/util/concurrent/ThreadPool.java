package ir.acharkit.android.util.concurrent;

import java.util.concurrent.LinkedBlockingQueue;

import ir.acharkit.android.util.Log;

/**
 * Author:  Alireza Tizfahm Fard
 * Date:    2/8/18
 * Email:   alirezat775@gmail.com
 */
public class ThreadPool {
    private static final String TAG = ThreadPool.class.getName();
    private static int CORE_POOL_SIZE = Runtime.getRuntime().availableProcessors();
    private static ThreadPool instance;
    private final ThreadWorker[] threads;
    private final LinkedBlockingQueue<Runnable> queue;


    /**
     * @param nThreads number thread doing task
     */
    public ThreadPool(int nThreads) {
        queue = new LinkedBlockingQueue<>();
        threads = new ThreadWorker[nThreads];

        for (int i = 0; i < nThreads; i++) {
            threads[i] = new ThreadWorker();
            threads[i].start();
        }
    }

    /**
     * @return instance ThreadPool
     */
    public static ThreadPool getInstance() {
        if (instance == null) {
            instance = new ThreadPool(CORE_POOL_SIZE);
        }
        return instance;
    }

    /**
     * @param task runnable task
     */
    public void execute(Runnable task) {
        synchronized (queue) {
            queue.add(task);
            queue.notify();
        }
    }

    /**
     * cancel thread work
     */
    public void onDestroy() {
        ThreadWorker.interrupted();
    }

    /**
     * new instance of Thread for doing task
     */
    private class ThreadWorker extends Thread {
        public void run() {
            Runnable task;
            while (true) {
                synchronized (queue) {
                    while (queue.isEmpty()) {
                        try {
                            queue.wait();
                        } catch (InterruptedException e) {
                            Log.w(TAG, e);

                        }
                    }
                    task = queue.poll();
                }
                try {
                    task.run();
                } catch (RuntimeException e) {
                    Log.w(TAG, e);
                }
            }
        }
    }
}
