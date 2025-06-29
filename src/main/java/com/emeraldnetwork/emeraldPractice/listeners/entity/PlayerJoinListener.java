package com.emeraldnetwork.emeraldPractice.listeners.entity;

import com.emeraldnetwork.emeraldPractice.player.PlayerManager;
import com.emeraldnetwork.emeraldPractice.utils.SpawnPointUtils;
import fr.mrmicky.fastboard.FastBoard;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;

public class PlayerJoinListener implements Listener{
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        
        PlayerManager.addPlayer(player);
        PlayerManager.giveSpawnItems(player);
        player.setPlayerTime(PlayerManager.getPlayerData(player.getUniqueId()).getProfile().getPlayerTime(), false);
        
        for(PotionEffect potionEffect : player.getActivePotionEffects()){
            player.removePotionEffect(potionEffect.getType());
        }
        
        player.setSaturation(20.0f);
        player.setLevel(0);
        player.setExp(0.0f);
        player.setFireTicks(0);
        player.setFoodLevel(20);
        player.setHealth(player.getMaxHealth());
        player.teleport(SpawnPointUtils.getSpawnPoint());
        
        player.playSound(player.getLocation(), Sound.NOTE_PLING, 1.0f, 1.0f);
        player.sendMessage(ChatColor.RESET + "");
        player.sendMessage(ChatColor.RESET + "" + ChatColor.DARK_GREEN + ChatColor.BOLD + "Welcome to Emerald Practice!");
        player.sendMessage(ChatColor.RESET + "" + ChatColor.GRAY + "NOTE: the server is still in alpha, if you encounter any bugs please open a ticket on our website or discord!");
        player.sendMessage(ChatColor.RESET + "");
        player.sendMessage(ChatColor.RESET + "" + ChatColor.GRAY + "Our Website: " + ChatColor.DARK_GREEN + "https://www.emerald-network.com");
        player.sendMessage(ChatColor.RESET + "" + ChatColor.GRAY + "Our Store: " + ChatColor.DARK_GREEN + "https://www.emerald-network.com/store");
        player.sendMessage(ChatColor.RESET + "" + ChatColor.GRAY + "Our Discord: " + ChatColor.DARK_GREEN + "https://dsc.gg/emeraldnetwork");
        player.sendMessage(ChatColor.RESET + "");
    }
}
