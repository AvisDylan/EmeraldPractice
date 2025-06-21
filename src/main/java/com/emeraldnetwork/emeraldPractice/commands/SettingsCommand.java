/**
 * Created by dylan on 6/12/2025
 */

package com.emeraldnetwork.emeraldPractice.commands;

import com.emeraldnetwork.emeraldPractice.misc.DeathEffect;
import com.emeraldnetwork.emeraldPractice.player.PlayerData;
import com.emeraldnetwork.emeraldPractice.player.PlayerManager;
import com.emeraldnetwork.emeraldPractice.player.PlayerState;
import com.emeraldnetwork.emeraldPractice.utils.GuiUtils;
import com.emeraldnetwork.emeraldPractice.utils.ItemUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.WeatherType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class SettingsCommand implements CommandExecutor{
    
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings){
        Player player = (Player) commandSender;
        PlayerData playerData = PlayerManager.getPlayerData(player.getUniqueId());
        
        if(playerData.getPlayerState() != PlayerState.SPAWN){
            commandSender.sendMessage(ChatColor.RED + "You can't edit settings in your current state!");
            return false;
        }
        
        Inventory inventory = GuiUtils.createInventoryWithBorder(player, 36, ChatColor.DARK_GREEN + "Settings");
        
        ItemStack receiveMessages = ItemUtils.createItem(Material.BOOK_AND_QUILL, 1, ChatColor.RESET + "" + ChatColor.DARK_GREEN + ChatColor.BOLD + "Receive Messages",
                ChatColor.GRAY + "Allow people to send you private messages",
                "",
                (playerData.getProfile().isReceiveMessages() ? ChatColor.DARK_GREEN + "Enabled" : ChatColor.GRAY + "Enabled"),
                (!playerData.getProfile().isReceiveMessages() ? ChatColor.DARK_GREEN + "Disabled" : ChatColor.GRAY + "Disabled"));
        ItemStack messageSounds = ItemUtils.createItem(Material.NOTE_BLOCK, 1, ChatColor.RESET + "" + ChatColor.DARK_GREEN + ChatColor.BOLD + "Messages Sounds",
                ChatColor.GRAY + "Play a sound when somebody messages you",
                "",
                (playerData.getProfile().isMessageSounds() ? ChatColor.DARK_GREEN + "Enabled" : ChatColor.GRAY + "Enabled"),
                (!playerData.getProfile().isMessageSounds() ? ChatColor.DARK_GREEN + "Disabled" : ChatColor.GRAY + "Disabled"));
        ItemStack duelRequests = ItemUtils.createItem(Material.STONE_SWORD, 1, ChatColor.RESET + "" + ChatColor.DARK_GREEN + ChatColor.BOLD + "Duel Requests",
                ChatColor.GRAY + "Allow people to send private duel requests",
                "",
                (playerData.getProfile().isDuelRequests() ? ChatColor.DARK_GREEN + "Enabled" : ChatColor.GRAY + "Enabled"),
                (!playerData.getProfile().isDuelSounds() ? ChatColor.DARK_GREEN + "Disabled" : ChatColor.GRAY + "Disabled"));
        ItemStack duelSounds = ItemUtils.createItem(Material.STONE_AXE, 1, ChatColor.RESET + "" + ChatColor.DARK_GREEN + ChatColor.BOLD + "Duel Sounds",
                ChatColor.GRAY + "Play a sound when somebody sends you a duel request",
                "",
                (playerData.getProfile().isDuelSounds() ? ChatColor.DARK_GREEN + "Enabled" : ChatColor.GRAY + "Enabled"),
                (!playerData.getProfile().isDuelSounds() ? ChatColor.DARK_GREEN + "Disabled" : ChatColor.GRAY + "Disabled"));
        ItemStack allowSpectators = ItemUtils.createItem(Material.ARMOR_STAND, 1, ChatColor.RESET + "" + ChatColor.DARK_GREEN + ChatColor.BOLD + "Allow Spectators",
                ChatColor.GRAY + "Allow people to spectate your matches",
                "",
                (playerData.getProfile().isAllowSpectators() ? ChatColor.DARK_GREEN + "Enabled" : ChatColor.GRAY + "Enabled"),
                (!playerData.getProfile().isAllowSpectators() ? ChatColor.DARK_GREEN + "Disabled" : ChatColor.GRAY + "Disabled"));
        ItemStack scoreBoard = ItemUtils.createItem(Material.ITEM_FRAME, 1, ChatColor.RESET + "" + ChatColor.DARK_GREEN + ChatColor.BOLD + "Scoreboard",
                ChatColor.GRAY + "See the scoreboard on the side",
                "",
                (playerData.getProfile().isScoreBoard() ? ChatColor.DARK_GREEN + "Enabled" : ChatColor.GRAY + "Enabled"),
                (!playerData.getProfile().isScoreBoard() ? ChatColor.DARK_GREEN + "Disabled" : ChatColor.GRAY + "Disabled"));
        ItemStack globalChat = ItemUtils.createItem(Material.PAPER, 1, ChatColor.RESET + "" + ChatColor.DARK_GREEN + ChatColor.BOLD + "Global Chat",
                ChatColor.GRAY + "See chat messages",
                "",
                (playerData.getProfile().isGlobalChat() ? ChatColor.DARK_GREEN + "Enabled" : ChatColor.GRAY + "Enabled"),
                (!playerData.getProfile().isGlobalChat() ? ChatColor.DARK_GREEN + "Disabled" : ChatColor.GRAY + "Disabled"));
        ItemStack partyRequests = ItemUtils.createItem(Material.NAME_TAG, 1, ChatColor.RESET + "" + ChatColor.DARK_GREEN + ChatColor.BOLD + "Party Requests",
                ChatColor.GRAY + "Allow people to invite you to their party",
                "",
                (playerData.getProfile().isPartyInvites() ? ChatColor.DARK_GREEN + "Enabled" : ChatColor.GRAY + "Enabled"),
                (!playerData.getProfile().isPartyInvites() ? ChatColor.DARK_GREEN + "Disabled" : ChatColor.GRAY + "Disabled"));
        ItemStack partySounds = ItemUtils.createItem(Material.RECORD_8, 1, ChatColor.RESET + "" + ChatColor.DARK_GREEN + ChatColor.BOLD + "Party Sounds",
                ChatColor.GRAY + "Play a sound when you receive a party invite",
                "",
                (playerData.getProfile().isPartySounds() ? ChatColor.DARK_GREEN + "Enabled" : ChatColor.GRAY + "Enabled"),
                (!playerData.getProfile().isPartySounds() ? ChatColor.DARK_GREEN + "Disabled" : ChatColor.GRAY + "Disabled"));
        ItemStack playerWeather = ItemUtils.createItem(Material.SNOW_BALL, 1, ChatColor.RESET + "" + ChatColor.DARK_GREEN + ChatColor.BOLD + "Weather Type",
                ChatColor.GRAY + "Customize what weather you see",
                "",
                (playerData.getProfile().getPlayerWeather() == WeatherType.CLEAR ? ChatColor.DARK_GREEN + "Clear" : ChatColor.GRAY + "Clear"),
                (playerData.getProfile().getPlayerWeather() == WeatherType.DOWNFALL ? ChatColor.DARK_GREEN + "Raining" : ChatColor.GRAY + "Raining"));
        ItemStack playerTime = ItemUtils.createItem(Material.WATCH, 1, ChatColor.RESET + "" + ChatColor.DARK_GREEN + ChatColor.BOLD + "World Time",
                ChatColor.GRAY + "Customize the player time",
                "",
                (playerData.getProfile().getPlayerTime() == 0 ? ChatColor.DARK_GREEN + "Sunrise" : ChatColor.GRAY + "Sunrise"),
                (playerData.getProfile().getPlayerTime() == 6000 ? ChatColor.DARK_GREEN + "Noon" : ChatColor.GRAY + "Noon"),
                (playerData.getProfile().getPlayerTime() == 12000 ? ChatColor.DARK_GREEN + "Sunset" : ChatColor.GRAY + "Sunset"),
                (playerData.getProfile().getPlayerTime() == 18000 ? ChatColor.DARK_GREEN + "Midnight" : ChatColor.GRAY + "Midnight"));
        ItemStack deathEffect = ItemUtils.createItem(Material.SKULL_ITEM, 1, ChatColor.RESET + "" + ChatColor.DARK_GREEN + ChatColor.BOLD + "Death Effect",
                ChatColor.GRAY + "Customize the what animation to play when you kill another player",
                "",
                (playerData.getProfile().getDeathEffect() == DeathEffect.NONE ? ChatColor.DARK_GREEN + "None" : ChatColor.GRAY + "None"),
                (playerData.getProfile().getDeathEffect() == DeathEffect.EXPLOSION ? ChatColor.DARK_GREEN + "Explosion" : ChatColor.GRAY + "Explosion"),
                (playerData.getProfile().getDeathEffect() == DeathEffect.BLOOD ? ChatColor.DARK_GREEN + "Blood" : ChatColor.GRAY + "Blood"),
                (playerData.getProfile().getDeathEffect() == DeathEffect.LIGHTNING ? ChatColor.DARK_GREEN + "Lightning" : ChatColor.GRAY + "Lightning"));
        ItemStack pingRange = ItemUtils.createItem(Material.STICK, 1, ChatColor.RESET + "" + ChatColor.DARK_GREEN + ChatColor.BOLD + "Ping Range",
                ChatColor.GRAY + "Only queue people within the ping range of you",
                "",
                (playerData.getProfile().getPingRange() == 0 ? ChatColor.DARK_GREEN + "Off" : ChatColor.GRAY + "Off"),
                (playerData.getProfile().getPingRange() == 50 ? ChatColor.DARK_GREEN + "50" : ChatColor.GRAY + "50"),
                (playerData.getProfile().getPingRange() == 100 ? ChatColor.DARK_GREEN + "100" : ChatColor.GRAY + "100"),
                (playerData.getProfile().getPingRange() == 150 ? ChatColor.DARK_GREEN + "150" : ChatColor.GRAY + "150"),
                (playerData.getProfile().getPingRange() == 200 ? ChatColor.DARK_GREEN + "200" : ChatColor.GRAY + "200"),
                (playerData.getProfile().getPingRange() == 250 ? ChatColor.DARK_GREEN + "250" : ChatColor.GRAY + "250"),
                (playerData.getProfile().getPingRange() == 300 ? ChatColor.DARK_GREEN + "300" : ChatColor.GRAY + "300"));
        
        inventory.addItem(receiveMessages);
        inventory.addItem(messageSounds);
        inventory.addItem(duelRequests);
        inventory.addItem(duelSounds);
        inventory.addItem(allowSpectators);
        inventory.addItem(scoreBoard);
        inventory.addItem(globalChat);
        inventory.addItem(partyRequests);
        inventory.addItem(partySounds);
        inventory.addItem(playerWeather);
        inventory.addItem(playerTime);
        inventory.addItem(deathEffect);
        inventory.addItem(pingRange);
        
        player.openInventory(inventory);
        return true;
    }
}
