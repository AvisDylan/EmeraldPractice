package com.emeraldnetwork.emeraldPractice.queue;

import com.emeraldnetwork.emeraldPractice.kit.Kit;
import com.emeraldnetwork.emeraldPractice.match.MatchManager;
import com.emeraldnetwork.emeraldPractice.player.PlayerData;
import com.emeraldnetwork.emeraldPractice.player.PlayerManager;
import com.emeraldnetwork.emeraldPractice.player.PlayerState;
import com.emeraldnetwork.emeraldPractice.utils.MultithreadedUtils;
import org.bukkit.entity.Player;

import java.util.concurrent.ConcurrentLinkedQueue;

public class QueueManager{

    public static final ConcurrentLinkedQueue<QueueEntry> QUEUE = new ConcurrentLinkedQueue<>();
    
    public static void joinQueue(Player player, Kit kit, boolean ranked){
        PlayerData playerData = PlayerManager.getPlayerData(player);
        
        playerData.setPlayerState(PlayerState.QUEUE);
        QUEUE.offer(new QueueEntry(playerData, kit, ranked));
        PlayerManager.giveQueueItems(player);
    }
    
    public static void leaveQueue(Player player){
        PlayerData playerData = PlayerManager.getPlayerData(player);
        
        for(QueueEntry queueEntry : QUEUE){
            if(queueEntry.getPlayerData().equals(playerData)){
                playerData.setPlayerState(PlayerState.SPAWN);
                QUEUE.remove(queueEntry);
                break;
            }
        }
    }
    
    public static void matchPlayers(Kit kit){
        QueueEntry entry1 = QUEUE.stream().filter(entry -> entry.getKit().equals(kit)).findFirst().orElse(null);
        QueueEntry entry2 = QUEUE.stream().filter(entry -> entry.getKit().equals(kit) && !entry.equals(entry1)).findFirst().orElse(null);
        
        if(entry1 != null && entry2 != null){
            QUEUE.remove(entry1);
            QUEUE.remove(entry2);
            MatchManager.startMatch(entry1.getPlayerData(), entry2.getPlayerData(), kit);
        }
    }
    
    public static void handleQueue(Kit kit){
        MultithreadedUtils.EXECUTOR_SERVICE.submit(() -> {
            long playersInKitQueue = QUEUE.stream().filter(entry -> entry.getKit().equals(kit)).count();
            
            if(playersInKitQueue >= 2)
                matchPlayers(kit);
        });
    }
}
