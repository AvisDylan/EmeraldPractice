/**
 * Created by dylan on 6/20/2025
 */

package com.emeraldnetwork.emeraldPractice.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public final class GuiUtils{
    
    public static Inventory createInventoryWithBorder(Player owner, int size, String title){
        Inventory inventory = Bukkit.createInventory(owner, size, title);
        
        for(int i = 0; i < size; i++){
            if(i < 9 || i > (size - 9) || i % 9 == 0 || i % 9 == 8)
                inventory.setItem(i, ItemUtils.createFillerItem());
        }
        
        return inventory;
    }
}
