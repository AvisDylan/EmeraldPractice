/**
 * Created by dylan on 6/24/2025
 */

package com.emeraldnetwork.emeraldPractice.utils;

import com.emeraldnetwork.emeraldPractice.file.FileManager;
import com.emeraldnetwork.emeraldPractice.player.PlayerData;
import com.emeraldnetwork.emeraldPractice.player.PlayerManager;
import org.bukkit.entity.Player;

import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

public class StatResetUtils{
    
    public static final Queue<UUID> PLAYERS_TO_RESET = new ConcurrentLinkedQueue<>();
    
    public static void resetStats(Player player){
        PlayerData playerData = PlayerManager.getPlayerData(player.getUniqueId());
        
        FileManager.saveProfile(playerData.getProfile());
        playerData.resetProfile();
        player.kickPlayer("stat reset");
    }
    
    public static void resetOfflinePlayer(UUID uuid){
        PLAYERS_TO_RESET.add(uuid);
    }
}
