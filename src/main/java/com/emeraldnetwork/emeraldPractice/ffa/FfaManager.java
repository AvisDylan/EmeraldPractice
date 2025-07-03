/**
 * Created by dylan on 6/30/2025
 */

package com.emeraldnetwork.emeraldPractice.ffa;

import com.emeraldnetwork.emeraldPractice.kit.Kit;
import com.emeraldnetwork.emeraldPractice.kit.KitManager;
import com.emeraldnetwork.emeraldPractice.player.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public final class FfaManager{
    
    public static final Map<Kit, Ffa> FFAS = new HashMap<>();
    
    public static void init(){
        for(Kit kit : KitManager.KITS){
            if(!kit.isEnabled())
                continue;
            
            if(!kit.isFfa())
                continue;
            
            FFAS.put(kit, new Ffa());
        }
    }
    
    public static void joinFfa(PlayerData playerData, Kit kit){
        Player player = Bukkit.getPlayer(playerData.getUuid());
        
        player.teleport(kit.getFfaLocation());
        getKitFfa(kit).getPlayers().add(playerData);
    }
    
    public static void forfeitFfa(PlayerData playerData){
    
    }
    
    public static void leaveFfa(PlayerData playerData){
    
    }
    
    public static Ffa getKitFfa(Kit kit){
        for(Map.Entry<Kit, Ffa> ffa : FFAS.entrySet()){
            if(ffa.getKey().equals(kit))
                return ffa.getValue();
        }
        
        return null;
    }
    
    public static Ffa getPlayerFfa(PlayerData playerData){
        return FFAS.values().stream().filter(ffa -> ffa.getPlayers().contains(playerData)).findFirst().orElse(null);
    }
}
