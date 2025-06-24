package com.emeraldnetwork.emeraldPractice.commands;

import com.emeraldnetwork.emeraldPractice.file.FileManager;
import com.emeraldnetwork.emeraldPractice.player.PlayerData;
import com.emeraldnetwork.emeraldPractice.player.PlayerManager;
import com.emeraldnetwork.emeraldPractice.profile.PlayerProfile;
import com.emeraldnetwork.emeraldPractice.utils.StatResetUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ResetStatsCommand implements CommandExecutor{
    
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings){
        if(commandSender.hasPermission("emerald.practice")){
            if(strings[0].equalsIgnoreCase("*"))
                for(OfflinePlayer player : Bukkit.getOfflinePlayers()){
                    if(player.isOnline())
                        StatResetUtils.resetStats((Player) player);
                    else
                        StatResetUtils.resetOfflinePlayer(player.getUniqueId());
                }
            else{
                OfflinePlayer player = Bukkit.getOfflinePlayer(strings[0]);
                
                if(player != null){
                    if(player.isOnline())
                        StatResetUtils.resetStats((Player) player);
                    else
                        StatResetUtils.resetOfflinePlayer(player.getUniqueId());
                }else
                    commandSender.sendMessage(ChatColor.RED + strings[0] + " is not a valid player!");
            }
        }else
            commandSender.sendMessage(ChatColor.RED + "You don't have permission to run this command!");
        return true;
    }
}
