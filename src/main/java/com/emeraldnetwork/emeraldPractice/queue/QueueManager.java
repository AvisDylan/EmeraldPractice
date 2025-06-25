package com.emeraldnetwork.emeraldPractice.queue;

import com.emeraldnetwork.emeraldPractice.kit.Kit;
import com.emeraldnetwork.emeraldPractice.match.MatchManager;
import com.emeraldnetwork.emeraldPractice.player.PlayerData;
import com.emeraldnetwork.emeraldPractice.player.PlayerManager;
import com.emeraldnetwork.emeraldPractice.player.PlayerState;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

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
                queueEntry.cancelTask();
                QUEUE.remove(queueEntry);
                
                return true;
            }
        }
        
        return false;
    }
    
    public static void handleQueue(Kit kit){
        List<QueueEntry> unrankedEntries = QUEUE.stream()
                .filter(entry -> entry.getKit().equals(kit) && !entry.isRanked())
                .toList();
        
        matchPlayers(unrankedEntries, kit, false);
        
        List<QueueEntry> rankedEntries = QUEUE.stream()
                .filter(entry -> entry.getKit().equals(kit) && entry.isRanked())
                .toList();
        
        matchPlayers(unrankedEntries, kit, true);
    }
    
    public static void matchPlayers(List<QueueEntry> entries, Kit kit, boolean ranked){
        for(int i = 0; i < entries.size(); i++){
            QueueEntry entry1 = entries.get(i);
            
            for(int j = i + 1; j < entries.size(); j++){
                QueueEntry entry2 = entries.get(j);
                PlayerData playerData1 = entry1.getPlayerData();
                PlayerData playerData2 = entry2.getPlayerData();
                
                if(entry1.isWithinPingRange(playerData2) && entry2.isWithinPingRange(playerData1)){
                    entry1.cancelTask();
                    entry2.cancelTask();
                    
                    QUEUE.remove(entry1);
                    QUEUE.remove(entry2);
                    
                    MatchManager.startMatch(kit, ranked, playerData1, playerData2);
                    return;
                }
            }
        }
    }
    
    public static QueueEntry getQueueEntry(PlayerData playerData){
        return QUEUE.stream().filter(queueEntry -> queueEntry.getPlayerData().equals(playerData)).findFirst().orElse(null);
    }
    
    public static int getPlaceInQueue(QueueEntry queueEntry){
        int placeInQueue = 0;
        ConcurrentLinkedQueue<QueueEntry> tempQueue = new ConcurrentLinkedQueue<>(QUEUE);
        
        for(int i = 0; i < tempQueue.size(); i++){
            QueueEntry queueEntry1 = tempQueue.poll();
            
            if(queueEntry1.equals(queueEntry)){
                placeInQueue = i;
                break;
            }
        }
        
        return placeInQueue + 1;
    }
    
    public static int getPlayersInKitQueue(Kit kit, boolean ranked){
        return Math.toIntExact(QUEUE.stream().filter(queueEntry -> queueEntry.getKit().equals(kit) && queueEntry.isRanked() == ranked).count());
    }
}
