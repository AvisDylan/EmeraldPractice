/**
 * Created by dylan on 6/11/2025
 */

package com.emeraldnetwork.emeraldPractice.party;

import com.emeraldnetwork.emeraldPractice.duel.DuelRequest;
import com.emeraldnetwork.emeraldPractice.player.PlayerData;
import com.emeraldnetwork.emeraldPractice.player.PlayerManager;
import com.emeraldnetwork.emeraldPractice.player.PlayerState;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.List;

public class Party{
    
    private PlayerData partyLeader;
    private PartyDuelRequest tempPartyDuelRequest;
    private final List<PlayerData> players = new LinkedList<>();
    private boolean priv = true;
    private final List<PartyDuelRequest> duelRequests = new LinkedList<>();
    
    public Party(PlayerData partyLeader){
        this.partyLeader = partyLeader;
        
        PartyManager.PARTIES.add(this);
    }
    
    public void joinParty(PlayerData playerData){
        players.forEach(playerData1 -> {
            Player player = Bukkit.getPlayer(playerData1.getUuid());
            
            player.sendMessage(ChatColor.DARK_GREEN + Bukkit.getPlayer(playerData.getUuid()).getName() + ChatColor.GRAY + " has joined the party!");
        });
        
        Player playerJoining = Bukkit.getPlayer(playerData.getUuid());
        
        players.add(playerData);
        PlayerManager.giveSpawnItems(playerJoining); //if you are give spawn items whilst in a party it will give the party items
    }
    
    public void leaveParty(PlayerData playerData){
        if(players.size() <= 1)
            PartyManager.PARTIES.remove(this);
        
        if(playerData.equals(partyLeader))
            setPartyLeader(players.get(0));
        
        players.remove(playerData);
        
        Player player = Bukkit.getPlayer(playerData.getUuid());
        
        PlayerManager.giveSpawnItems(player);
        
        players.forEach(playerData1 -> {
            Player player1 = Bukkit.getPlayer(playerData1.getUuid());
            
            player1.sendMessage(ChatColor.DARK_GREEN + player.getName() + ChatColor.GRAY + " has left the party!");
        });
    }
    
    public void disband(){
        players.forEach(playerData -> {
            Player player = Bukkit.getPlayer(playerData.getUuid());
            
            players.remove(playerData);
            player.sendMessage(ChatColor.DARK_GREEN + Bukkit.getPlayer(partyLeader.getUuid()).getName() + ChatColor.GRAY + " has disbanded the party!");
            PlayerManager.giveSpawnItems(player);
        });
        
        PartyManager.PARTIES.remove(this);
    }
    
    public void sendPartyMessage(PlayerData senderData, String message){
        Player sender = Bukkit.getPlayer(senderData.getUuid());
        
        players.forEach(playerData -> {
            Player player = Bukkit.getPlayer(playerData.getUuid());
            
            player.playSound(player.getLocation(), Sound.NOTE_PLING, 1.0f, 1.0f);
            player.sendMessage(ChatColor.GRAY + "[" + ChatColor.DARK_GREEN + "Party" + ChatColor.GRAY + "] " + sender.getName() + ": " + message);
        });
    }
    
    public void invite(PlayerData playerData){
        //TODO ADD INVITES
    }
    
    public void announceParty(PlayerData playerData){
        Player sender = Bukkit.getPlayer(playerData.getUuid());
        
        for(PlayerData playerData1 : PlayerManager.PLAYERS.values()){
            if(playerData.getPlayerState() != PlayerState.SPAWN)
                continue;
            
            Player player = Bukkit.getPlayer(playerData1.getUuid());
            TextComponent base = new TextComponent(ChatColor.DARK_GREEN + sender.getName() + ChatColor.GRAY + " wants you to join their party! ");
            TextComponent joinComponent = new TextComponent(ChatColor.RESET + "" + ChatColor.DARK_GREEN + ChatColor.BOLD + "(Join)");
            
            joinComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/party join " + sender.getName()));
            joinComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{ new TextComponent(ChatColor.GRAY + "Click to join") }));
            base.addExtra(joinComponent);
            
            player.spigot().sendMessage(base);
        }
    }
    
    public PartyDuelRequest getTempPartyDuelRequest(){
        return tempPartyDuelRequest;
    }
    
    public void setTempPartyDuelRequest(PartyDuelRequest tempPartyDuelRequest){
        this.tempPartyDuelRequest = tempPartyDuelRequest;
    }
    
    public List<PartyDuelRequest> getDuelRequests(){
        return duelRequests;
    }
    
    public PlayerData getPartyLeader(){
        return partyLeader;
    }
    
    public void setPartyLeader(PlayerData partyLeader){
        this.partyLeader = partyLeader;
    }
    
    public List<PlayerData> getPlayers(){
        return players;
    }
    
    public boolean isPriv(){
        return priv;
    }
    
    public void setPriv(boolean priv){
        this.priv = priv;
    }
}
