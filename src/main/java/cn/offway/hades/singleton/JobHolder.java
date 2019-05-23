package cn.offway.hades.singleton;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;

/**
 * singleton-multithreading-in-java
 * https://stackoverflow.com/a/13246515
 */
public class JobHolder {
    private static final HashMap<String, ExecutorService> poolMap = new HashMap<>();

    private JobHolder() {
    }

    public static synchronized HashMap<String, ExecutorService> getHolder() {
        return poolMap;
    }
}
