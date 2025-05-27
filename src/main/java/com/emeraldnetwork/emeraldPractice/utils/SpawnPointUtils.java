package com.emeraldnetwork.emeraldPractice.utils;

import com.google.gson.annotations.Expose;
import org.bukkit.Location;

public class SpawnPointUtils{
    
    @Expose
    private static Location spawnPoint;
    
    public static Location getSpawnPoint(){
        return spawnPoint;
    }
    
    public static void setSpawnPoint(Location spawnPoint){
        SpawnPointUtils.spawnPoint = spawnPoint;
    }
}
