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
        //TODO FIX PING RANGE
        List<QueueEntry> unrankedEntries = QUEUE.stream()
                .filter(entry -> entry.getKit().equals(kit) && !entry.isRanked())
                .filter(entry -> QUEUE.stream()
                        .filter(entry1 -> entry1.getKit().equals(kit) && !entry1.isRanked())
                        .anyMatch(entry1 -> (entry.isWithinPingRange(entry1.getPlayerData())
                                && entry1.isWithinPingRange(entry.getPlayerData()))))
                .limit(2)
                .toList();
        List<QueueEntry> rankedEntries = QUEUE.stream()
                .filter(entry -> entry.getKit().equals(kit) && entry.isRanked())
                .filter(entry -> QUEUE.stream()
                        .filter(entry1 -> entry1.getKit().equals(kit) && entry1.isRanked())
                        .anyMatch(entry1 -> entry.isWithinPingRange(entry1.getPlayerData())
                                && entry1.isWithinPingRange(entry.getPlayerData())))
                .limit(2)
                .toList();
        
        if(unrankedEntries.size() >= 2){
            PlayerData playerData1 = unrankedEntries.get(0).getPlayerData(),
                    playerData2 = unrankedEntries.get(1).getPlayerData();
            
            unrankedEntries.get(0).cancelTask();
            unrankedEntries.get(1).cancelTask();
            
            QUEUE.remove(unrankedEntries.get(0));
            QUEUE.remove(unrankedEntries.get(1));
            
            MatchManager.startMatch(kit, false, playerData1, playerData2);
        }
        
        if(rankedEntries.size() >= 2){
            PlayerData playerData1 = rankedEntries.get(0).getPlayerData(),
                    playerData2 = rankedEntries.get(1).getPlayerData();
            
            rankedEntries.get(0).cancelTask();
            rankedEntries.get(1).cancelTask();
            
            QUEUE.remove(rankedEntries.get(0));
            QUEUE.remove(rankedEntries.get(1));
            
            MatchManager.startMatch(kit, true, playerData1, playerData2);
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
