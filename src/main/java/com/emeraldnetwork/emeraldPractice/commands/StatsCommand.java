/**
 * Created by dylan on 6/22/2025
 */

package com.emeraldnetwork.emeraldPractice.commands;

import com.emeraldnetwork.emeraldPractice.database.DatabaseManager;
import com.emeraldnetwork.emeraldPractice.kit.Kit;
import com.emeraldnetwork.emeraldPractice.kit.KitManager;
import com.emeraldnetwork.emeraldPractice.player.PlayerData;
import com.emeraldnetwork.emeraldPractice.player.PlayerManager;
import com.emeraldnetwork.emeraldPractice.profile.PlayerKitProfile;
import com.emeraldnetwork.emeraldPractice.profile.PlayerProfile;
import com.emeraldnetwork.emeraldPractice.utils.GuiUtils;
import com.emeraldnetwork.emeraldPractice.utils.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class StatsCommand implements CommandExecutor{
    
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings){
        if(strings.length < 1){
            commandSender.sendMessage(ChatColor.RED + "Invalid arguments!");
            return false;
        }
        
        OfflinePlayer target = Bukkit.getOfflinePlayer(strings[0]);
        Player player = (Player) commandSender;
        
        if(target == null){
            commandSender.sendMessage(ChatColor.RED + strings[0] + " is not a valid player!");
            return false;
        }
        
        PlayerProfile targetProfile = target.isOnline() ? PlayerManager.getPlayerData(target.getUniqueId()).getProfile() : DatabaseManager.loadPlayerProfile(target.getUniqueId());
        PlayerData playerData = PlayerManager.getPlayerData(player.getUniqueId());
        
        Inventory inventory = GuiUtils.createInventoryWithBorder(player, 45, ChatColor.DARK_GREEN + target.getName() + ChatColor.GRAY + "'s Stats");
        
        for(Kit kit : KitManager.KITS){
            if(!kit.isEnabled())
                continue;
            
            PlayerKitProfile playerKitProfile = targetProfile.getKitProfile(kit);
            
            if(playerKitProfile == null)
                continue;
            
            ItemStack itemStack = ItemUtils.createItem(kit.getIcon().getType(), 1, kit.getIcon().getDurability(), ChatColor.RESET + "" + ChatColor.DARK_GREEN + ChatColor.BOLD + kit.getDisplayName());
            ItemMeta itemMeta = itemStack.getItemMeta();
            List<String> lore = new ArrayList<>();
            
            lore.add(ChatColor.RESET + "" + ChatColor.DARK_GREEN + ChatColor.BOLD + "Global");
            lore.add(ChatColor.RESET + "");
            lore.add(ChatColor.RESET + "" + ChatColor.GRAY + "Winstreak: " + ChatColor.DARK_GREEN + targetProfile.getWinstreak());
            lore.add(ChatColor.RESET + "");
            lore.add(ChatColor.RESET + "" + ChatColor.DARK_GREEN + ChatColor.BOLD + "Unranked");
            lore.add(ChatColor.RESET + "");
            lore.add(ChatColor.RESET + "" + ChatColor.GRAY + "Wins: " + ChatColor.DARK_GREEN + playerKitProfile.getUnrankedWins());
            lore.add(ChatColor.RESET + "" + ChatColor.GRAY + "Losses: " + ChatColor.DARK_GREEN + playerKitProfile.getUnrankedLosses());
            lore.add(ChatColor.RESET + "");
            lore.add(ChatColor.RESET + "" + ChatColor.DARK_GREEN + ChatColor.BOLD + "Ranked");
            lore.add(ChatColor.RESET + "");
            lore.add(ChatColor.RESET + "" + ChatColor.GRAY + "Elo: " + ChatColor.DARK_GREEN + playerKitProfile.getElo());
            lore.add(ChatColor.RESET + "" + ChatColor.GRAY + "Wins: " + ChatColor.DARK_GREEN + playerKitProfile.getRankedWins());
            lore.add(ChatColor.RESET + "" + ChatColor.GRAY + "Losses: " + ChatColor.DARK_GREEN + playerKitProfile.getRankedLosses());
            lore.add(ChatColor.RESET + "");
            lore.add(ChatColor.RESET + "" + ChatColor.DARK_GREEN + ChatColor.BOLD + "General");
            lore.add(ChatColor.RESET + "");
            lore.add(ChatColor.RESET + "" + ChatColor.GRAY + "Winstreak: " + ChatColor.DARK_GREEN + playerKitProfile.getWinstreak());
            lore.add(ChatColor.RESET + "" + ChatColor.GRAY + "Kills: " + ChatColor.DARK_GREEN + playerKitProfile.getKills());
            lore.add(ChatColor.RESET + "" + ChatColor.GRAY + "Deaths: " + ChatColor.DARK_GREEN + playerKitProfile.getDeaths());
            lore.add(ChatColor.RESET + "" + ChatColor.GRAY + "K/D: " + ChatColor.DARK_GREEN + playerKitProfile.getKd());
            
            itemMeta.setLore(lore);
            itemStack.setItemMeta(itemMeta);
            inventory.addItem(itemStack);
        }
        
        player.openInventory(inventory);
        
        return true;
    }
}
