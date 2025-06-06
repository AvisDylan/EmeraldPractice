package com.emeraldnetwork.emeraldPractice.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class MultithreadedUtils{
    
    public static final ExecutorService EXECUTOR_SERVICE = Executors.newCachedThreadPool();
}
