package com.emeraldnetwork.emeraldPractice.listeners.entity;

import com.emeraldnetwork.emeraldPractice.EmeraldPractice;
import com.emeraldnetwork.emeraldPractice.match.Match;
import com.emeraldnetwork.emeraldPractice.match.MatchManager;
import com.emeraldnetwork.emeraldPractice.match.MatchState;
import com.emeraldnetwork.emeraldPractice.misc.PearlCooldown;
import com.emeraldnetwork.emeraldPractice.player.PlayerData;
import com.emeraldnetwork.emeraldPractice.player.PlayerManager;
import com.emeraldnetwork.emeraldPractice.utils.GuiUtils;
import com.emeraldnetwork.emeraldPractice.utils.ItemUtils;
import com.emeraldnetwork.emeraldPractice.utils.MultithreadedUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class PlayerInteractListener implements Listener{
    
    private final Map<PlayerData, PearlCooldown> playerPearlCooldowns = new HashMap<>();
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event){
        if(event.getPlayer().getItemInHand() == null || event.getItem() == null)
            return;
        
        PlayerData playerData = PlayerManager.getPlayerData(event.getPlayer().getUniqueId());
        
        switch(playerData.getPlayerState()){
            case SPAWN -> {
                if(event.getAction() == Action.RIGHT_CLICK_AIR && event.getItem().hasItemMeta()){
                    switch(event.getItem().getItemMeta().getDisplayName()){
                        case "§7Unranked Queue (right click)" -> event.getPlayer().chat("/queue gui unranked");
                        case "§2Ranked Queue §7(right click)" -> event.getPlayer().chat("/queue gui ranked");
                        case "§7FFA (right click)" -> event.getPlayer().chat("/ffa gui");
                        case "§7Bot Queue (right click)" -> event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Feature coming soon!")); //event.getPlayer().chat("/queuegui bot")
                        case "§7Create a Party (right click)" -> event.getPlayer().chat("/party create");
                        case "§7Kit Editor (right click)" -> event.getPlayer().chat("/kiteditor");
                        case "§7Tournament (right click)" -> event.getPlayer().sendMessage(ChatColor.GRAY + "Feature coming soon!"); //event.getPlayer().chat("/tournamentgui")
                        case "§7Leaderboards (right click)" -> event.getPlayer().chat("/leaderboards");
                        case "§7Settings (right click)" -> event.getPlayer().chat("/emeraldpractice:settings");
                        case "§cLeave Party §7(right click)" -> event.getPlayer().chat("/party leave");
                        case "§7Party List (right click)" -> event.getPlayer().chat("/party list");
                        case "§Start a Party Fight (right click)" -> event.getPlayer().chat("/party fight");
                    }
                }
            }
            case QUEUE -> {
                if(event.getAction() == Action.RIGHT_CLICK_AIR && event.getItem().hasItemMeta()){
                    if(event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("§cLeave Queue §7(right click)"))
                        event.getPlayer().chat("/queue leave");
                }
            }
            case SPECTATING -> {
                if(event.getAction() == Action.RIGHT_CLICK_AIR && event.getItem().hasItemMeta()){
                    if(event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.RED + "Stop Spectating " + ChatColor.GRAY + "(right click)"))
                        event.getPlayer().chat("/leave");
                    else if(event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GRAY + "Teleport to a Player (right click)")){
                        Inventory inventory = GuiUtils.createInventoryWithBorder(event.getPlayer(), 36, ChatColor.GRAY + "Choose a player");
                        Match match = MatchManager.getSpectatorMatch(playerData);
                        
                        if(match == null)
                            return;
                        
                        match.getPlayers().forEach(playerData1 -> {
                            Player player = Bukkit.getPlayer(playerData1.getUuid());
                            ItemStack playerHead = ItemUtils.createSkull(player, 1, ChatColor.DARK_GREEN + player.getName());
                            
                            inventory.addItem(playerHead);
                        });
                        
                        event.getPlayer().openInventory(inventory);
                    }
                }
            }
            case DUEL -> {
                Match match = MatchManager.getPlayerMatch(playerData);
                
                if(match != null && match.getMatchState() == MatchState.STARTING || match.getMatchState() == MatchState.ENDING){
                    event.setCancelled(true);
                    return;
                }
                
                if(event.getPlayer().getItemInHand().getType() == Material.ENDER_PEARL && (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)){
                    if(playerPearlCooldowns.containsKey(playerData)){
                        long secondsSinceLastPearlThrow = (System.currentTimeMillis() - playerPearlCooldowns.get(playerData).getLastPearl()) / 1000;
                        
                        if(secondsSinceLastPearlThrow < 10){
                            event.setCancelled(true);
                            event.getPlayer().sendMessage(ChatColor.RED + "You can pearl again in " + (10 - secondsSinceLastPearlThrow) + " seconds!");
                            return;
                        }
                        
                        Bukkit.getScheduler().cancelTask(playerPearlCooldowns.get(playerData).getSchedulerId());
                    }
                    
                    int schedulerId = Bukkit.getScheduler().runTaskTimerAsynchronously(EmeraldPractice.getPlugin(), () -> {
                        MultithreadedUtils.EXECUTOR_SERVICE.submit(() -> {
                            Player player = Bukkit.getPlayer(playerData.getUuid());
                            int countdown = Math.round(Math.max(0, 10 - (System.currentTimeMillis() - playerPearlCooldowns.get(playerData).getLastPearl()) / 1000));
                            
                            player.setLevel(countdown);
                            player.setExp(countdown / 10.0f);
                        });
                    }, 0L, 10L).getTaskId();
                    
                    playerPearlCooldowns.put(playerData, new PearlCooldown(System.currentTimeMillis(), schedulerId));
                }
            }
        }
    }
}
