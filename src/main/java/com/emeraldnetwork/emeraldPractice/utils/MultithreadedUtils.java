package com.emeraldnetwork.emeraldPractice.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultithreadedUtils{
    
    public static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
}
