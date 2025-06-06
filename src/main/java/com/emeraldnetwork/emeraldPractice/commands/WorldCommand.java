package com.emeraldnetwork.emeraldPractice.commands;

import com.emeraldnetwork.emeraldPractice.EmeraldPractice;
import com.emeraldnetwork.emeraldPractice.map.WorldUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WorldCommand implements CommandExecutor{
    
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings){
        if(commandSender.hasPermission("emerald.practice")){
            switch(strings[0]){
                case "create" -> {
                    if(strings[1].equalsIgnoreCase("normal")){
                        WorldUtils.createWorld(strings[2]);
                        commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aYou have created a new world called " + strings[2] + "!"));
                    }else if(strings[1].equalsIgnoreCase("void")){
                        WorldUtils.createVoidWorld(strings[2]);
                        commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aYou have created a new void world called " + strings[2] + "!"));
                    }else
                        commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cInvalid option!"));
                }
                case "teleport" -> {
                    Player player = (Player) commandSender;
                    World world = Bukkit.getWorld(strings[1]);
                    
                    if(world == null){
                        commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c" + strings[1] + " is not a valid world!"));
                        return false;
                    }
                    
                    player.teleport(new Location(world, 0, 0, 0));
                    commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aTeleported you to " + strings[1] + "!"));
                }
                case "delete" -> {
                    World world = Bukkit.getWorld(strings[1]);
                    
                    if(world == null){
                        commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c" + strings[1] + " is not a valid world!"));
                        return false;
                    }
                    
                    commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aThere is no point in adding this I can't lie just delete the folder!"));
                }
                case "list" -> {
                    StringBuilder builder = new StringBuilder();
                    
                    Bukkit.getWorlds().forEach(world -> builder.append(world.getName()).append(", "));
                    
                    commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aWorlds: " + builder.substring(0, builder.length() - 2)));
                }
            }
        }else
            commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou don't have permission to run this command!"));
        return true;
    }
}
