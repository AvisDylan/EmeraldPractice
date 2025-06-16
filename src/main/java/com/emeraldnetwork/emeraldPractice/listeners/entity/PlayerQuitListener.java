package com.emeraldnetwork.emeraldPractice.listeners.entity;

import com.emeraldnetwork.emeraldPractice.match.Match;
import com.emeraldnetwork.emeraldPractice.match.MatchManager;
import com.emeraldnetwork.emeraldPractice.player.PlayerData;
import com.emeraldnetwork.emeraldPractice.player.PlayerManager;
import com.emeraldnetwork.emeraldPractice.player.PlayerState;
import com.emeraldnetwork.emeraldPractice.queue.QueueEntry;
import com.emeraldnetwork.emeraldPractice.queue.QueueManager;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener{

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        PlayerData playerData = PlayerManager.getPlayerData(event.getPlayer().getUniqueId());
        
        if(playerData.getFastBoard() != null)
            playerData.getFastBoard().delete();
        
        if(playerData.getPlayerState() == PlayerState.DUEL){
            Match match = MatchManager.getPlayerMatch(playerData);
            
            if(match != null)
                match.onLeave(playerData);
        }else if(playerData.getPlayerState() == PlayerState.QUEUE){
            QueueEntry queueEntry = QueueManager.getQueueEntry(playerData);
            
            if(queueEntry != null)
                QueueManager.leaveQueue(Bukkit.getPlayer(playerData.getUuid()));
        }
        
        PlayerManager.removePlayer(event.getPlayer().getUniqueId());
    }
}
