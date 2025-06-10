package com.emeraldnetwork.emeraldPractice.commands;

import com.emeraldnetwork.emeraldPractice.kit.Kit;
import com.emeraldnetwork.emeraldPractice.kit.KitManager;
import com.emeraldnetwork.emeraldPractice.player.PlayerManager;
import com.emeraldnetwork.emeraldPractice.player.PlayerState;
import com.emeraldnetwork.emeraldPractice.queue.QueueEntry;
import com.emeraldnetwork.emeraldPractice.queue.QueueManager;
import com.emeraldnetwork.emeraldPractice.utils.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class QueueCommand implements CommandExecutor{
    
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings){
        Player player = (Player) commandSender;
        
        switch(strings[0].toLowerCase()){
            case "join" -> {
                if(!PlayerManager.getPlayerData(player.getUniqueId()).getPlayerState().equals(PlayerState.SPAWN)){
                    commandSender.sendMessage("§cYou can't run this command in your state!");
                    return false;
                }
                
                Kit selectedKit = null;
                
                for(Kit kit : KitManager.KITS){
                    if(kit.getName().equalsIgnoreCase(strings[2])){
                        selectedKit = kit;
                        break;
                    }
                }
                
                if(selectedKit == null){
                    commandSender.sendMessage("§c" + strings[2] + " is not a valid kit!");
                    return false;
                }
                
                if(!selectedKit.isEnabled()){
                    commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou cannot play " + selectedKit.getDisplayName() + "!"));
                    return false;
                }
                
                if(selectedKit.getMaps().isEmpty()){
                    commandSender.sendMessage(ChatColor.RED + selectedKit.getDisplayName() + " has no maps!");
                    return false;
                }
                
                switch(strings[1].toLowerCase()){
                    case "unranked" -> {
                        QueueManager.joinQueue(player, selectedKit, false, 1);
                        commandSender.sendMessage(ChatColor.GRAY + "You have joined the unranked queue for " + ChatColor.DARK_GREEN + selectedKit.getDisplayName() + ChatColor.GRAY + "!");
                    }
                    case "ranked" -> {
                        if(!selectedKit.isRanked()){
                            commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c" + selectedKit.getDisplayName() + " does not allow ranked matches!"));
                            return false;
                        }
                        
                        QueueManager.joinQueue(player, selectedKit, true, 1);
                        commandSender.sendMessage(ChatColor.GRAY + "You have joined the unranked queue for " + ChatColor.DARK_GREEN + selectedKit.getDisplayName() + ChatColor.GRAY + "!");
                    }
                    default -> commandSender.sendMessage("§c" + strings[1] + " is not a valid option!");
                }
            }
            case "leave" -> {
                boolean result = QueueManager.leaveQueue(player);
                
                if(!result){
                    commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou are not in any queue!"));
                    return false;
                }
                
                commandSender.sendMessage(ChatColor.GRAY + "You have left the queue!");
                PlayerManager.getPlayerData(player.getUniqueId()).setPlayerState(PlayerState.SPAWN);
                PlayerManager.giveSpawnItems(player);
            }
            default -> commandSender.sendMessage("§c" + strings[0] + " is not a valid option!");
        }
        return true;
    }
}
