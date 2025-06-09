package com.emeraldnetwork.emeraldPractice.listeners.entity;

import com.emeraldnetwork.emeraldPractice.match.Match;
import com.emeraldnetwork.emeraldPractice.match.MatchManager;
import com.emeraldnetwork.emeraldPractice.player.PlayerData;
import com.emeraldnetwork.emeraldPractice.player.PlayerManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener{
    
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event){
        event.setDeathMessage(null);
        event.setKeepInventory(true);
        event.getEntity().spigot().respawn();
        
        PlayerData playerData = PlayerManager.getPlayerData(event.getEntity().getUniqueId());
        
        switch(playerData.getPlayerState()){
            case DUEL -> {
                Match match = MatchManager.getPlayerMatch(playerData);
                
                if(match != null){
                    if(match.getKit().isDeathDrops())
                        event.getDrops().clear();
                }
            }
            case FFA -> {
            
            }
            default -> event.getDrops().clear();
        }
    }
}
