package com.emeraldnetwork.emeraldPractice.listeners.entity;

import com.emeraldnetwork.emeraldPractice.EmeraldPractice;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerItemConsumeListener implements Listener{
    
    @EventHandler
    public void onPlayerItemConsume(PlayerItemConsumeEvent event){
        if(event.getItem().getType() == Material.POTION){
            Bukkit.getScheduler().runTaskLater(EmeraldPractice.getPlugin(), () -> event.getPlayer().getInventory().removeItem(new ItemStack(Material.GLASS_BOTTLE, 1)), 1L);
        }
    }
}
