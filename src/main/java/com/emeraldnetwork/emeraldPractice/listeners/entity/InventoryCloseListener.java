/**
 * Created by dylan on 6/19/2025
 */

package com.emeraldnetwork.emeraldPractice.listeners.entity;

import com.emeraldnetwork.emeraldPractice.EmeraldPractice;
import com.emeraldnetwork.emeraldPractice.kit.Kit;
import com.emeraldnetwork.emeraldPractice.kit.KitManager;
import com.emeraldnetwork.emeraldPractice.player.PlayerData;
import com.emeraldnetwork.emeraldPractice.player.PlayerManager;
import com.emeraldnetwork.emeraldPractice.player.PlayerState;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.Objects;

public class InventoryCloseListener implements Listener{
    
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event){
        PlayerData playerData = PlayerManager.getPlayerData(event.getPlayer().getUniqueId());
        
        if(Objects.requireNonNull(playerData.getPlayerState()) == PlayerState.SPAWN){
            if(event.getInventory().getTitle() == null || event.getInventory().getTitle().isEmpty())
                return;
            
            if(event.getInventory().getTitle().contains("Edit ")){
                String kitName = ChatColor.stripColor(event.getInventory().getTitle().substring(7));
                Kit kit = KitManager.getKit(kitName);
                
                playerData.getProfile().getKitProfile(kit).setKitLayoutItems(event.getPlayer().getInventory().getContents());
                event.getPlayer().sendMessage(ChatColor.GRAY + "Saved your layout for " + ChatColor.DARK_GREEN + kit.getDisplayName() + ChatColor.GRAY + "!");
                Bukkit.getScheduler().runTaskLater(EmeraldPractice.getPlugin(), () -> {
                    PlayerManager.giveSpawnItems((Player) event.getPlayer());
                }, 1L);
            }
        }
    }
}
