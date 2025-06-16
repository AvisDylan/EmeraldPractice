/**
 * Created by dylan on 6/11/2025
 */

package com.emeraldnetwork.emeraldPractice.party;

import com.emeraldnetwork.emeraldPractice.player.PlayerData;
import com.emeraldnetwork.emeraldPractice.player.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.Queue;

public class Party{
    
    private PlayerData partyLeader;
    private final Queue<PlayerData> players = new LinkedList<>();
    private boolean priv = true;
    
    public Party(PlayerData partyLeader){
        this.partyLeader = partyLeader;
    }
    
    public void joinParty(PlayerData playerData){
        players.forEach(playerData1 -> {
            Player player = Bukkit.getPlayer(playerData1.getUuid());
            
            player.sendMessage(ChatColor.DARK_GREEN + Bukkit.getPlayer(playerData.getUuid()).getName() + ChatColor.GRAY + " has joined the party!");
        });
        
        Player playerJoining = Bukkit.getPlayer(playerData.getUuid());
        
        players.offer(playerData);
        PlayerManager.giveSpawnItems(playerJoining); //if you are give spawn items whilst in a party it will give the party items
    }
    
    public void leaveParty(PlayerData playerData){
        players.poll();
        
        if(players.isEmpty()){
            disband();
            return;
        }
        
        if(playerData.equals(partyLeader))
            setPartyLeader(players.peek());
        
        players.forEach(playerData1 -> {
            Player player = Bukkit.getPlayer(playerData1.getUuid());
            
            player.sendMessage(ChatColor.DARK_GREEN + Bukkit.getPlayer(playerData.getUuid()).getName() + ChatColor.GRAY + " has left the party!");
        });
        
        Player player = Bukkit.getPlayer(playerData.getUuid());
        
        PlayerManager.giveSpawnItems(player);
    }
    
    public void disband(){
        players.forEach(playerData1 -> {
            Player player = Bukkit.getPlayer(playerData1.getUuid());
            
            player.sendMessage(ChatColor.DARK_GREEN + Bukkit.getPlayer(partyLeader.getUuid()).getName() + ChatColor.GRAY + " has disbanded the party!");
            PlayerManager.giveSpawnItems(player);
        });
    }
    
    public void sendPartyMessage(PlayerData senderData, String message){
        Player sender = Bukkit.getPlayer(senderData.getUuid());
        
        players.forEach(playerData -> {
            Player player = Bukkit.getPlayer(playerData.getUuid());
            
            player.sendMessage(ChatColor.GRAY + "[" + ChatColor.DARK_GREEN + "Party" + ChatColor.GRAY + "] " + sender.getName() + ": " + message);
            PlayerManager.giveSpawnItems(player);
        });
    }
    
    public void invite(PlayerData playerData){
        //TODO ADD INVITES
    }
    
    public void announceParty(PlayerData playerData){
        //TODO ADD ANNOUNCING
    }
    
    public PlayerData getPartyLeader(){
        return partyLeader;
    }
    
    public void setPartyLeader(PlayerData partyLeader){
        this.partyLeader = partyLeader;
    }
    
    public Queue<PlayerData> getPlayers(){
        return players;
    }
    
    public boolean isPriv(){
        return priv;
    }
    
    public void setPriv(boolean priv){
        this.priv = priv;
    }
}
