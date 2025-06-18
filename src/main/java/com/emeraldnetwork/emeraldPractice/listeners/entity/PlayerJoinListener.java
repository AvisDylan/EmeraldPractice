package com.emeraldnetwork.emeraldPractice.listeners.entity;

import com.emeraldnetwork.emeraldPractice.player.PlayerManager;
import com.emeraldnetwork.emeraldPractice.utils.SpawnPointUtils;
import fr.mrmicky.fastboard.FastBoard;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener{
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        PlayerManager.addPlayer(event.getPlayer());
        PlayerManager.giveSpawnItems(event.getPlayer());
        event.getPlayer().setPlayerTime(PlayerManager.getPlayerData(event.getPlayer().getUniqueId()).getProfile().getPlayerTime(), true);
        event.getPlayer().teleport(SpawnPointUtils.getSpawnPoint());
        
        event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.NOTE_PLING, 1.0f, 1.0f);
        event.getPlayer().sendMessage(ChatColor.RESET + "");
        event.getPlayer().sendMessage(ChatColor.RESET + "" + ChatColor.DARK_GREEN + ChatColor.BOLD + "Welcome to Emerald Practice!");
        event.getPlayer().sendMessage(ChatColor.RESET + "" + ChatColor.GRAY + "NOTE: the server is still in alpha, if you encounter any bugs please open a ticket on our website or discord!");
        event.getPlayer().sendMessage(ChatColor.RESET + "");
        event.getPlayer().sendMessage(ChatColor.RESET + "" + ChatColor.GRAY + "Our Website: " + ChatColor.DARK_GREEN + "https://www.emerald-network.com");
        event.getPlayer().sendMessage(ChatColor.RESET + "" + ChatColor.GRAY + "Our Store: " + ChatColor.DARK_GREEN + "https://www.emerald-network.com/store");
        event.getPlayer().sendMessage(ChatColor.RESET + "" + ChatColor.GRAY + "Our Discord: " + ChatColor.DARK_GREEN + "https://dsc.gg/emeraldnetwork");
        event.getPlayer().sendMessage(ChatColor.RESET + "");
    }
}
