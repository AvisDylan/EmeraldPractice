package com.emeraldnetwork.emeraldPractice.listeners.entity;

import com.emeraldnetwork.emeraldPractice.duel.DuelRequest;
import com.emeraldnetwork.emeraldPractice.kit.Kit;
import com.emeraldnetwork.emeraldPractice.kit.KitManager;
import com.emeraldnetwork.emeraldPractice.map.Map;
import com.emeraldnetwork.emeraldPractice.misc.DeathEffect;
import com.emeraldnetwork.emeraldPractice.player.PlayerData;
import com.emeraldnetwork.emeraldPractice.player.PlayerManager;
import com.emeraldnetwork.emeraldPractice.utils.GuiUtils;
import com.emeraldnetwork.emeraldPractice.utils.ItemUtils;
import me.zowpy.core.api.CoreAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.WeatherType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class InventoryClickListener implements Listener{

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event){
        PlayerData playerData = PlayerManager.getPlayerData(event.getWhoClicked().getUniqueId());
        
        if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(" ")){
            event.setCancelled(true);
            return;
        }
        
        switch(playerData.getPlayerState()){
            case QUEUE, SPAWN, SPECTATING -> {
                if(event.getClickedInventory().getName().contains("Duel")){
                    String kitName;

                    if(!event.getCurrentItem().hasItemMeta())
                        return;
                        
                    kitName = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());
                    
                    for(Kit kit : KitManager.KITS){
                        if(kit.getDisplayName().equalsIgnoreCase(kitName)){
                            Inventory inventory = GuiUtils.createInventoryWithBorder((Player) event.getWhoClicked(), 45, ChatColor.DARK_GREEN + "Select a map");
                            
                            kit.getMaps().forEach(map -> inventory.addItem(ItemUtils.createItem(map.getIcon().getType(), 1, ChatColor.DARK_GREEN + map.getDisplayName())));
                            
                            event.getWhoClicked().openInventory(inventory);
                            playerData.setTempRequest(new DuelRequest(kit, playerData.getTempRequest().getReceiver(), playerData.getTempRequest().getSender()));
                            break;
                        }
                    }
                }else if(event.getClickedInventory().getName().contains("Select a map")){
                    String mapName;
                    
                    if(!event.getCurrentItem().hasItemMeta())
                        return;
                    
                    mapName = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());
                    
                    for(Map map : playerData.getTempRequest().getKit().getMaps()){
                        if(map.getDisplayName().equalsIgnoreCase(mapName)){
                            playerData.getTempRequest().getReceiver().getDuelRequests().add(new DuelRequest(map, playerData.getTempRequest().getKit(), playerData.getTempRequest().getReceiver(), playerData.getTempRequest().getSender()));
                            
                            //send sender message
                            event.getWhoClicked().sendMessage("");
                            event.getWhoClicked().sendMessage(ChatColor.RESET + "" + ChatColor.DARK_GREEN + ChatColor.BOLD + "Duel Request");
                            event.getWhoClicked().sendMessage(ChatColor.RESET + "" + ChatColor.GRAY + "To: " + ChatColor.DARK_GREEN + Bukkit.getPlayer(playerData.getTempRequest().getReceiver().getUuid()).getName() + ChatColor.GRAY + " (" + playerData.getTempRequest().getReceiver().getPing() + " ms)");
                            event.getWhoClicked().sendMessage(ChatColor.RESET + "" + ChatColor.GRAY + "Kit: " + ChatColor.DARK_GREEN + playerData.getTempRequest().getKit().getDisplayName());
                            event.getWhoClicked().sendMessage(ChatColor.RESET + "" + ChatColor.GRAY + "Map: " + ChatColor.DARK_GREEN + map.getDisplayName());
                            event.getWhoClicked().sendMessage("");
                            
                            playerData.setTempRequest(null);
                            event.getWhoClicked().closeInventory();
                            break;
                        }
                    }
                }else if(event.getClickedInventory().getTitle().equalsIgnoreCase(ChatColor.GRAY + "Choose a player")){
                    Player player = Bukkit.getPlayer(ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName()));
                    
                    if(player != null)
                        event.getWhoClicked().teleport(player);
                }else if(event.getClickedInventory().getTitle().equalsIgnoreCase(ChatColor.DARK_GREEN + "Settings")){
                    if(!event.getCurrentItem().hasItemMeta())
                        return;
                    
                    ItemStack item = event.getCurrentItem();
                    ItemMeta itemMeta = item.getItemMeta();
                    String itemName = itemMeta.getDisplayName();
                    
                    if(itemName.equalsIgnoreCase(ChatColor.RESET + "" + ChatColor.DARK_GREEN + ChatColor.BOLD + "Receive Messages")){
                        playerData.getProfile().setReceiveMessages(!playerData.getProfile().isReceiveMessages());
                        
                        List<String> lore = List.of(ChatColor.GRAY + "Allow people to send you private messages",
                                "",
                                (playerData.getProfile().isReceiveMessages() ? ChatColor.DARK_GREEN + "Enabled" : ChatColor.GRAY + "Enabled"),
                                (!playerData.getProfile().isReceiveMessages() ? ChatColor.DARK_GREEN + "Disabled" : ChatColor.GRAY + "Disabled"));
                        
                        CoreAPI.getInstance().getProfileManager().getByUUID(event.getWhoClicked().getUniqueId()).getSettings().setReceivePrivateMessages(playerData.getProfile().isReceiveMessages());
                        itemMeta.setLore(lore);
                        item.setItemMeta(itemMeta);
                    }else if(itemName.equalsIgnoreCase(ChatColor.RESET + "" + ChatColor.DARK_GREEN + ChatColor.BOLD + "Receive Friend Requests")){
                        playerData.getProfile().setReceiveFriendRequests(!playerData.getProfile().isReceiveFriendRequests());
                        
                        List<String> lore = List.of(ChatColor.GRAY + "Allow people to add you as a friend",
                                "",
                                (playerData.getProfile().isReceiveFriendRequests() ? ChatColor.DARK_GREEN + "Enabled" : ChatColor.GRAY + "Enabled"),
                                (!playerData.getProfile().isReceiveFriendRequests() ? ChatColor.DARK_GREEN + "Disabled" : ChatColor.GRAY + "Disabled"));
                        
                        CoreAPI.getInstance().getProfileManager().getByUUID(event.getWhoClicked().getUniqueId()).getSettings().setFriendRequests(playerData.getProfile().isReceiveFriendRequests());
                        itemMeta.setLore(lore);
                        item.setItemMeta(itemMeta);
                    }else if(itemName.equalsIgnoreCase(ChatColor.RESET + "" + ChatColor.DARK_GREEN + ChatColor.BOLD + "Duel Requests")){
                        playerData.getProfile().setDuelRequests(!playerData.getProfile().isDuelRequests());
                        
                        List<String> lore = List.of(ChatColor.GRAY + "Allow people to send private duel requests",
                                "",
                                (playerData.getProfile().isDuelRequests() ? ChatColor.DARK_GREEN + "Enabled" : ChatColor.GRAY + "Enabled"),
                                (!playerData.getProfile().isDuelRequests() ? ChatColor.DARK_GREEN + "Disabled" : ChatColor.GRAY + "Disabled"));
                        
                        itemMeta.setLore(lore);
                        item.setItemMeta(itemMeta);
                    }else if(itemName.equalsIgnoreCase(ChatColor.RESET + "" + ChatColor.DARK_GREEN + ChatColor.BOLD + "Duel Sounds")){
                        playerData.getProfile().setDuelSounds(!playerData.getProfile().isDuelSounds());
                        
                        List<String> lore = List.of(ChatColor.GRAY + "Play a sound when somebody sends you a duel request",
                                "",
                                (playerData.getProfile().isDuelSounds() ? ChatColor.DARK_GREEN + "Enabled" : ChatColor.GRAY + "Enabled"),
                                (!playerData.getProfile().isDuelSounds() ? ChatColor.DARK_GREEN + "Disabled" : ChatColor.GRAY + "Disabled"));
                        
                        itemMeta.setLore(lore);
                        item.setItemMeta(itemMeta);
                    }else if(itemName.equalsIgnoreCase(ChatColor.RESET + "" + ChatColor.DARK_GREEN + ChatColor.BOLD + "Allow Spectators")){
                        playerData.getProfile().setAllowSpectators(!playerData.getProfile().isAllowSpectators());
                        
                        List<String> lore = List.of(ChatColor.GRAY + "Allow people to spectate your matches",
                                "",
                                (playerData.getProfile().isAllowSpectators() ? ChatColor.DARK_GREEN + "Enabled" : ChatColor.GRAY + "Enabled"),
                                (!playerData.getProfile().isAllowSpectators() ? ChatColor.DARK_GREEN + "Disabled" : ChatColor.GRAY + "Disabled"));
                        
                        itemMeta.setLore(lore);
                        item.setItemMeta(itemMeta);
                    }else if(itemName.equalsIgnoreCase(ChatColor.RESET + "" + ChatColor.DARK_GREEN + ChatColor.BOLD + "Scoreboard")){
                        playerData.getProfile().setScoreBoard(!playerData.getProfile().isScoreBoard());
                        
                        List<String> lore = List.of(ChatColor.GRAY + "See the scoreboard on the side",
                                "",
                                (playerData.getProfile().isScoreBoard() ? ChatColor.DARK_GREEN + "Enabled" : ChatColor.GRAY + "Enabled"),
                                (!playerData.getProfile().isScoreBoard() ? ChatColor.DARK_GREEN + "Disabled" : ChatColor.GRAY + "Disabled"));
                        
                        itemMeta.setLore(lore);
                        item.setItemMeta(itemMeta);
                    }else if(itemName.equalsIgnoreCase(ChatColor.RESET + "" + ChatColor.DARK_GREEN + ChatColor.BOLD + "Global Chat")){
                        playerData.getProfile().setGlobalChat(!playerData.getProfile().isGlobalChat());
                        
                        List<String> lore = List.of(ChatColor.GRAY + "See chat messages",
                                "",
                                (playerData.getProfile().isGlobalChat() ? ChatColor.DARK_GREEN + "Enabled" : ChatColor.GRAY + "Enabled"),
                                (!playerData.getProfile().isGlobalChat() ? ChatColor.DARK_GREEN + "Disabled" : ChatColor.GRAY + "Disabled"));
                        
                        CoreAPI.getInstance().getProfileManager().getByUUID(event.getWhoClicked().getUniqueId()).getSettings().setGlobalChat(playerData.getProfile().isGlobalChat());
                        itemMeta.setLore(lore);
                        item.setItemMeta(itemMeta);
                    }else if(itemName.equalsIgnoreCase(ChatColor.RESET + "" + ChatColor.DARK_GREEN + ChatColor.BOLD + "Party Requests")){
                        playerData.getProfile().setPartyInvites(!playerData.getProfile().isPartyInvites());
                        
                        List<String> lore = List.of(ChatColor.GRAY + "Allow people to invite you to their party",
                                "",
                                (playerData.getProfile().isPartyInvites() ? ChatColor.DARK_GREEN + "Enabled" : ChatColor.GRAY + "Enabled"),
                                (!playerData.getProfile().isPartyInvites() ? ChatColor.DARK_GREEN + "Disabled" : ChatColor.GRAY + "Disabled"));
                        
                        itemMeta.setLore(lore);
                        item.setItemMeta(itemMeta);
                    }else if(itemName.equalsIgnoreCase(ChatColor.RESET + "" + ChatColor.DARK_GREEN + ChatColor.BOLD + "Party Sounds")){
                        playerData.getProfile().setPartySounds(!playerData.getProfile().isPartySounds());
                        
                        List<String> lore = List.of(ChatColor.GRAY + "Play a sound when you receive a party invite",
                                "",
                                (playerData.getProfile().isPartySounds() ? ChatColor.DARK_GREEN + "Enabled" : ChatColor.GRAY + "Enabled"),
                                (!playerData.getProfile().isPartySounds() ? ChatColor.DARK_GREEN + "Disabled" : ChatColor.GRAY + "Disabled"));
                        
                        itemMeta.setLore(lore);
                        item.setItemMeta(itemMeta);
                    }else if(itemName.equalsIgnoreCase(ChatColor.RESET + "" + ChatColor.DARK_GREEN + ChatColor.BOLD + "Weather Type")){
                        playerData.getProfile().setPlayerWeather(playerData.getProfile().getPlayerWeather() == WeatherType.CLEAR ? WeatherType.DOWNFALL : WeatherType.CLEAR);
                        
                        List<String> lore = List.of(ChatColor.GRAY + "Customize what weather you see",
                                "",
                                (playerData.getProfile().getPlayerWeather() == WeatherType.CLEAR ? ChatColor.DARK_GREEN + "Clear" : ChatColor.GRAY + "Clear"),
                                (playerData.getProfile().getPlayerWeather() == WeatherType.DOWNFALL ? ChatColor.DARK_GREEN + "Raining" : ChatColor.GRAY + "Raining"));
                        
                        ((Player) event.getWhoClicked()).setPlayerWeather(playerData.getProfile().getPlayerWeather());
                        itemMeta.setLore(lore);
                        item.setItemMeta(itemMeta);
                    }else if(itemName.equalsIgnoreCase(ChatColor.RESET + "" + ChatColor.DARK_GREEN + ChatColor.BOLD + "World Time")){
                        playerData.getProfile().setPlayerTime(playerData.getProfile().getPlayerTime() >= 18000 ? 0 : playerData.getProfile().getPlayerTime() + 6000);
                        
                        List<String> lore = List.of(ChatColor.GRAY + "Customize the player time",
                                "",
                                (playerData.getProfile().getPlayerTime() == 0 ? ChatColor.DARK_GREEN + "Sunrise" : ChatColor.GRAY + "Sunrise"),
                                (playerData.getProfile().getPlayerTime() == 6000 ? ChatColor.DARK_GREEN + "Noon" : ChatColor.GRAY + "Noon"),
                                (playerData.getProfile().getPlayerTime() == 12000 ? ChatColor.DARK_GREEN + "Sunset" : ChatColor.GRAY + "Sunset"),
                                (playerData.getProfile().getPlayerTime() == 18000 ? ChatColor.DARK_GREEN + "Midnight" : ChatColor.GRAY + "Midnight"));
                        
                        ((Player) event.getWhoClicked()).setPlayerTime(playerData.getProfile().getPlayerTime(), false);
                        itemMeta.setLore(lore);
                        item.setItemMeta(itemMeta);
                    }else if(itemName.equalsIgnoreCase(ChatColor.RESET + "" + ChatColor.DARK_GREEN + ChatColor.BOLD + "Death Effect")){
                        if(playerData.getProfile().getDeathEffect() == DeathEffect.NONE)
                            playerData.getProfile().setDeathEffect(DeathEffect.EXPLOSION);
                        else if(playerData.getProfile().getDeathEffect() == DeathEffect.EXPLOSION)
                            playerData.getProfile().setDeathEffect(DeathEffect.BLOOD);
                        else if(playerData.getProfile().getDeathEffect() == DeathEffect.BLOOD)
                            playerData.getProfile().setDeathEffect(DeathEffect.LIGHTNING);
                        else
                            playerData.getProfile().setDeathEffect(DeathEffect.NONE);
                        
                        List<String> lore = List.of(ChatColor.GRAY + "Customize the what animation to play when you kill another player",
                                "",
                                (playerData.getProfile().getDeathEffect() == DeathEffect.NONE ? ChatColor.DARK_GREEN + "None" : ChatColor.GRAY + "None"),
                                (playerData.getProfile().getDeathEffect() == DeathEffect.EXPLOSION ? ChatColor.DARK_GREEN + "Explosion" : ChatColor.GRAY + "Explosion"),
                                (playerData.getProfile().getDeathEffect() == DeathEffect.BLOOD ? ChatColor.DARK_GREEN + "Blood" : ChatColor.GRAY + "Blood"),
                                (playerData.getProfile().getDeathEffect() == DeathEffect.LIGHTNING ? ChatColor.DARK_GREEN + "Lightning" : ChatColor.GRAY + "Lightning"));
                        
                        ((Player) event.getWhoClicked()).setPlayerWeather(playerData.getProfile().getPlayerWeather());
                        itemMeta.setLore(lore);
                        item.setItemMeta(itemMeta);
                    }else if(itemName.equalsIgnoreCase(ChatColor.RESET + "" + ChatColor.DARK_GREEN + ChatColor.BOLD + "Ping Range")){
                        playerData.getProfile().setPingRange(playerData.getProfile().getPingRange() >= 300 ? 0 : playerData.getProfile().getPingRange() + 50);
                        
                        List<String> lore = List.of(ChatColor.GRAY + "Only queue people within the ping range of you",
                                "",
                                (playerData.getProfile().getPingRange() == 0 ? ChatColor.DARK_GREEN + "Off" : ChatColor.GRAY + "Off"),
                                (playerData.getProfile().getPingRange() == 50 ? ChatColor.DARK_GREEN + "50" : ChatColor.GRAY + "50"),
                                (playerData.getProfile().getPingRange() == 100 ? ChatColor.DARK_GREEN + "100" : ChatColor.GRAY + "100"),
                                (playerData.getProfile().getPingRange() == 150 ? ChatColor.DARK_GREEN + "150" : ChatColor.GRAY + "150"),
                                (playerData.getProfile().getPingRange() == 200 ? ChatColor.DARK_GREEN + "200" : ChatColor.GRAY + "200"),
                                (playerData.getProfile().getPingRange() == 250 ? ChatColor.DARK_GREEN + "250" : ChatColor.GRAY + "250"),
                                (playerData.getProfile().getPingRange() == 300 ? ChatColor.DARK_GREEN + "300" : ChatColor.GRAY + "300"));
                        
                        itemMeta.setLore(lore);
                        item.setItemMeta(itemMeta);
                    }
                }else if(event.getClickedInventory().getTitle().equalsIgnoreCase(ChatColor.DARK_GREEN + "Unranked Queue")){
                    if(!event.getCurrentItem().hasItemMeta())
                        return;
                    
                    String itemName = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());
                    
                    ((Player) event.getWhoClicked()).chat("/queue join unranked " + itemName);
                    event.getWhoClicked().closeInventory();
                }else if(event.getClickedInventory().getTitle().equalsIgnoreCase(ChatColor.DARK_GREEN + "Ranked Queue")){
                    if(!event.getCurrentItem().hasItemMeta())
                        return;
                    
                    String itemName = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());
                    
                    ((Player) event.getWhoClicked()).chat("/queue join ranked " + itemName);
                    event.getWhoClicked().closeInventory();
                }else if(event.getClickedInventory().getTitle().equalsIgnoreCase(ChatColor.DARK_GREEN + "Select a Kit to Edit")){
                    if(!event.getCurrentItem().hasItemMeta())
                        return;
                    
                    String itemName = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());
                    Kit kit = KitManager.getKit(itemName);
                    //event.getWhoClicked().closeInventory();
                    
                    Inventory inventory = GuiUtils.createInventoryWithBorder((Player) event.getWhoClicked(), 36, ChatColor.GRAY + "Edit " + ChatColor.DARK_GREEN + kit.getDisplayName());
                    
                    for(int i = 0; i < 36; i++){
                        if(kit.getEditorItems()[i] == null)
                            continue;
                        
                        inventory.setItem(i, kit.getEditorItems()[i]);
                    }
                    
                    event.getWhoClicked().getInventory().setContents(playerData.getProfile().getKitProfile(kit).getKitLayoutItems() == null ? kit.getItems() : playerData.getProfile().getKitProfile(kit).getKitLayoutItems());
                    event.getWhoClicked().openInventory(inventory);
                }
                
                if(event.getView().getTitle().contains("Edit "))
                    return;
                
                if(!playerData.getProfile().isEditMode())
                    event.setCancelled(true);
            }
        }
    }
}
