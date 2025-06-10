package com.emeraldnetwork.emeraldPractice.commands;

import com.emeraldnetwork.emeraldPractice.file.FileManager;
import com.emeraldnetwork.emeraldPractice.player.PlayerData;
import com.emeraldnetwork.emeraldPractice.player.PlayerManager;
import com.emeraldnetwork.emeraldPractice.profile.PlayerProfile;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ResetStatsCommand implements CommandExecutor{
    
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings){
        if(commandSender.hasPermission("emerald.practice")){
            if(strings[0].equalsIgnoreCase("*"))
                PlayerManager.PLAYERS.values().forEach(PlayerData::resetProfile);
            else{
                for(PlayerData playerData : PlayerManager.PLAYERS.values()){
                    Player player = Bukkit.getPlayer(playerData.getUuid());
                    
                    if(player.getName().equalsIgnoreCase(strings[0])){
                        commandSender.sendMessage(ChatColor.GREEN + "Reset " + player.getName() + "'s stats!");
                        FileManager.saveProfile(playerData.getProfile());
                        playerData.resetProfile();
                        return true;
                    }
                }
                
                commandSender.sendMessage(ChatColor.RED + strings[0] + " is not a valid player!");
            }
        }else
            commandSender.sendMessage(ChatColor.RED + "You don't have permission to run this command!");
        return true;
    }
}
