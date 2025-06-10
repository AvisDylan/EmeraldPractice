package com.emeraldnetwork.emeraldPractice.commands;

import com.emeraldnetwork.emeraldPractice.kit.Kit;
import com.emeraldnetwork.emeraldPractice.kit.KitManager;
import com.emeraldnetwork.emeraldPractice.player.PlayerData;
import com.emeraldnetwork.emeraldPractice.player.PlayerManager;
import com.emeraldnetwork.emeraldPractice.player.PlayerState;
import com.emeraldnetwork.emeraldPractice.utils.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class DuelCommand implements CommandExecutor{
    
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings){
        if(strings.length < 1){
            commandSender.sendMessage(ChatColor.RED + " Invalid Arguments!");
            return false;
        }
        
        Player receiver = Bukkit.getPlayer(strings[0]);
        Player sender = (Player) commandSender;
        
        if(receiver != null){
            PlayerData receiverData = PlayerManager.getPlayerData(receiver.getUniqueId());
            PlayerData senderData = PlayerManager.getPlayerData(sender.getUniqueId());
            
            if(receiverData.getPlayerState() != PlayerState.SPAWN){
                commandSender.sendMessage(ChatColor.RED + receiver.getName() + " is not at spawn!");
                return false;
            }
            
            if(!receiverData.getProfile().isDuelRequests()){
                commandSender.sendMessage(ChatColor.RED + receiver.getName() + " is not accepting duel requests at the moment!");
                return false;
            }
            
            if(senderData.getPlayerState() != PlayerState.SPAWN){
                commandSender.sendMessage(ChatColor.RED + "You are not at spawn!");
                return false;
            }
            
            Inventory inventory = Bukkit.createInventory(sender, 45, ChatColor.GRAY + "Duel " + ChatColor.GREEN + receiver.getName());
            
            for(int i = 0; i < KitManager.KITS.size(); i++){
                Kit kit = KitManager.KITS.get(i);
                ItemStack itemStack = ItemUtils.createItem(kit.getIcon().getType(), 1, ChatColor.DARK_GREEN + kit.getDisplayName());
                
                inventory.setItem(i, itemStack);
            }
            
            sender.openInventory(inventory);
            
            commandSender.sendMessage("You sent a duel to " + receiver.getName());
            receiver.sendMessage("You received a duel from " + sender.getName());
        }else
            commandSender.sendMessage(ChatColor.RED + strings[0] + " is not a valid player!");
        return true;
    }
}
