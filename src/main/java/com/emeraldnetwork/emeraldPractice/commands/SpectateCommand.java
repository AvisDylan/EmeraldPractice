/**
 * Created by dylan on 6/11/2025
 */

package com.emeraldnetwork.emeraldPractice.commands;

import com.emeraldnetwork.emeraldPractice.match.Match;
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

public class SpectateCommand implements CommandExecutor{
    
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings){
        if(strings.length < 1){
            commandSender.sendMessage(ChatColor.RED + " Invalid arguments!");
            return false;
        }
        
        Player target = Bukkit.getPlayer(strings[0]);
        Player sender = (Player) commandSender;
        
        if(target != null){
            PlayerData targetData = PlayerManager.getPlayerData(target.getUniqueId());
            PlayerData senderData = PlayerManager.getPlayerData(sender.getUniqueId());
            
            if(sender.equals(target)){
                commandSender.sendMessage(ChatColor.RED + "You can't spectate yourself!");
                return false;
            }
            
            if(senderData.getPlayerState() != PlayerState.SPAWN){
                commandSender.sendMessage(ChatColor.RED + "You are not at spawn!");
                return false;
            }
            
            Match match = MatchManager.getPlayerMatch(targetData);
            
            if(match != null){
                for(PlayerData playerData : match.getPlayers()){
                    if(!playerData.getProfile().isAllowSpectators()){
                        commandSender.sendMessage(ChatColor.RED + "Somebody in " + target.getName() + "'s game isn't allowing spectators!");
                        return false;
                    }
                    
                    Player player = Bukkit.getPlayer(playerData.getUuid());
                    
                    player.hidePlayer(sender);
                    player.sendMessage(ChatColor.DARK_GREEN + sender.getName() + ChatColor.GRAY + " has started spectating!");
                }
                
                senderData.setPlayerState(PlayerState.SPECTATING);
                PlayerManager.giveSpectatorItems(sender);
                sender.teleport(target);
                match.getSpectators().add(senderData);
                sender.setAllowFlight(true);
            }else
                commandSender.sendMessage(ChatColor.RED + target.getName() + " is not in game!");
        }else
            commandSender.sendMessage(ChatColor.RED + strings[0] + " is not a valid player!");
        return true;
    }
}
