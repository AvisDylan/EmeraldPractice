package com.emeraldnetwork.emeraldPractice.listeners.entity;

import com.emeraldnetwork.emeraldPractice.player.PlayerManager;
import com.emeraldnetwork.emeraldPractice.utils.SpawnPointUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener{
    
    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        PlayerManager.addPlayer(event.getPlayer());
        PlayerManager.giveSpawnItems(event.getPlayer());
        event.getPlayer().teleport(SpawnPointUtils.getSpawnPoint());
    }
}
