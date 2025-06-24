package com.emeraldnetwork.emeraldPractice.player;

import com.emeraldnetwork.emeraldPractice.database.DatabaseManager;
import com.emeraldnetwork.emeraldPractice.party.PartyManager;
import com.emeraldnetwork.emeraldPractice.utils.ItemUtils;
import com.emeraldnetwork.emeraldPractice.utils.StatResetUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class PlayerManager{
    
    public static final Map<UUID, PlayerData> PLAYERS = new HashMap<>();
    
    public static void addPlayer(Player player){
        PlayerData playerData = new PlayerData(player.getUniqueId());
        
        PLAYERS.put(player.getUniqueId(), playerData);
        playerData.setPlayerState(PlayerState.SPAWN);
        player.setPlayerWeather(playerData.getProfile().getPlayerWeather());
        player.setPlayerTime(playerData.getProfile().getPlayerTime(), false);
        
        if(StatResetUtils.PLAYERS_TO_RESET.contains(player.getUniqueId()))
            StatResetUtils.resetStats(player);
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
        PlayerData playerData = getPlayerData(player.getUniqueId());
        
        Bukkit.getLogger().info(PartyManager.getPlayerParty(playerData) + " ");
        
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        player.getInventory().setContents(PartyManager.getPlayerParty(playerData) != null ? ItemUtils.PARTY_ITEMS : ItemUtils.SPAWN_ITEMS);
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
