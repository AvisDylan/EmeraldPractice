package com.emeraldnetwork.emeraldPractice.player;

import com.emeraldnetwork.emeraldPractice.database.DatabaseManager;
import com.emeraldnetwork.emeraldPractice.utils.ItemUtils;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerManager{
    
    public static final Map<UUID, PlayerData> PLAYERS = new HashMap<>();
    
    public static void addPlayer(Player player){
        PlayerData playerData = new PlayerData(player.getUniqueId());
        PLAYERS.put(player.getUniqueId(), playerData);
        playerData.setPlayerState(PlayerState.SPAWN);
    }
    
    public static void removePlayer(UUID uuid){
        PlayerData playerData = getPlayerData(uuid);
        
        DatabaseManager.savePlayerProfile(playerData.getProfile());
        playerData.getFastBoard().delete();
        PLAYERS.remove(uuid);
    }
    
    public static PlayerData getPlayerData(UUID uuid){
        return PLAYERS.get(uuid);
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
