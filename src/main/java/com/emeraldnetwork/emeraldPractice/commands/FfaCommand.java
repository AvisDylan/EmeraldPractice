/**
 * Created by dylan on 6/30/2025
 */

package com.emeraldnetwork.emeraldPractice.commands;

import com.emeraldnetwork.emeraldPractice.ffa.Ffa;
import com.emeraldnetwork.emeraldPractice.ffa.FfaManager;
import com.emeraldnetwork.emeraldPractice.kit.Kit;
import com.emeraldnetwork.emeraldPractice.kit.KitManager;
import com.emeraldnetwork.emeraldPractice.party.PartyManager;
import com.emeraldnetwork.emeraldPractice.player.PlayerData;
import com.emeraldnetwork.emeraldPractice.player.PlayerManager;
import com.emeraldnetwork.emeraldPractice.player.PlayerState;
import com.emeraldnetwork.emeraldPractice.utils.GuiUtils;
import com.emeraldnetwork.emeraldPractice.utils.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class FfaCommand implements CommandExecutor{
    
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings){
        if(strings.length < 1){
            commandSender.sendMessage(ChatColor.RED + "Invalid arguments!");
            return false;
        }
        
        Player player = (Player) commandSender;
        PlayerData playerData = PlayerManager.getPlayerData(player.getUniqueId());
        
        if(playerData.getPlayerState() != PlayerState.SPAWN){
            commandSender.sendMessage(ChatColor.RED + "You cannot run this command in your state!");
            return false;
        }
        
        if(PartyManager.getPlayerParty(playerData) != null){
            commandSender.sendMessage("You can't run this command while in a party");
            return false;
        }
        
        switch(strings[0].toLowerCase()){
            case "gui" -> {
                Inventory inventory = GuiUtils.createInventoryWithBorder(player, 45, ChatColor.DARK_GREEN + "FFA");
                
                for(Kit kit : KitManager.KITS){
                    if(!kit.isEnabled())
                        continue;
                    
                    if(!kit.isFfa())
                        continue;
                    
                    inventory.addItem(ItemUtils.createItem(kit.getIcon().getType(), 1, kit.getIcon().getDurability(), ChatColor.DARK_GREEN + kit.getDisplayName()));
                }
                
                player.openInventory(inventory);
            }
            case "join" -> {
                if(strings.length < 2){
                    commandSender.sendMessage(ChatColor.RED + "Invalid arguments!");
                    return false;
                }
                
                Kit kit = KitManager.getKit(strings[1]);
                
                if(kit == null){
                    commandSender.sendMessage(ChatColor.RED + strings[1] + " is not a valid kit!");
                    return false;
                }
                
                if(!kit.isEnabled()){
                    commandSender.sendMessage(ChatColor.RED + kit.getDisplayName() + " is not enabled!");
                    return false;
                }
                
                if(!kit.isFfa()){
                    commandSender.sendMessage(ChatColor.RED + kit.getDisplayName() + " is not enabled for ffa!");
                    return false;
                }
                
                commandSender.sendMessage(ChatColor.GRAY + "You have joined the " + ChatColor.DARK_GREEN + kit.getDisplayName() + ChatColor.GRAY + " ffa!");
                FfaManager.joinFfa(playerData, kit);
            }
            default -> commandSender.sendMessage(ChatColor.RED + strings[0] + " is not a valid argument!");
        }
        
        return true;
    }
}
