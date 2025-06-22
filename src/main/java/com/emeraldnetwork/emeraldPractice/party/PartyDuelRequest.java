/**
 * Created by dylan on 6/10/2025
 */

package com.emeraldnetwork.emeraldPractice.party;

import com.emeraldnetwork.emeraldPractice.EmeraldPractice;
import com.emeraldnetwork.emeraldPractice.kit.Kit;
import com.emeraldnetwork.emeraldPractice.map.Map;
import com.emeraldnetwork.emeraldPractice.player.PlayerData;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class PartyDuelRequest{
    
    private Party sender, receiver;
    private Kit kit;
    private Map map;
    
    public PartyDuelRequest(Kit kit, Party receiver, Party sender){
        this.kit = kit;
        this.receiver = receiver;
        this.sender = sender;
    }
    
    public PartyDuelRequest(Map map, Kit kit, Party receiverParty, Party senderParty){
        this.map = map;
        this.kit = kit;
        this.receiver = receiverParty;
        this.sender = senderParty;
        
        PlayerData sender = senderParty.getPartyLeader();
        PlayerData receiver = receiverParty.getPartyLeader();
        Player receiverPlayer = Bukkit.getPlayer(receiver.getUuid());
        Player senderPlayer = Bukkit.getPlayer(sender.getUuid());
        
        if(receiver.getProfile().isDuelSounds())
            receiverPlayer.playSound(receiverPlayer.getLocation(), Sound.NOTE_PLING, 1.0f, 1.0f);
        
        receiverPlayer.sendMessage(ChatColor.RESET + "");
        receiverPlayer.sendMessage(ChatColor.RESET + "" + ChatColor.DARK_GREEN + ChatColor.BOLD + "Duel Request");
        receiverPlayer.sendMessage(ChatColor.RESET + "" + ChatColor.GRAY + "From: " + ChatColor.DARK_GREEN + senderPlayer.getName() + ChatColor.GRAY + "'s party");
        receiverPlayer.sendMessage(ChatColor.RESET + "" + ChatColor.GRAY + "Kit: " + ChatColor.DARK_GREEN + kit.getDisplayName());
        receiverPlayer.sendMessage(ChatColor.RESET + "" + ChatColor.GRAY + "Map: " + ChatColor.DARK_GREEN + map.getDisplayName());
        receiverPlayer.sendMessage(ChatColor.RESET + "");
        
        TextComponent acceptComponent = new TextComponent(ChatColor.RESET + "" + ChatColor.DARK_GREEN + ChatColor.BOLD + "(Accept)");
        
        acceptComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/party duel accept " + senderPlayer.getName()));
        acceptComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{ new TextComponent(ChatColor.GRAY + "Click to accept") }));
        receiverPlayer.spigot().sendMessage(acceptComponent);
        receiverPlayer.sendMessage(ChatColor.RESET + "");
        
        Bukkit.getScheduler().runTaskLater(EmeraldPractice.getPlugin(), () -> {
            if(this.receiver.getDuelRequests().contains(this)){
                this.receiver.getDuelRequests().remove(this);
                
                Bukkit.getPlayer(sender.getUuid()).sendMessage(ChatColor.GRAY + "Duel request expired!");
            }
        }, 1200L);
    }
    
    public Party getSender(){
        return sender;
    }
    
    public void setSender(Party sender){
        this.sender = sender;
    }
    
    public Party getReceiver(){
        return receiver;
    }
    
    public void setReceiver(Party receiver){
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
