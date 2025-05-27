package com.emeraldnetwork.emeraldPractice.commands;

import com.emeraldnetwork.emeraldPractice.player.PlayerManager;
import com.emeraldnetwork.emeraldPractice.profile.PlayerProfile;
import com.emeraldnetwork.emeraldPractice.utils.SpawnPointUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnPointCommand implements CommandExecutor{
    
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings){
        if(commandSender.hasPermission("emerald.practice")){
            SpawnPointUtils.setSpawnPoint(((Player) commandSender).getLocation());
            
            commandSender.sendMessage("§aSet spawn point to " + ((Player) commandSender).getLocation() + "!");
        }else
            commandSender.sendMessage("§cYou don't have permission to run this command!");
        return true;
    }
}
