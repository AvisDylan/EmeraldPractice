package com.emeraldnetwork.emeraldPractice.commands;

import com.emeraldnetwork.emeraldPractice.match.MatchManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class InventoryCommand implements CommandExecutor{
    
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings){
        if(strings.length < 1){
            commandSender.sendMessage(ChatColor.RED + " Invalid Arguments!");
            return false;
        }
        
        UUID inventoryUuid = UUID.fromString(strings[0]);
        
        if(MatchManager.INVENTORY_MAP.containsKey(inventoryUuid)){
            Player player = (Player) commandSender;
            ItemStack[] sourceInventory = MatchManager.INVENTORY_MAP.get(inventoryUuid);
            Inventory inventory = Bukkit.createInventory(player, sourceInventory.length, Bukkit.getPlayer(inventoryUuid).getName() + "'s inventory");
            
            inventory.setContents(sourceInventory);
            
            player.openInventory(inventory);
        }else
            commandSender.sendMessage(ChatColor.RED + "Invalid UUID!");
        
        return true;
    }
}
