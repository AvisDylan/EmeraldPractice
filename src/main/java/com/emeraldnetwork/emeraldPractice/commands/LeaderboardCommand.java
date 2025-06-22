/**
 * Created by dylan on 6/20/2025
 */

package com.emeraldnetwork.emeraldPractice.commands;

import com.emeraldnetwork.emeraldPractice.kit.Kit;
import com.emeraldnetwork.emeraldPractice.kit.KitManager;
import com.emeraldnetwork.emeraldPractice.utils.GuiUtils;
import com.emeraldnetwork.emeraldPractice.utils.ItemUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class LeaderboardCommand implements CommandExecutor{
    
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings){
        Player player = (Player) commandSender;
        Inventory inventory = GuiUtils.createInventoryWithBorder(player, 45, ChatColor.DARK_GREEN + "Leaderboards");
        
        for(Kit kit : KitManager.KITS){
            if(!kit.isEnabled())
                continue;
            
            inventory.addItem(ItemUtils.createItem(kit.getIcon().getType(), 1, kit.getIcon().getDurability(), ChatColor.RESET + "" + ChatColor.DARK_GREEN + ChatColor.BOLD + kit.getDisplayName()));
        }
        
        return true;
    }
}
