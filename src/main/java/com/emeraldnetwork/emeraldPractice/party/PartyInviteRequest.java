/**
 * Created by dylan on 6/17/2025
 */

package com.emeraldnetwork.emeraldPractice.party;

import com.emeraldnetwork.emeraldPractice.EmeraldPractice;
import com.emeraldnetwork.emeraldPractice.player.PlayerData;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class PartyInviteRequest{
    
    private PlayerData sender;
    private PlayerData receiver;
    
    public PartyInviteRequest(PlayerData sender, PlayerData receiver){
        this.sender = sender;
        this.receiver = receiver;
        
        Player senderPlayer = Bukkit.getPlayer(sender.getUuid());
        Player receiverPlayer = Bukkit.getPlayer(receiver.getUuid());
        
        if(receiver.getProfile().isPartySounds())
            receiverPlayer.playSound(receiverPlayer.getLocation(), Sound.NOTE_PLING, 1.0f, 1.0f);
        
        TextComponent base = new TextComponent(ChatColor.RESET + "" + ChatColor.DARK_GREEN + receiverPlayer.getName() + ChatColor.GRAY + " has invited you to their party! ");
        TextComponent acceptComponent = new TextComponent(ChatColor.RESET + "" + ChatColor.DARK_GREEN + ChatColor.BOLD + "(Accept)");
        
        acceptComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/party accept " + senderPlayer.getName()));
        acceptComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{ new TextComponent(ChatColor.GRAY + "Click to accept") }));
        base.addExtra(acceptComponent);
        receiverPlayer.spigot().sendMessage(base);
        
        Bukkit.getScheduler().runTaskLater(EmeraldPractice.getPlugin(), () -> {
            if(this.receiver.getPartyRequests().contains(this)){
                this.receiver.getPartyRequests().remove(this);
                
                Bukkit.getPlayer(sender.getUuid()).sendMessage(ChatColor.GRAY + "Party request expired!");
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
}
