package com.emeraldnetwork.emeraldPractice.listeners.entity;

import com.emeraldnetwork.emeraldPractice.match.Match;
import com.emeraldnetwork.emeraldPractice.match.MatchManager;
import com.emeraldnetwork.emeraldPractice.player.PlayerData;
import com.emeraldnetwork.emeraldPractice.player.PlayerManager;
import com.emeraldnetwork.emeraldPractice.player.PlayerState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class LeaveListener implements Listener{

    @EventHandler
    public void onLeave(PlayerQuitEvent event){
        PlayerData playerData = PlayerManager.getPlayerData(event.getPlayer());
        
        if(playerData.getPlayerState() == PlayerState.DUEL){
            Match match = MatchManager.getPlayerMatch(playerData);
            
            if(match != null)
                match.onLeave(playerData);
        }
        
        PlayerManager.removePlayer(event.getPlayer());
    }
}
