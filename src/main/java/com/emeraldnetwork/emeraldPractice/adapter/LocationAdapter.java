package com.emeraldnetwork.emeraldPractice.adapter;

import com.google.gson.annotations.Expose;

public class LocationAdapter{
    
    @Expose
    private String worldName;
    @Expose
    private double x, y, z;
    @Expose
    private float pitch, yaw;
    
    public LocationAdapter(String worldName, double x, double y, double z, float pitch, float yaw){
        this.worldName = worldName;
        this.x = x;
        this.y = y;
        this.z = z;
        this.pitch = pitch;
        this.yaw = yaw;
    }
    
    public String getWorldName(){
        return worldName;
    }
    
    public double getX(){
        return x;
    }
    
    public double getY(){
        return y;
    }
    
    public double getZ(){
        return z;
    }
    
    public float getPitch(){
        return pitch;
    }
    
    public float getYaw(){
        return yaw;
    }
}
