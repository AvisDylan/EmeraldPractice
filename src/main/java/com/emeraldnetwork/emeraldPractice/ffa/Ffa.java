/**
 * Created by dylan on 6/30/2025
 */

package com.emeraldnetwork.emeraldPractice.ffa;

import com.emeraldnetwork.emeraldPractice.EmeraldPractice;
import com.emeraldnetwork.emeraldPractice.player.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.LinkedList;
import java.util.List;

public class Ffa{
    
    private final List<PlayerData> players = new LinkedList<>();
    
    public List<PlayerData> getPlayers(){
        return players;
    }
    
    public void onBlockPlace(Block block){
        Bukkit.getScheduler().runTaskLater(EmeraldPractice.getPlugin(), () -> block.setType(Material.AIR), 200L);
    }
}
