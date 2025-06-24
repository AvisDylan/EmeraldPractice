/**
 * Created by dylan on 6/24/2025
 */

package com.emeraldnetwork.emeraldPractice.listeners.entity;

import com.emeraldnetwork.emeraldPractice.map.ActiveMap;
import com.emeraldnetwork.emeraldPractice.map.Map;
import com.emeraldnetwork.emeraldPractice.match.Match;
import com.emeraldnetwork.emeraldPractice.match.MatchManager;
import com.emeraldnetwork.emeraldPractice.player.PlayerData;
import com.emeraldnetwork.emeraldPractice.player.PlayerManager;
import com.emeraldnetwork.emeraldPractice.player.PlayerState;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerRespawnListener implements Listener{
    
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event){
        PlayerData playerData = PlayerManager.getPlayerData(event.getPlayer().getUniqueId());
        
        if(playerData.getPlayerState() == PlayerState.DUEL){
            Match match = MatchManager.getPlayerMatch(playerData);
            
            if(match != null){
                match.onDeath(playerData);
                
                Map map = match.getActiveMap().getMap();
                
                if(match.getTeamOne().getPlayers().contains(playerData))
                    event.setRespawnLocation(new Location(match.getActiveMap().getWorld(), map.getPlayerOneX(), map.getPlayerOneY(), map.getPlayerOneZ(), map.getPlayerOneYaw(), map.getPlayerOnePitch()));
                else if(match.getTeamTwo().getPlayers().contains(playerData))
                    event.setRespawnLocation(new Location(match.getActiveMap().getWorld(), map.getPlayerTwoX(), map.getPlayerTwoY(), map.getPlayerTwoZ(), map.getPlayerTwoYaw(), map.getPlayerTwoPitch()));
            }
        }
    }
}
