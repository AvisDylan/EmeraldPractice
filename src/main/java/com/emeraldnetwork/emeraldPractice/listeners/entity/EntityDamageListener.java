package com.emeraldnetwork.emeraldPractice.listeners.entity;

import com.emeraldnetwork.emeraldPractice.match.Match;
import com.emeraldnetwork.emeraldPractice.match.MatchManager;
import com.emeraldnetwork.emeraldPractice.player.PlayerData;
import com.emeraldnetwork.emeraldPractice.player.PlayerManager;
import com.emeraldnetwork.emeraldPractice.player.PlayerState;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityDamageListener implements Listener{
    
    @EventHandler
    public void onEntityDamage(EntityDamageEvent event){
        if(!(event.getEntity() instanceof Player player))
            return;
        
        PlayerData playerData = PlayerManager.getPlayerData(player.getUniqueId());
        
        if(event.getCause() == EntityDamageEvent.DamageCause.FALL){
            switch(playerData.getPlayerState()){
                case QUEUE, SPAWN, SPECTATING -> {
                    event.setCancelled(true);
                }
                case DUEL -> {
                    Match match = MatchManager.getPlayerMatch(playerData);
                    
                    if(match != null){
                        if(!match.getKit().isFallDamage())
                            event.setCancelled(true);
                    }
                }
            }
        }else if(event.getCause() == EntityDamageEvent.DamageCause.VOID){
            if(playerData.getPlayerState() == PlayerState.DUEL)
                player.setHealth(0);
        }
    }
}
