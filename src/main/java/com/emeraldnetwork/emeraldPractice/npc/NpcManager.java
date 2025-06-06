package com.emeraldnetwork.emeraldPractice.npc;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class NpcManager{
    
    public static Player generateOfflinePlayerNPC(Player player){
        NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, player.getName());
        
        npc.spawn(player.getLocation());
        
        return (Player) npc.getEntity();
    }
}
