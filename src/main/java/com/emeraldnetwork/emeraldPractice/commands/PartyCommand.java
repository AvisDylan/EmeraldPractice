package com.emeraldnetwork.emeraldPractice.commands;

import com.emeraldnetwork.emeraldPractice.party.Party;
import com.emeraldnetwork.emeraldPractice.party.PartyManager;
import com.emeraldnetwork.emeraldPractice.player.PlayerData;
import com.emeraldnetwork.emeraldPractice.player.PlayerManager;
import com.emeraldnetwork.emeraldPractice.player.PlayerState;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PartyCommand implements CommandExecutor{
    
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings){
        PlayerData playerData = PlayerManager.getPlayerData(((Player) commandSender).getUniqueId());
        Party party = PartyManager.getPlayerParty(playerData);
        
        if(strings.length < 1){
            commandSender.sendMessage(ChatColor.RED + " Invalid arguments!");
            return false;
        }
        
        if(playerData.getPlayerState() != PlayerState.SPAWN){
            commandSender.sendMessage(ChatColor.RED + "You can't run this command in your state!");
            return false;
        }
        
        switch(strings[0].toLowerCase()){
            case "create" -> {
                if(party != null){
                    commandSender.sendMessage(ChatColor.RED + "You already are in party!");
                    return false;
                }
                
                commandSender.sendMessage(ChatColor.GRAY + "You have created a new party!");
                Party newParty = new Party(playerData);
                newParty.joinParty(playerData);
            }
            case "disband" -> {
                if(party == null){
                    commandSender.sendMessage(ChatColor.RED + "You are not in a party!");
                    return false;
                }
                
                if(!party.getPartyLeader().equals(playerData)){
                    commandSender.sendMessage(ChatColor.RED + "You aren't the party leader!");
                    return false;
                }
                
                commandSender.sendMessage(ChatColor.GRAY + "You have disbanded the party!");
                party.disband();
            }
            case "leave" -> {
                if(party == null){
                    commandSender.sendMessage(ChatColor.RED + "You are not in a party!");
                    return false;
                }
                
                commandSender.sendMessage(ChatColor.GRAY + "You have left the party!");
                party.leaveParty(playerData);
            }
            case "join" -> {
                if(strings.length < 2){
                    commandSender.sendMessage(ChatColor.RED + " Invalid arguments!");
                    return false;
                }
                
                if(party != null){
                    commandSender.sendMessage(ChatColor.RED + "You already are in party!");
                    return false;
                }
                
                Player target = Bukkit.getPlayer(strings[1]);
                
                if(target == null){
                    commandSender.sendMessage(ChatColor.RED + strings[1] + " is not a valid player!");
                    return false;
                }
                
                PlayerData targetData = PlayerManager.getPlayerData(target.getUniqueId());
                Party targetParty = PartyManager.getPlayerParty(targetData);
                
                if(targetData.equals(playerData)){
                    commandSender.sendMessage(ChatColor.RED + "You can't join your own party!");
                    return false;
                }
                
                if(targetData.getPlayerState() != PlayerState.SPAWN){
                    commandSender.sendMessage(ChatColor.RED + target.getName() + "'s party is not at spawn!");
                    return false;
                }
                
                if(targetParty == null){
                    commandSender.sendMessage(ChatColor.RED + target.getName() + " is not in a party!");
                    return false;
                }
                
                if(targetParty.isPriv()){
                    commandSender.sendMessage(ChatColor.RED + target.getName() + "'s party is private!");
                    return false;
                }
                
                commandSender.sendMessage(ChatColor.GRAY + "You have joined " + ChatColor.DARK_GREEN + target.getName() + ChatColor.GRAY + "'s party!");
                targetParty.joinParty(playerData);
            }
            case "duel" -> {
                //TODO ADD PARTY DUELS
            }
            case "promote" -> {
                if(strings.length < 2){
                    commandSender.sendMessage(ChatColor.RED + " Invalid arguments!");
                    return false;
                }
                
                if(party == null){
                    commandSender.sendMessage(ChatColor.RED + "You are not in a party!");
                    return false;
                }
                
                if(!party.getPartyLeader().equals(playerData)){
                    commandSender.sendMessage(ChatColor.RED + "You are not the party leader!");
                    return false;
                }
                
                Player target = Bukkit.getPlayer(strings[1]);
                
                if(target == null){
                    commandSender.sendMessage(ChatColor.RED + strings[1] + " is not a valid player!");
                    return false;
                }
                
                PlayerData targetData = PlayerManager.getPlayerData(target.getUniqueId());
                
                if(targetData.equals(playerData)){
                    commandSender.sendMessage(ChatColor.RED + "You can't promote yourself!");
                    return false;
                }
                
                commandSender.sendMessage(ChatColor.GRAY + "You have promoted " + ChatColor.DARK_GREEN + target.getName() + ChatColor.GRAY + " to the party leader!");
                party.setPartyLeader(targetData);
            }
            case "chat" -> {
                if(strings.length < 2){
                    commandSender.sendMessage(ChatColor.RED + " Invalid arguments!");
                    return false;
                }
                
                if(party == null){
                    commandSender.sendMessage(ChatColor.RED + "You are not in a party!");
                    return false;
                }
                
                StringBuilder builder = new StringBuilder();
                
                for(int i = 1; i < strings.length - 1; i++){
                    builder.append(strings[i]);
                }
                
                party.sendPartyMessage(playerData, builder.toString());
            }
            case "invite" -> {
                if(strings.length < 2){
                    commandSender.sendMessage(ChatColor.RED + " Invalid arguments!");
                    return false;
                }
                
                if(party == null){
                    commandSender.sendMessage(ChatColor.RED + "You are not in a party!");
                    return false;
                }
                
                if(!party.getPartyLeader().equals(playerData)){
                    commandSender.sendMessage(ChatColor.RED + "You are not the party leader!");
                    return false;
                }
                
                Player target = Bukkit.getPlayer(strings[1]);
                
                if(target == null){
                    commandSender.sendMessage(ChatColor.RED + strings[1] + " is not a valid player!");
                    return false;
                }
                
                PlayerData targetData = PlayerManager.getPlayerData(target.getUniqueId());
                Party targetParty = PartyManager.getPlayerParty(playerData);
                
                if(targetData.equals(playerData)){
                    commandSender.sendMessage(ChatColor.RED + "You can't invite yourself!");
                    return false;
                }
                
                if(targetData.getPlayerState() != PlayerState.SPAWN){
                    commandSender.sendMessage(ChatColor.RED + target.getName() + " is not at spawn!");
                    return false;
                }
                
                if(targetParty != null){
                    commandSender.sendMessage(ChatColor.RED + target.getName() + " is already in a party!");
                    return false;
                }
                
                commandSender.sendMessage(ChatColor.GRAY + "You have invited " + ChatColor.DARK_GREEN + target.getName() + ChatColor.GRAY + " to the party leader!");
                party.setPartyLeader(targetData);
            }
            case "announce" -> {
                if(party == null){
                    commandSender.sendMessage(ChatColor.RED + "You are not in a party!");
                    return false;
                }
                
                if(!party.getPartyLeader().equals(playerData)){
                    commandSender.sendMessage(ChatColor.RED + "You are not the party leader!");
                    return false;
                }
                
                if(party.isPriv()){
                    commandSender.sendMessage(ChatColor.RED + "The party is private!");
                    return false;
                }
                
                commandSender.sendMessage(ChatColor.GRAY + "You have announced the party!");
                party.announceParty(playerData);
            }
            case "public" -> {
                if(party == null){
                    commandSender.sendMessage(ChatColor.RED + "You are not in a party!");
                    return false;
                }
                
                if(!party.getPartyLeader().equals(playerData)){
                    commandSender.sendMessage(ChatColor.RED + "You are not the party leader!");
                    return false;
                }
                
                if(!party.isPriv()){
                    commandSender.sendMessage(ChatColor.RED + "The party is already public!");
                    return false;
                }
                
                commandSender.sendMessage(ChatColor.GRAY + "You have made the party public!");
                party.setPriv(false);
            }
            case "private" -> {
                if(party == null){
                    commandSender.sendMessage(ChatColor.RED + "You are not in a party!");
                    return false;
                }
                
                if(!party.getPartyLeader().equals(playerData)){
                    commandSender.sendMessage(ChatColor.RED + "You are not the party leader!");
                    return false;
                }
                
                if(party.isPriv()){
                    commandSender.sendMessage(ChatColor.RED + "The party is already private!");
                    return false;
                }
                
                commandSender.sendMessage(ChatColor.GRAY + "You have made the party private!");
                party.setPriv(true);
            }
            default -> commandSender.sendMessage(ChatColor.RED + strings[0] + " is not a valid option!");
        }
        
        return true;
    }
}
