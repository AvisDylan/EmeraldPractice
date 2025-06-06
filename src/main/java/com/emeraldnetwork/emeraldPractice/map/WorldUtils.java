package com.emeraldnetwork.emeraldPractice.map;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;

public class WorldUtils{
    
    public static World createVoidWorld(String name){
        WorldCreator creator = new WorldCreator(name);
        
        creator.generator(new ChunkGenerator());
        return Bukkit.createWorld(creator);
    }
    
    public static World createWorld(String name){
        WorldCreator creator = new WorldCreator(name);
        
        return Bukkit.createWorld(creator);
    }
}

