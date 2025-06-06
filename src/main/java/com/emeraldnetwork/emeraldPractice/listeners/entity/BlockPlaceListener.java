package com.emeraldnetwork.emeraldPractice.listeners.entity;

import com.emeraldnetwork.emeraldPractice.match.Match;
import com.emeraldnetwork.emeraldPractice.match.MatchManager;
import com.emeraldnetwork.emeraldPractice.player.PlayerData;
import com.emeraldnetwork.emeraldPractice.player.PlayerManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlaceListener implements Listener{
    
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event){
        PlayerData playerData = PlayerManager.getPlayerData(event.getPlayer().getUniqueId());
        
        switch(playerData.getPlayerState()){
            case QUEUE, SPAWN, SPECTATING -> {
                if(!playerData.getProfile().isEditMode())
                    event.setCancelled(true);
            }
            case DUEL -> {
                Match match = MatchManager.getPlayerMatch(playerData);
                
                if(match != null){
                    /*if(event.getBlockPlaced().getLocation().getY() >= (match.getActiveMap().getMap().getPlayerOneY() + match.getKit().getMaxBuildHeight())){
                        event.setCancelled(true);
                        return;
                    }
                    
                    if(event.getBlockPlaced().getLocation().getY() <= (match.getActiveMap().getMap().getPlayerOneY() + match.getKit().getMinBuildHeight())){
                        event.setCancelled(true);
                        return;
                    }
                    */
                    if(match.getKit().isBlocks())
                        match.getPlayerPlacedBlocks().add(event.getBlockPlaced());
                    else
                        event.setCancelled(true);
                }
            }
        }
    }
}
