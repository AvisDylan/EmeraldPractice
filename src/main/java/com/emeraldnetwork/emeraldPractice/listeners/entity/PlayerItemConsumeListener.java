package com.emeraldnetwork.emeraldPractice.listeners.entity;

import com.emeraldnetwork.emeraldPractice.EmeraldPractice;
import com.emeraldnetwork.emeraldPractice.match.Match;
import com.emeraldnetwork.emeraldPractice.match.MatchManager;
import com.emeraldnetwork.emeraldPractice.match.MatchState;
import com.emeraldnetwork.emeraldPractice.player.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PlayerItemConsumeListener implements Listener{
    
    @EventHandler
    public void onPlayerItemConsume(PlayerItemConsumeEvent event){
        Match match = MatchManager.getPlayerMatch(PlayerManager.getPlayerData(event.getPlayer().getUniqueId()));
        
        if(match == null)
            return;
        
        if(match.getMatchState() == MatchState.STARTING || match.getMatchState() == MatchState.ENDING){
            event.setCancelled(true);
            return;
        }
        
        if(event.getItem().hasItemMeta()
                && event.getItem().getItemMeta().hasDisplayName()
                && event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GOLD + "Golden Head")){
            event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 180, 1));
            event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 2400, 0));
        }
        
        if(event.getItem().getType() == Material.POTION)
            Bukkit.getScheduler().runTaskLater(EmeraldPractice.getPlugin(), () -> event.getPlayer().getInventory().removeItem(new ItemStack(Material.GLASS_BOTTLE, 1)), 1L);
    }
}
