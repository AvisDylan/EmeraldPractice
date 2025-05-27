package com.emeraldnetwork.emeraldPractice.listeners.entity;

import com.emeraldnetwork.emeraldPractice.match.Match;
import com.emeraldnetwork.emeraldPractice.match.MatchManager;
import com.emeraldnetwork.emeraldPractice.player.PlayerData;
import com.emeraldnetwork.emeraldPractice.player.PlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityDamageListener implements Listener{
    
    @EventHandler
    public void onEntityDamage(EntityDamageEvent event){
        if(event.getCause() != EntityDamageEvent.DamageCause.FALL)
            return;
        
        if(!(event.getEntity() instanceof Player player))
            return;
        
        PlayerData playerData = PlayerManager.getPlayerData(player);
        
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
    }
}
