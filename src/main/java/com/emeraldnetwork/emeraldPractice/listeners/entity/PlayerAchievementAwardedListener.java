package com.emeraldnetwork.emeraldPractice.listeners.entity;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAchievementAwardedEvent;

public class PlayerAchievementAwardedListener implements Listener{
    
    @EventHandler
    public void onPlayerAchievementAwarded(PlayerAchievementAwardedEvent event){
        event.setCancelled(true);
    }
}
