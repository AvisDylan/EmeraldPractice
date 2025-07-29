/**
 * Created by dylan on 7/21/2025
 */

package com.emeraldnetwork.emeraldPractice.listeners.entity;

import com.emeraldnetwork.emeraldPractice.player.PlayerData;
import com.emeraldnetwork.emeraldPractice.player.PlayerManager;
import com.emeraldnetwork.emeraldPractice.player.PlayerState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class PlayerPickupItemListener implements Listener{
    
    @EventHandler
    public void onPlayerPickupItem(PlayerPickupItemEvent event){
        PlayerData playerData = PlayerManager.getPlayerData(event.getPlayer().getUniqueId());
        
        if(playerData.getPlayerState() == PlayerState.DUEL || playerData.getPlayerState() == PlayerState.FFA)
            return;
            
        event.setCancelled(true);
    }
}
