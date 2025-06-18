package com.emeraldnetwork.emeraldPractice.commands;

import com.emeraldnetwork.emeraldPractice.kit.Kit;
import com.emeraldnetwork.emeraldPractice.kit.KitManager;
import com.emeraldnetwork.emeraldPractice.map.Map;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MapCommand implements CommandExecutor{
    
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings){
        if(commandSender.hasPermission("emerald.practice")){
            Player player = (Player) commandSender;
            
            switch(strings[0]){
                case "create" -> {
                    Kit kit = KitManager.getKit(strings[1]);
                    
                    if(kit != null){
                        Map map = new Map(strings[2], strings[3]);
                        
                        if(player.getItemInHand() != null)
                            map.setIcon(player.getItemInHand());
                        
                        kit.addMap(map);
                        commandSender.sendMessage("§aCreated map " + strings[2] + "!");
                        return false;
                    }
                    
                    commandSender.sendMessage("§c" + strings[1] + " is not a valid kit!");
                }
                case "delete" -> {
                    Kit kit = KitManager.getKit(strings[1]);
                    
                    if(kit != null){
                        for(Map map : kit.getMaps()){
                            if(map.getName().equalsIgnoreCase(strings[2])){
                                kit.removeMap(map);
                                commandSender.sendMessage("§aDeleted map " + strings[2] + "!");
                                return false;
                            }
                        }
                        commandSender.sendMessage("§c" + strings[2] + " is not a map kit!");
                        return false;
                    }else
                        commandSender.sendMessage("§c" + strings[1] + " is not a valid kit!");
                }
                case "copy" -> {
                    Kit sourceKit = KitManager.getKit(strings[1]);
                    
                    if(sourceKit != null){
                        for(Map map : sourceKit.getMaps()){
                            if(map.getName().equalsIgnoreCase(strings[2])){
                                Kit targetKit = KitManager.getKit(strings[3]);
                                
                                if(targetKit == null){
                                    commandSender.sendMessage(ChatColor.RED + strings[3] + " is not a valid kit!");
                                    return false;
                                }
                                
                                targetKit.getMaps().add(map);
                                commandSender.sendMessage(ChatColor.GREEN + "Added map " + map.getName() + " to " + targetKit.getName());
                                break;
                            }
                        }
                    }else
                        commandSender.sendMessage(ChatColor.RED + strings[1] + " is not a valid kit!");
                }
                case "setspawn" -> {
                    for(Kit kit : KitManager.KITS){
                        if(kit.getName().equalsIgnoreCase(strings[1])){
                            for(Map map : kit.getMaps()){
                                commandSender.sendMessage(kit.getMaps() + " " + map.getName() + " " + strings[2]);
                                
                                if(map.getName().equalsIgnoreCase(strings[2])){
                                    if(strings[3].equalsIgnoreCase("player1")){
                                        map.setPlayerOneX(player.getLocation().getX());
                                        map.setPlayerOneY(player.getLocation().getY());
                                        map.setPlayerOneZ(player.getLocation().getZ());
                                        commandSender.sendMessage("§aSet player one spawn on " + strings[1] + " on map" + strings[2] + " to your position!");
                                    }else if(strings[3].equalsIgnoreCase("player2")){
                                        map.setPlayerTwoX(player.getLocation().getX());
                                        map.setPlayerTwoY(player.getLocation().getY());
                                        map.setPlayerTwoZ(player.getLocation().getZ());
                                        commandSender.sendMessage("§aSet player one spawn on " + strings[1] + " on map" + strings[2] + " to your position!");
                                    }else{
                                        commandSender.sendMessage("§c" + strings[1] + " is not a valid option!");
                                    }
                                    return false;
                                }
                            }
                            commandSender.sendMessage("§c" + strings[2] + " is not a map kit!");
                            return false;
                        }
                    }
                }
                case "seticon" -> {
                    for(Kit kit : KitManager.KITS){
                        if(kit.getName().equalsIgnoreCase(strings[1])){
                            for(Map map : kit.getMaps()){
                                if(map.getName().equalsIgnoreCase(strings[2])){
                                    if(player.getItemInHand() != null){
                                        map.setIcon(player.getItemInHand());
                                        commandSender.sendMessage("§aSet icon on " + strings[1] + " on map" + strings[2] + " to the item in your hand!");
                                        return false;
                                    }
                                    commandSender.sendMessage("§cYou have no items in your hand!");
                                    return false;
                                }
                            }
                            commandSender.sendMessage("§c" + strings[2] + " is not a map kit!");
                            return false;
                        }
                    }
                    commandSender.sendMessage("§c" + strings[1] + " is not a valid kit!");
                }
                case "setname" -> {
                    for(Kit kit : KitManager.KITS){
                        if(kit.getName().equalsIgnoreCase(strings[1])){
                            for(Map map : kit.getMaps()){
                                if(map.getName().equalsIgnoreCase(strings[2])){
                                    map.setName(strings[3]);
                                    commandSender.sendMessage("§aSet name on " + strings[1] + " on map" + strings[2] + " to" + strings[3] + "!");
                                    return false;
                                }
                            }
                            commandSender.sendMessage("§c" + strings[2] + " is not a map kit!");
                            return false;
                        }
                    }
                    commandSender.sendMessage("§c" + strings[1] + " is not a valid kit!");
                }
                case "setdisplayname" -> {
                    for(Kit kit : KitManager.KITS){
                        if(kit.getName().equalsIgnoreCase(strings[1])){
                            for(Map map : kit.getMaps()){
                                if(map.getName().equalsIgnoreCase(strings[2])){
                                    map.setDisplayName(strings[3]);
                                    commandSender.sendMessage("§aSet displayname on " + strings[1] + " on map " + strings[2] + " to " + strings[3] + "!");
                                    return false;
                                }
                            }
                            commandSender.sendMessage("§c" + strings[2] + " is not a map kit!");
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
