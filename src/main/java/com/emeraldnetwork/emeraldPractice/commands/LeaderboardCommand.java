/**
 * Created by dylan on 6/20/2025
 */

package com.emeraldnetwork.emeraldPractice.commands;

import com.emeraldnetwork.emeraldPractice.kit.Kit;
import com.emeraldnetwork.emeraldPractice.kit.KitManager;
import com.emeraldnetwork.emeraldPractice.player.PlayerData;
import com.emeraldnetwork.emeraldPractice.player.PlayerManager;
import com.emeraldnetwork.emeraldPractice.utils.GuiUtils;
import com.emeraldnetwork.emeraldPractice.utils.ItemUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class LeaderboardCommand implements CommandExecutor{
    
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings){
        Player player = (Player) commandSender;
        PlayerData playerData = PlayerManager.getPlayerData(player.getUniqueId());
        Inventory inventory = GuiUtils.createInventoryWithBorder(player, 45, ChatColor.DARK_GREEN + "Leaderboards");
        
        for(Kit kit : KitManager.KITS){
            if(!kit.isEnabled())
                continue;
            
            ItemStack itemStack = ItemUtils.createItem(kit.getIcon().getType(), 1, kit.getIcon().getDurability(), ChatColor.RESET + "" + ChatColor.DARK_GREEN + ChatColor.BOLD + kit.getDisplayName());
            ItemMeta itemMeta = itemStack.getItemMeta();
            List<String> lore = new ArrayList<>();
            
            lore.add(ChatColor.translateAlternateColorCodes('&', "&2&lUnranked"));
            lore.add(ChatColor.translateAlternateColorCodes('&', "&7Your Place In Leaderboards: &71"));
            lore.add("");
            
            for(int i = 0; i < 10; i++){
                lore.add(ChatColor.translateAlternateColorCodes('&', "&7#" + (i + 1) + ": &2" + (kit.getTopUnrankedPlayers().get(i) == null ? "None" : kit.getTopUnrankedPlayers().get(i)) + "&7 - &21"));
            }
            
            if(kit.isRanked()){
                lore.add("");
                lore.add(ChatColor.translateAlternateColorCodes('&', "&2&lRanked"));
                lore.add(ChatColor.translateAlternateColorCodes('&', "&7Your Place In Leaderboards: &71"));
                lore.add("");
                
                for(int i = 0; i < 10; i++){
                    lore.add(ChatColor.translateAlternateColorCodes('&', "&7#" + (i + 1) + ": &2" + (kit.getTopUnrankedPlayers().get(i) == null ? "None" : kit.getTopUnrankedPlayers().get(i)) + "&7 - &21"));
                }
            }
            
            itemMeta.setLore(lore);
            itemStack.setItemMeta(itemMeta);
            inventory.addItem(itemStack);
        }
        
        player.openInventory(inventory);
        
        return true;
    }
}
