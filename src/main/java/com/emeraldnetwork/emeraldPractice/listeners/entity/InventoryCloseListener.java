/**
 * Created by dylan on 6/19/2025
 */

package com.emeraldnetwork.emeraldPractice.listeners.entity;

import com.emeraldnetwork.emeraldPractice.kit.Kit;
import com.emeraldnetwork.emeraldPractice.kit.KitManager;
import com.emeraldnetwork.emeraldPractice.player.PlayerData;
import com.emeraldnetwork.emeraldPractice.player.PlayerManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class InventoryCloseListener implements Listener{
    
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event){
        PlayerData playerData = PlayerManager.getPlayerData(event.getPlayer().getUniqueId());
        
        switch(playerData.getPlayerState()){
            case SPAWN -> {
                if(event.getInventory().getTitle() == null || event.getInventory().getTitle().isEmpty())
                    return;
                
                if(event.getInventory().getTitle().contains("Edit ")){
                    String kitName = ChatColor.stripColor(event.getInventory().getTitle().substring(7));
                    Kit kit = KitManager.getKit(kitName);
                    
                    playerData.getProfile().getKitProfile(kit).setKitLayoutItems(event.getPlayer().getInventory().getContents());
                    PlayerManager.giveSpawnItems((Player) event.getPlayer());
                    event.getPlayer().sendMessage(ChatColor.GRAY + "Saved your layout for " + ChatColor.DARK_GREEN + kit.getDisplayName() + ChatColor.GRAY + "!");
                }
            }
        }
    }
}
