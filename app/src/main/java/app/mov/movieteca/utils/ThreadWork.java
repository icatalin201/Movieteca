package app.mov.movieteca.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadWork {

    private Thread thread;
    private ExecutorService executorService;

    public ThreadWork(Runnable runnable) {
        thread = new Thread(runnable);
        executorService = Executors.newSingleThreadExecutor();
    }

    public void startExecutor() {
        executorService.execute(thread);
    }

    public void stopExecutor() {
        executorService.shutdown();
    }

}
