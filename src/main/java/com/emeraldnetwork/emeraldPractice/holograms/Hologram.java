/**
 * Created by dylan on 6/18/2025
 */

package com.emeraldnetwork.emeraldPractice.holograms;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;

public class Hologram{

    private final String[] lines;
    
    public Hologram(String... lines){
        this.lines = lines;
    }
    
    public void spawn(Location location){
        for(String line : lines){
            ArmorStand armorStand = location.getWorld().spawn(location, ArmorStand.class);
            
            armorStand.setVisible(false);
            armorStand.setGravity(false);
            armorStand.setCustomName(ChatColor.translateAlternateColorCodes('&', line)  );
            armorStand.setCustomNameVisible(true);
            location.subtract(0, 0.25, 0);
        }
    }
}
