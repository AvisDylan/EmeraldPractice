package com.emeraldnetwork.emeraldPractice.utils;

import java.time.Duration;

public final class FormatUtils{
    
    public static String formateTime(long time){
        Duration duration = Duration.ofMillis(time);
        
        long minutes = duration.toMinutes();
        long seconds = duration.minusSeconds(minutes * 60).getSeconds();
        
        return String.format("%02d:%02d", minutes, seconds);
    }
    
    public static String formatOrdinalNumbers(int index){
        String formattedOrdinalNumber;
        
        if(index - 1 % 10 == 0)
            formattedOrdinalNumber = index + "st";
        else if(index - 2 % 10 == 0)
            formattedOrdinalNumber = index + "nd";
        else if(index - 3 % 10 == 0)
            formattedOrdinalNumber = index + "rd";
        else
            formattedOrdinalNumber = index + "th";
        
        return formattedOrdinalNumber;
    }
}
