package com.emeraldnetwork.emeraldPractice.utils;

import org.bukkit.World;

import java.util.Random;

public class ChunkUtils extends org.bukkit.generator.ChunkGenerator{
    
    @Override
    public ChunkData generateChunkData(World world, Random random, int x, int z, BiomeGrid biome){
        return createChunkData(world);
    }
}
