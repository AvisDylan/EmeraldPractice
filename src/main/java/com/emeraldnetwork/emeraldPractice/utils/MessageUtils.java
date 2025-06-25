/**
 * Created by dylan on 6/24/2025
 */

package com.emeraldnetwork.emeraldPractice.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public final class MessageUtils{
    
    private static int index = 0;
    private static final String[][] messages = { { "", "&7&m-+-----------------&r&2&lEmerald &a&lNetwork&r&7&m----------------+-", "", "&7Have you joined our discord yet? If not join with &2https://dsc.gg/emeraldnetwork&7!", "", "&7&m-+-------------------------------------------------+-", "" },
                                                    { "", "&7&m-+-----------------&r&2&lEmerald &a&lNetwork&r&7&m----------------+-", "", "&7Want to support the server? Then buy something at &2https://www.emerald-network.com/store&7!", "", "&7&m-+-------------------------------------------------+-", "" },
                                                    { "", "&7&m-+-----------------&r&2&lEmerald &a&lNetwork&r&7&m----------------+-", "", "&7Have you checked out our website? If not check it out at &2https://www.emerald-network.com&7!", "", "&7&m-+-------------------------------------------------+-", "" } };
    
    public static void sendMessage(){
        for(String message : messages[index]){
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', message));
        }
        
        if(index < messages.length)
            index++;
        else
            index = 0;
    }
}
