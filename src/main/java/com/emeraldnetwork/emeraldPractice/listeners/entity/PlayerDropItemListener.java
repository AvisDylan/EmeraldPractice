package com.emeraldnetwork.emeraldPractice.listeners.entity;

import com.emeraldnetwork.emeraldPractice.match.Match;
import com.emeraldnetwork.emeraldPractice.match.MatchManager;
import com.emeraldnetwork.emeraldPractice.match.MatchState;
import com.emeraldnetwork.emeraldPractice.player.PlayerData;
import com.emeraldnetwork.emeraldPractice.player.PlayerManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class PlayerDropItemListener implements Listener{
    
    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event){
        PlayerData playerData = PlayerManager.getPlayerData(event.getPlayer().getUniqueId());
        
        switch(playerData.getPlayerState()){
            case QUEUE, SPAWN, SPECTATING -> {
                if(!playerData.getProfile().isEditMode())
                    event.setCancelled(true);
            }
            case DUEL -> {
                Match match = MatchManager.getPlayerMatch(playerData);
                
                if(match != null){
                    if(match.getMatchState() == MatchState.STARTING || match.getMatchState() == MatchState.ENDING){
                        event.setCancelled(true);
                        return;
                    }
                    
                    if(!match.getKit().isDrop())
                        event.setCancelled(true);
                }
            }
        }
    }
}
