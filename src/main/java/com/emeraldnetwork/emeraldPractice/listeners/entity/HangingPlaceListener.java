/**
 * Created by dylan on 6/20/2025
 */

package com.emeraldnetwork.emeraldPractice.listeners.entity;

import org.bukkit.entity.ItemFrame;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.hanging.HangingPlaceEvent;

public class HangingPlaceListener implements Listener{
    
    @EventHandler
    public void onHangingPlace(HangingPlaceEvent event){
        if(event.getEntity() instanceof ItemFrame)
            event.setCancelled(true);
    }
}
