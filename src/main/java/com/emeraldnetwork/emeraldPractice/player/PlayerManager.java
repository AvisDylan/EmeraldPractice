package com.emeraldnetwork.emeraldPractice.player;

import com.emeraldnetwork.emeraldPractice.file.FileManager;
import com.emeraldnetwork.emeraldPractice.profile.PlayerProfile;
import com.emeraldnetwork.emeraldPractice.utils.ItemUtils;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerManager{
    
    private static final Map<UUID, PlayerData> PLAYERS = new HashMap<>();
    
    public static void addPlayer(Player player){
        PlayerData playerData = new PlayerData(player);
        
        PLAYERS.put(player.getUniqueId(), playerData);
        playerData.setPlayerState(PlayerState.SPAWN);
    }
    
    public static void removePlayer(Player player){
        PLAYERS.remove(player.getUniqueId());
    }
    
    public static PlayerData getPlayerData(Player player){
        return PLAYERS.get(player.getUniqueId());
    }
    
    public static void giveSpawnItems(Player player){
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        player.getInventory().setContents(ItemUtils.SPAWN_ITEMS);
        player.updateInventory();
    }
    
    public static void giveSpectatorItems(Player player){
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        player.getInventory().setContents(ItemUtils.SPEC_ITEMS);
        player.updateInventory();
    }
    
    public static void giveQueueItems(Player player){
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        player.getInventory().setContents(ItemUtils.QUEUE_ITEMS);
        player.updateInventory();
    }
}
