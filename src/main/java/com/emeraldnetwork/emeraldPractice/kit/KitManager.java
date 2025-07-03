package com.emeraldnetwork.emeraldPractice.kit;

import com.emeraldnetwork.emeraldPractice.player.PlayerData;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class KitManager{
    
    public static final List<Kit> KITS = new CopyOnWriteArrayList<>();
    
    public static Kit getKit(String name){
        return KITS.stream().filter(kit -> kit.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }
    
    public static Kit getPlayerFfa(PlayerData playerData){
        return KITS.stream().filter(kit -> kit.getFfaPlayers().contains(playerData)).findFirst().orElse(null);
    }
}
