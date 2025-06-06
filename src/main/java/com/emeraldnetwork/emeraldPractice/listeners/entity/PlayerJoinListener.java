package com.emeraldnetwork.emeraldPractice.listeners.entity;

import com.emeraldnetwork.emeraldPractice.player.PlayerManager;
import com.emeraldnetwork.emeraldPractice.utils.SpawnPointUtils;
import fr.mrmicky.fastboard.FastBoard;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener{
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        PlayerManager.addPlayer(event.getPlayer());
        PlayerManager.giveSpawnItems(event.getPlayer());
        event.getPlayer().teleport(SpawnPointUtils.getSpawnPoint());
    }
}
