package com.emeraldnetwork.emeraldPractice.listeners.entity;

import com.emeraldnetwork.emeraldPractice.duel.DuelRequest;
import com.emeraldnetwork.emeraldPractice.kit.Kit;
import com.emeraldnetwork.emeraldPractice.kit.KitManager;
import com.emeraldnetwork.emeraldPractice.map.Map;
import com.emeraldnetwork.emeraldPractice.player.PlayerData;
import com.emeraldnetwork.emeraldPractice.player.PlayerManager;
import com.emeraldnetwork.emeraldPractice.utils.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryClickListener implements Listener{

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event){
        PlayerData playerData = PlayerManager.getPlayerData(event.getWhoClicked().getUniqueId());
        
        switch(playerData.getPlayerState()){
            case QUEUE, SPAWN, SPECTATING -> {
                if(event.getClickedInventory().getName().contains("Duel")){
                    String kitName;

                    if(!event.getCurrentItem().hasItemMeta())
                        return;
                        
                    kitName = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());
                    
                    for(Kit kit : KitManager.KITS){
                        if(kit.getDisplayName().equalsIgnoreCase(kitName)){
                            Inventory inventory = Bukkit.createInventory(event.getWhoClicked(), 45, ChatColor.GRAY + "Select a map");
                            
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
                }
                
                if(!playerData.getProfile().isEditMode())
                    event.setCancelled(true);
            }
        }
    }
}
