package com.emeraldnetwork.emeraldPractice.commands;

import com.emeraldnetwork.emeraldPractice.kit.Kit;
import com.emeraldnetwork.emeraldPractice.kit.KitManager;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

public class KitCommand implements CommandExecutor{
    
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings){
        if(commandSender.hasPermission("emerald.practice")){
            Player player = (Player) commandSender;
            
            switch(strings[0].toLowerCase()){
                case "list" -> {
                    if(KitManager.KITS.isEmpty()){
                        commandSender.sendMessage("§cThere are no kits!");
                        return false;
                    }
                    
                    StringBuilder stringBuilder = new StringBuilder();
                    
                    for(Kit kit : KitManager.KITS){
                        stringBuilder.append(kit.getName()).append(", ");
                    }
                    
                    commandSender.sendMessage("§aKits: " + stringBuilder.substring(0, stringBuilder.length() - 2));
                }
                case "create" -> {
                    Kit kit = new Kit(strings[1]);
                    
                    KitManager.KITS.add(kit);
                    commandSender.sendMessage("§aCreated kit called " + strings[1] + "!");
                }
                case "delete" -> {
                    for(Kit kit1 : KitManager.KITS){
                        if(kit1.getName().equalsIgnoreCase(strings[1])){
                            KitManager.KITS.remove(kit1);
                            commandSender.sendMessage("§aRemoved kit called " + strings[1] + "!");
                            return false;
                        }
                    }
                    
                    commandSender.sendMessage("§c" + strings[1] + " is not a valid kit!");
                }
                case "edit" -> {
                    for(Kit kit1 : KitManager.KITS){
                        if(kit1.getName().equalsIgnoreCase(strings[1])){
                            switch(strings[2]){
                                case "name" -> {
                                    kit1.setName(strings[3]);
                                    commandSender.sendMessage("§aSet " + strings[1] + "'s name to " + strings[3] + "!");
                                }
                                case "displayname" -> {
                                    kit1.setDisplayName(strings[3]);
                                    commandSender.sendMessage("§aSet " + strings[1] + "'s display name to " + strings[3] + "!");
                                }
                                case "potion", "potionaffects", "potioneffects", "potioneffect", "potionaffect" -> {
                                    kit1.setPotionEffects(player.getActivePotionEffects().toArray(new PotionEffect[0]));
                                    commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aSet " + strings[1] + " potion effects your potion effects!"));
                                }
                                case "inventory" -> {
                                    kit1.setItems(player.getInventory().getContents());
                                    kit1.setArmourItems(player.getInventory().getArmorContents());
                                    commandSender.sendMessage("§aSet " + strings[1] + "'s items your inventory contents!");
                                }
                                case "editinventory" -> {
                                    kit1.setEditorItems(player.getInventory().getContents());
                                    commandSender.sendMessage("§aSet " + strings[1] + "'s editor items your inventory contents!");
                                }
                                case "icon" -> {
                                    kit1.setIcon(player.getItemInHand());
                                    commandSender.sendMessage("§aSet " + strings[1] + "'s icon to the item in your hand!");
                                }
                                case "ranked" -> {
                                    kit1.setRanked(!kit1.isRanked());
                                    commandSender.sendMessage(kit1.isRanked() ? "§aEnabled ranked on " + strings[1] + "!" : "§aDisabled ranked on " + strings[1] + "!");
                                }
                                case "enabled" -> {
                                    kit1.setEnabled(!kit1.isEnabled());
                                    commandSender.sendMessage(kit1.isEnabled() ? "§aEnabled " + strings[1] + "!" : "§aDisabled " + strings[1] + "!");
                                }
                                case "editable" -> {
                                    kit1.setEditable(!kit1.isEditable());
                                    commandSender.sendMessage(kit1.isEditable() ? "§aEnabled editable on " + strings[1] + "!" : "§aDisabled editable on " + strings[1] + "!");
                                }
                                case "bedwars" -> {
                                    kit1.setBedwars(!kit1.isBedwars());
                                    commandSender.sendMessage(kit1.isBedwars() ? "§aEnabled bedwars on " + strings[1] + "!" : "§aDisabled bedwars on " + strings[1] + "!");
                                }
                                case "boxing" -> {
                                    kit1.setBoxing(!kit1.isBoxing());
                                    commandSender.sendMessage(kit1.isBoxing() ? "§aEnabled boxing on " + strings[1] + "!" : "§aDisabled boxing on " + strings[1] + "!");
                                }
                                case "nohitdelay" -> {
                                    kit1.setNoHitDelay(!kit1.isNoHitDelay());
                                    commandSender.sendMessage(kit1.isNoHitDelay() ? "§aEnabled no hit delay on " + strings[1] + "!" : "§aDisabled no hit delay on " + strings[1] + "!");
                                }
                                case "blocks" -> {
                                    kit1.setBlocks(!kit1.isBlocks());
                                    commandSender.sendMessage(kit1.isBlocks() ? "§aEnabled break blocks on " + strings[1] + "!" : "§aDisabled break blocks on " + strings[1] + "!");
                                }
                                case "rounds" -> {
                                    if(!NumberUtils.isNumber(strings[3])){
                                        commandSender.sendMessage("§c" + strings[3] + " is not a valid number!");
                                        return false;
                                    }
                                    
                                    kit1.setRounds(Integer.parseInt(strings[3]));
                                    commandSender.sendMessage("§aSet " + strings[1] + "'s rounds to" + strings[3] + "! For no rounds put 0!");
                                }
                                case "maxduration" -> {
                                    if(!NumberUtils.isNumber(strings[3])){
                                        commandSender.sendMessage("§c" + strings[3] + " is not a valid number!");
                                        return false;
                                    }
                                    
                                    kit1.setMaxDurationInSeconds(Integer.parseInt(strings[3]));
                                    commandSender.sendMessage("§aSet " + strings[1] + "'s max duration in seconds to" + strings[3] + "! For no limit put 0!");
                                }
                                case "ffa" -> {
                                    kit1.setFfa(!kit1.isFfa());
                                    commandSender.sendMessage(kit1.isFfa() ? "§aEnabled ffa on " + strings[1] + "!" : "§aDisabled ffa on " + strings[1] + "!");
                                }
                                case "hunger" -> {
                                    kit1.setHunger(!kit1.isHunger());
                                    commandSender.sendMessage(kit1.isHunger() ? "§aEnabled hunger on " + strings[1] + "!" : "§aDisabled hunger on " + strings[1] + "!");
                                }
                                case "deathdrops" -> {
                                    kit1.setDeathDrops(!kit1.isDeathDrops());
                                    commandSender.sendMessage(kit1.isDeathDrops() ? "§aEnabled death drops on " + strings[1] + "!" : "§aDisabled death drops on " + strings[1] + "!");
                                }
                                case "drops" -> {
                                    kit1.setDrop(!kit1.isDrop());
                                    commandSender.sendMessage(kit1.isDrop() ? "§aEnabled drops on " + strings[1] + "!" : "§aDisabled drops on " + strings[1] + "!");
                                }
                                case "falldamage", "falldmg" -> {
                                    kit1.setFallDamage(!kit1.isFallDamage());
                                    commandSender.sendMessage(kit1.isFallDamage() ? "§aEnabled fall damage on " + strings[1] + "!" : "§aDisabled fall damage on " + strings[1] + "!");
                                }
                                case "maxheight" -> {
                                    if(!NumberUtils.isNumber(strings[3])){
                                        commandSender.sendMessage("§c" + strings[3] + " is not a valid number!");
                                        return false;
                                    }
                                    
                                    kit1.setMaxBuildHeight(Integer.parseInt(strings[3]));
                                    commandSender.sendMessage("§aSet " + strings[1] + "'s max height to" + strings[3] + "! For no rounds put 0!");
                                }
                                case "minheight" -> {
                                    if(!NumberUtils.isNumber(strings[3])){
                                        commandSender.sendMessage("§c" + strings[3] + " is not a valid number!");
                                        return false;
                                    }
                                    
                                    kit1.setMinBuildHeight(Integer.parseInt(strings[3]));
                                    commandSender.sendMessage("§aSet " + strings[1] + "'s min height to" + strings[3] + "! For no rounds put 0!");
                                }
                                default -> commandSender.sendMessage("§c" + strings[0] + " is not a valid option!");
                            }
                            return false;
                        }
                    }
                    commandSender.sendMessage("§c" + strings[1] + " is not a valid kit!");
                }
                case "give" -> {
                    for(Kit kit1 : KitManager.KITS){
                        if(kit1.getName().equalsIgnoreCase(strings[1])){
                            kit1.applyKit(player);
                            commandSender.sendMessage("§aGave you kit " + strings[1] + "!");
                            return false;
                        }
                    }
                }
                case "info" -> {
                    for(Kit kit1 : KitManager.KITS){
                        if(kit1.getName().equalsIgnoreCase(strings[1])){
                            commandSender.sendMessage("§aKit: " + kit1 + "!");
                            return false;
                        }
                    }
                }
                default -> commandSender.sendMessage("§c" + strings[0] + " is not a valid option!");
            }
        }else
            commandSender.sendMessage("§cYou don't have permission to run this command!");
        return true;
    }
}
