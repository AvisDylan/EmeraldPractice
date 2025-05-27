package com.emeraldnetwork.emeraldPractice.commands;

import com.emeraldnetwork.emeraldPractice.kit.Kit;
import com.emeraldnetwork.emeraldPractice.kit.KitManager;
import com.emeraldnetwork.emeraldPractice.player.PlayerManager;
import com.emeraldnetwork.emeraldPractice.player.PlayerState;
import com.emeraldnetwork.emeraldPractice.queue.QueueEntry;
import com.emeraldnetwork.emeraldPractice.queue.QueueManager;
import com.emeraldnetwork.emeraldPractice.utils.ItemUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class QueueCommand implements CommandExecutor{
    
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings){
        Player player = (Player) commandSender;
        
        switch(strings[0].toLowerCase()){
            case "join":
                if(!PlayerManager.getPlayerData(player).getPlayerState().equals(PlayerState.SPAWN)){
                    commandSender.sendMessage("§cYou can't run this command in your state!");
                    return false;
                }
                
                switch(strings[1].toLowerCase()){
                    case "unranked", "ranked":
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
                        
                        QueueManager.joinQueue(player, selectedKit, strings[1].equalsIgnoreCase("ranked"));
                        commandSender.sendMessage("§aYou have joined the queue for " + selectedKit.getDisplayName() + "!");
                        break;
                    default:
                        commandSender.sendMessage("§c" + strings[1] + " is not a valid option!");
                }
                break;
            case "leave":
                for(QueueEntry queueEntry : QueueManager.QUEUE){
                    if(queueEntry.getPlayerData().equals(PlayerManager.getPlayerData(player))){
                        commandSender.sendMessage("§aYou have left the queue for " + queueEntry.getKit().getDisplayName() + "!");
                        PlayerManager.getPlayerData(player).setPlayerState(PlayerState.SPAWN);
                        QueueManager.QUEUE.remove(queueEntry);
                        PlayerManager.giveSpawnItems(player);
                        return false;
                    }
                }
                
                //no need to check if a player is in a queue because it checks for queue state
                break;
            default:
                commandSender.sendMessage("§c" + strings[0] + " is not a valid option!");
        }
        return true;
    }
}
