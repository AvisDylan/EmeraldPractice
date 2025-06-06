package com.emeraldnetwork.emeraldPractice.map;

import com.emeraldnetwork.emeraldPractice.utils.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;

import java.io.File;

public class ActiveMap{
    
    private final File activeWorldFolder;
    private Map map;
    private org.bukkit.World world;
    
    public ActiveMap(Map map){
        this.map = map;
        activeWorldFolder = new File(Bukkit.getWorldContainer().getParentFile(), map.getWorldFolder().getName() + "_active_" + System.currentTimeMillis());
    }
    
    public boolean load(){
        FileUtils.copy(map.getWorldFolder(), activeWorldFolder);
        
        world = Bukkit.createWorld(new WorldCreator(activeWorldFolder.getName()));
        
        if(world == null){
            Bukkit.getLogger().severe("Failed to load world!");
            return false;
        }
        
        world.setAutoSave(false);
        
        return true;
    }
    
    public void cleanUp(){
        Bukkit.unloadWorld(world, false);
        FileUtils.delete(activeWorldFolder);
    }
    
    public Map getMap(){
        return map;
    }
    
    public void setMap(Map map){
        this.map = map;
    }
    
    public World getWorld(){
        return world;
    }
}
