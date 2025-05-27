package com.emeraldnetwork.emeraldPractice.utils;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;

import java.util.Random;
import java.util.stream.Collectors;

public class WorldUtils{
    
    public static World createVoidWorld(){
        WorldCreator creator = new WorldCreator(new Random().ints(97, 122  + 1).limit(16)
                                                                                                                    .mapToObj(i -> String.valueOf((char) i))
                                                                                                                    .collect(Collectors.joining()));
        
        creator.generator(new ChunkUtils());
        return Bukkit.createWorld(creator);
    }
}

