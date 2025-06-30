/**
 * Created by dylan on 6/30/2025
 */

package com.emeraldnetwork.emeraldPractice.listeners.entity;

import com.emeraldnetwork.emeraldPractice.match.Match;
import com.emeraldnetwork.emeraldPractice.match.MatchManager;
import com.emeraldnetwork.emeraldPractice.player.PlayerData;
import com.emeraldnetwork.emeraldPractice.player.PlayerManager;
import com.emeraldnetwork.emeraldPractice.player.PlayerState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;

public class EntityRegainHealthListener implements Listener{
    
    @EventHandler
    public void onEntityRegainHealth(EntityRegainHealthEvent event){
        if(!(event.getEntity() instanceof Player player))
            return;
        
        EntityRegainHealthEvent.RegainReason regainReason = event.getRegainReason();
        PlayerData playerData = PlayerManager.getPlayerData(player.getUniqueId());
        
        if(playerData.getPlayerState() != PlayerState.DUEL)
            return;
        
        Match match = MatchManager.getPlayerMatch(playerData);
        
        if(match == null)
            return;
        
        if(match.getKit().isHealthRegen())
            return;
        
        if(regainReason == EntityRegainHealthEvent.RegainReason.SATIATED || regainReason == EntityRegainHealthEvent.RegainReason.REGEN)
            event.setCancelled(true);
    }
}
