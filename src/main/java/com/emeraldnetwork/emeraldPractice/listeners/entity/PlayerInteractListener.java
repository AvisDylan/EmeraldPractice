package com.emeraldnetwork.emeraldPractice.listeners.entity;

import com.emeraldnetwork.emeraldPractice.player.PlayerData;
import com.emeraldnetwork.emeraldPractice.player.PlayerManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractListener implements Listener{
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event){
        if(event.getPlayer().getItemInHand() == null || event.getAction() != Action.RIGHT_CLICK_AIR)
            return;
        
        PlayerData playerData = PlayerManager.getPlayerData(event.getPlayer());
        
        switch(playerData.getPlayerState()){
            case SPAWN -> {
                switch(event.getItem().getItemMeta().getDisplayName()){
                    case "§7Unranked Queue (right click)" -> event.getPlayer().chat("/queuegui unranked");
                    case "§2Ranked Queue §7(right click)" -> event.getPlayer().chat("/queuegui ranked");
                    case "§7FFA (right click)" -> event.getPlayer().chat("/ffagui");
                    case "§7Bot Queue (right click)" -> event.getPlayer().chat("/queuegui bot");
                    case "§7Create a party (right click)" -> event.getPlayer().chat("/partygui");
                    case "§7Kit editor (right click)" -> event.getPlayer().chat("/kiteditor");
                    case "§7Tournament (right click)" -> event.getPlayer().chat("/tournamentgui");
                    case "§7Leaderboards (right click)" -> event.getPlayer().chat("/leaderboards");
                    case "§7Settings (right click)" -> event.getPlayer().chat("/settings");
                }
            }
            case QUEUE -> {
                if(event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("§cLeave Queue §7(right click)"))
                    event.getPlayer().chat("/queue leave");
            }
        }
    }
}
