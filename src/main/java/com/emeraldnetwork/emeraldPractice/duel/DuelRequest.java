/**
 * Created by dylan on 6/10/2025
 */

package com.emeraldnetwork.emeraldPractice.duel;

import com.emeraldnetwork.emeraldPractice.EmeraldPractice;
import com.emeraldnetwork.emeraldPractice.kit.Kit;
import com.emeraldnetwork.emeraldPractice.map.Map;
import com.emeraldnetwork.emeraldPractice.player.PlayerData;
import com.emeraldnetwork.emeraldPractice.player.PlayerManager;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class DuelRequest{
    
    private PlayerData sender, receiver;
    private Kit kit;
    private Map map;
    
    public DuelRequest(Kit kit, PlayerData receiver, PlayerData sender){
        this.kit = kit;
        this.receiver = receiver;
        this.sender = sender;
    }
    
    public DuelRequest(Map map, Kit kit, PlayerData receiver, PlayerData sender){
        this.map = map;
        this.kit = kit;
        this.receiver = receiver;
        this.sender = sender;
        
        Player receiverPlayer = Bukkit.getPlayer(receiver.getUuid());
        Player senderPlayer = Bukkit.getPlayer(sender.getUuid());
        
        if(receiver.getProfile().isMessageSounds())
            receiverPlayer.playSound(receiverPlayer.getLocation(), Sound.NOTE_PLING, 1.0f, 1.0f);
        
        receiverPlayer.sendMessage(ChatColor.RESET + "");
        receiverPlayer.sendMessage(ChatColor.RESET + "" + ChatColor.DARK_GREEN + ChatColor.BOLD + "Duel Request");
        receiverPlayer.sendMessage(ChatColor.RESET + "" + ChatColor.GRAY + "From: " + ChatColor.DARK_GREEN + senderPlayer.getName() + ChatColor.GRAY + " (" + sender.getPing() + " ms)");
        receiverPlayer.sendMessage(ChatColor.RESET + "" + ChatColor.GRAY + "Kit: " + ChatColor.DARK_GREEN + kit.getDisplayName());
        receiverPlayer.sendMessage(ChatColor.RESET + "" + ChatColor.GRAY + "Map: " + ChatColor.DARK_GREEN + map.getDisplayName());
        receiverPlayer.sendMessage(ChatColor.RESET + "");
        
        TextComponent acceptComponent = new TextComponent(ChatColor.RESET + "" + ChatColor.DARK_GREEN + ChatColor.BOLD + "(Accept)");
        
        acceptComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/accept " + senderPlayer.getName()));
        acceptComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{ new TextComponent(ChatColor.GRAY + "Click to accept") }));
        receiverPlayer.spigot().sendMessage(acceptComponent);
        receiverPlayer.sendMessage(ChatColor.RESET + "");
        
        Bukkit.getScheduler().runTaskLater(EmeraldPractice.getPlugin(), () -> {
            if(receiver.getDuelRequests().contains(this)){
                receiver.getDuelRequests().remove(this);
                
                Bukkit.getPlayer(sender.getUuid()).sendMessage(ChatColor.GRAY + "Duel request expired!");
            }
        }, 1200L);
    }
    
    public PlayerData getSender(){
        return sender;
    }
    
    public void setSender(PlayerData sender){
        this.sender = sender;
    }
    
    public PlayerData getReceiver(){
        return receiver;
    }
    
    public void setReceiver(PlayerData receiver){
        this.receiver = receiver;
    }
    
    public Kit getKit(){
        return kit;
    }
    
    public void setKit(Kit kit){
        this.kit = kit;
    }
    
    public Map getMap(){
        return map;
    }
    
    public void setMap(Map map){
        this.map = map;
    }
}
