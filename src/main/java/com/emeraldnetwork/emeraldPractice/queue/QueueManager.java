package com.emeraldnetwork.emeraldPractice.queue;

import com.emeraldnetwork.emeraldPractice.kit.Kit;
import com.emeraldnetwork.emeraldPractice.match.MatchManager;
import com.emeraldnetwork.emeraldPractice.player.PlayerData;
import com.emeraldnetwork.emeraldPractice.player.PlayerManager;
import com.emeraldnetwork.emeraldPractice.player.PlayerState;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class QueueManager{

    public static final ConcurrentLinkedQueue<QueueEntry> QUEUE = new ConcurrentLinkedQueue<>();
    
    public static void joinQueue(Player player, Kit kit, boolean ranked, int teamSize){
        PlayerData playerData = PlayerManager.getPlayerData(player.getUniqueId());
        
        playerData.setPlayerState(PlayerState.QUEUE);
        QUEUE.offer(new QueueEntry(playerData, kit, ranked, teamSize));
        PlayerManager.giveQueueItems(player);
    }
    
    public static boolean leaveQueue(Player player){
        PlayerData playerData = PlayerManager.getPlayerData(player.getUniqueId());
        
        for(QueueEntry queueEntry : QUEUE){
            if(queueEntry.getPlayerData().equals(playerData)){
                playerData.setPlayerState(PlayerState.SPAWN);
                QUEUE.remove(queueEntry);
                
                return true;
            }
        }
        
        return false;
    }
    
    public static void handleQueue(Kit kit){
        List<QueueEntry> unrankedEntries = QUEUE.stream().filter(entry -> entry.getKit().equals(kit) && !entry.isRanked()).limit(2).toList();
        List<QueueEntry> rankedEntries = QUEUE.stream().filter(entry -> entry.getKit().equals(kit) && entry.isRanked()).limit(2).toList();
        
        if(unrankedEntries.size() >= 2){
            PlayerData playerData1 = unrankedEntries.get(0).getPlayerData(),
                    playerData2 = unrankedEntries.get(1).getPlayerData();
            
            QUEUE.remove(unrankedEntries.get(0));
            QUEUE.remove(unrankedEntries.get(1));
            
            MatchManager.startMatch(kit, false, playerData1, playerData2);
        }
        
        if(rankedEntries.size() >= 2){
            PlayerData playerData1 = rankedEntries.get(0).getPlayerData(),
                    playerData2 = rankedEntries.get(1).getPlayerData();
            
            QUEUE.remove(rankedEntries.get(0));
            QUEUE.remove(rankedEntries.get(1));
            
            MatchManager.startMatch(kit, true, playerData1, playerData2);
        }
    }
}
