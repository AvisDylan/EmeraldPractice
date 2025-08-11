package com.emeraldnetwork.emeraldPractice.commands;

import com.emeraldnetwork.emeraldPractice.duel.DuelRequest;
import com.emeraldnetwork.emeraldPractice.kit.Kit;
import com.emeraldnetwork.emeraldPractice.kit.KitManager;
import com.emeraldnetwork.emeraldPractice.party.PartyManager;
import com.emeraldnetwork.emeraldPractice.player.PlayerData;
import com.emeraldnetwork.emeraldPractice.player.PlayerManager;
import com.emeraldnetwork.emeraldPractice.player.PlayerState;
import com.emeraldnetwork.emeraldPractice.utils.GuiUtils;
import com.emeraldnetwork.emeraldPractice.utils.ItemUtils;
import me.zowpy.core.api.CoreAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class DuelCommand implements CommandExecutor{
    
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings){
        if(strings.length < 1){
            commandSender.sendMessage(ChatColor.RED + " Invalid arguments!");
            return false;
        }
        
        Player receiver = Bukkit.getPlayer(strings[0]);
        Player sender = (Player) commandSender;
        
        if(receiver != null){
            PlayerData receiverData = PlayerManager.getPlayerData(receiver.getUniqueId());
            PlayerData senderData = PlayerManager.getPlayerData(sender.getUniqueId());
            
            if(sender.equals(receiver)){
                commandSender.sendMessage(ChatColor.RED + "You can't duel yourself!");
                return false;
            }
            
            if(PartyManager.getPlayerParty(senderData) != null){
                commandSender.sendMessage(ChatColor.RED + "You cannot duel " + receiver.getName() + " as you are in a party!");
                return false;
            }
            
            if(PartyManager.getPlayerParty(receiverData) != null){
                commandSender.sendMessage(ChatColor.RED + "You cannot duel " + receiver.getName() + " as they are in a party!");
                return false;
            }
            
            if(senderData.getDuelRequest(receiverData) != null){
                sender.chat("/accept " + receiver.getName());
                return false;
            }
            
            if(receiverData.getDuelRequest(senderData) != null){
                commandSender.sendMessage(ChatColor.RED + "You already sent a duel request to " + receiver.getName() + "!");
                return false;
            }
            
            if(receiverData.getPlayerState() != PlayerState.SPAWN){
                commandSender.sendMessage(ChatColor.RED + receiver.getName() + " is not at spawn!");
                return false;
            }
            
            if(!receiverData.getProfile().isDuelRequests() || CoreAPI.getInstance().getProfileManager().getByUUID(receiver.getUniqueId()).getIgnoreList().contains(sender.getUniqueId())){
                commandSender.sendMessage(ChatColor.RED + receiver.getName() + " is not accepting duel requests at the moment!");
                return false;
            }
            
            if(senderData.getPlayerState() != PlayerState.SPAWN){
                commandSender.sendMessage(ChatColor.RED + "You are not at spawn!");
                return false;
            }
            
            Inventory inventory = GuiUtils.createInventoryWithBorder(sender.getPlayer(), 45, ChatColor.GRAY + "Duel " + ChatColor.GREEN + receiver.getName());
            
            for(int i = 0; i < KitManager.KITS.size(); i++){
                Kit kit = KitManager.KITS.get(i);
                
                inventory.addItem(ItemUtils.createItem(kit.getIcon().getType(), 1, kit.getIcon().getDurability(), ChatColor.DARK_GREEN + kit.getDisplayName()));
            }
            
            sender.openInventory(inventory);
            senderData.setTempRequest(new DuelRequest(null, receiverData, senderData));
        }else
            commandSender.sendMessage(ChatColor.RED + strings[0] + " is not a valid player!");
        return true;
    }
}
