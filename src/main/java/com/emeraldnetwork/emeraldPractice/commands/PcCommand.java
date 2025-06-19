/**
 * Created by dylan on 6/19/2025
 */

package com.emeraldnetwork.emeraldPractice.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PcCommand implements CommandExecutor{
    
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings){
        StringBuilder builder = new StringBuilder();
        
        for(String string : strings){
            builder.append(string).append(" ");
        }
        
        ((Player) commandSender).chat("/party chat " + builder);
        return true;
    }
}
