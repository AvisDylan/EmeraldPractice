/**
 * Created by dylan on 7/29/2025
 */

package com.emeraldnetwork.emeraldPractice.listeners.entity;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class PlayerTeleportListener implements Listener{
    
    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event){
        if(event.getCause() != PlayerTeleportEvent.TeleportCause.ENDER_PEARL)
            return;
        
        Location location = event.getTo();
        Block block = location.getBlock();
        BlockFace[] blockFaces = { BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST };
        
        for(BlockFace blockFace : blockFaces){
            Block adjBlock = block.getRelative(blockFace);
            
            if(adjBlock.getType().isSolid()){
                location.setX(location.getBlockX() + 0.5D);
                location.setZ(location.getBlockZ() + 0.5D);
                event.setTo(location);
                break;
            }
        }
    }
}
