/**
 * Created by dylan on 6/10/2025
 */

package com.emeraldnetwork.emeraldPractice.commands;

import com.emeraldnetwork.emeraldPractice.duel.DuelRequest;
import com.emeraldnetwork.emeraldPractice.match.MatchManager;
import com.emeraldnetwork.emeraldPractice.player.PlayerData;
import com.emeraldnetwork.emeraldPractice.player.PlayerManager;
import com.emeraldnetwork.emeraldPractice.player.PlayerState;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AcceptCommand implements CommandExecutor{
    
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings){
        if(strings.length < 1){
            commandSender.sendMessage(ChatColor.RED + " Invalid Arguments!");
            return false;
        }
        
        Player sender = Bukkit.getPlayer(strings[0]);
        Player receiver = (Player) commandSender;
        PlayerData receiverData = PlayerManager.getPlayerData(receiver.getUniqueId());
        
        if(sender == null){
            commandSender.sendMessage(ChatColor.RED + strings[0] + " is not a valid player!");
            return false;
        }
        
        PlayerData senderData = PlayerManager.getPlayerData(sender.getUniqueId());
        
        if(receiverData.getDuelRequest(senderData) != null){
            DuelRequest incomingRequest = receiverData.getDuelRequest(senderData);
            
            if(senderData.getPlayerState() != PlayerState.SPAWN){
                commandSender.sendMessage(ChatColor.RED + receiver.getName() + " is not at spawn!");
                return false;
            }
            
            if(receiverData.getPlayerState() != PlayerState.SPAWN){
                commandSender.sendMessage(ChatColor.RED + "You are not at spawn!");
                return false;
            }
            
            MatchManager.startMatch(incomingRequest.getKit(), false, incomingRequest.getMap(), senderData, receiverData);
            receiverData.getDuelRequests().remove(incomingRequest);
            receiver.sendMessage(ChatColor.GRAY + "You have accepted " + ChatColor.DARK_GREEN + sender.getName() + ChatColor.GRAY + "'s duel!");
        }else
            commandSender.sendMessage(ChatColor.RED + "You have no duel requests from " + sender.getName() + "!");
        return true;
    }
}
