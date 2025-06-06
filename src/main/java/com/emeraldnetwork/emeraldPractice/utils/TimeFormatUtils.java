package com.emeraldnetwork.emeraldPractice.utils;

import java.time.Duration;

public class TimeFormatUtils{

    public static String formateTime(long time){
        Duration duration = Duration.ofMillis(time);
        
        long minutes = duration.toMinutes();
        long seconds = duration.minusSeconds(minutes * 60).getSeconds();
        
        return String.format("%02d:%02d", minutes, seconds);
    }
}
