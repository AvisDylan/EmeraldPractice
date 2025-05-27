package com.emeraldnetwork.emeraldPractice.commands;

import com.emeraldnetwork.emeraldPractice.player.PlayerManager;
import com.emeraldnetwork.emeraldPractice.profile.PlayerProfile;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EditModeCommand implements CommandExecutor{
    
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings){
        if(commandSender.hasPermission("emerald.practice")){
            PlayerProfile playerProfile = PlayerManager.getPlayerData((Player) commandSender).getProfile();
            
            playerProfile.setEditMode(!playerProfile.isEditMode());
            commandSender.sendMessage(playerProfile.isEditMode() ? "§aYou have enabled edit mode!" : "§aYou have disabled edit mode!");
        }else
            commandSender.sendMessage("§cYou don't have permission to run this command!");
        return true;
    }
}
