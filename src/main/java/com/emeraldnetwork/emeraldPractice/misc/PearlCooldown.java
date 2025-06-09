package com.emeraldnetwork.emeraldPractice.misc;

public class PearlCooldown{
    
    private final long lastPearl;
    private final int schedulerId;
    
    public PearlCooldown(long lastPearl, int schedulerId){
        this.lastPearl = lastPearl;
        this.schedulerId = schedulerId;
    }
    
    public long getLastPearl(){
        return lastPearl;
    }
    
    public int getSchedulerId(){
        return schedulerId;
    }
}
