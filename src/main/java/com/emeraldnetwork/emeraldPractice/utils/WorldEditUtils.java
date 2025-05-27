package com.emeraldnetwork.emeraldPractice.utils;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.io.File;

public class WorldEditUtils{
    
    public static void loadMap(File file, World world){
        MultithreadedUtils.EXECUTOR_SERVICE.submit(() -> {
            WorldEditPlugin worldEditPlugin = (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit");
            
            if(worldEditPlugin == null)
                throw new RuntimeException("WorldEdit is not on the server!");
            
            
        });
    }
}
