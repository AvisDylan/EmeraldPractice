/**
 * Created by dylan on 6/19/2025
 */

package com.emeraldnetwork.emeraldPractice.commands;

import com.emeraldnetwork.emeraldPractice.kit.Kit;
import com.emeraldnetwork.emeraldPractice.kit.KitManager;
import com.emeraldnetwork.emeraldPractice.player.PlayerData;
import com.emeraldnetwork.emeraldPractice.player.PlayerManager;
import com.emeraldnetwork.emeraldPractice.player.PlayerState;
import com.emeraldnetwork.emeraldPractice.utils.GuiUtils;
import com.emeraldnetwork.emeraldPractice.utils.ItemUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class KitEditorCommand implements CommandExecutor{
    
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings){
        Player player = (Player) commandSender;
        PlayerData playerData = PlayerManager.getPlayerData(player.getUniqueId());
        
        if(playerData.getPlayerState() != PlayerState.SPAWN){
            commandSender.sendMessage(ChatColor.RED + "You can't run this command in your state!");
            return false;
        }
        
        Inventory inventory = GuiUtils.createInventoryWithBorder(player, 45, ChatColor.DARK_GREEN + "Select a Kit to Edit");
        
        for(int i = 0; i < KitManager.KITS.size(); i++){
            Kit kit = KitManager.KITS.get(i);
            
            if(!kit.isEnabled() || !kit.isEditable())
                continue;
            
            inventory.addItem(ItemUtils.createItem(kit.getIcon().getType(), 1, ChatColor.DARK_GREEN + kit.getDisplayName()));
        }
        
        player.openInventory(inventory);
        return true;
    }
}
