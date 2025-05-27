package com.emeraldnetwork.emeraldPractice.listeners.world;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class MobSpawnListener implements Listener{
    
    @EventHandler
    public void onMobSpawn(CreatureSpawnEvent event){
        event.setCancelled(true);
    }
}
