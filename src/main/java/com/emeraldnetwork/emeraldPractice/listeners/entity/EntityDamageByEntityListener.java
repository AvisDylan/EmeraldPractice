package com.emeraldnetwork.emeraldPractice.listeners.entity;

import com.emeraldnetwork.emeraldPractice.events.EmeraldPlayerDeathEvent;
import com.emeraldnetwork.emeraldPractice.match.Match;
import com.emeraldnetwork.emeraldPractice.match.MatchManager;
import com.emeraldnetwork.emeraldPractice.player.PlayerData;
import com.emeraldnetwork.emeraldPractice.player.PlayerManager;
import com.emeraldnetwork.emeraldPractice.player.PlayerState;
import com.emeraldnetwork.emeraldPractice.utils.MultithreadedUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EntityDamageByEntityListener implements Listener{
    
    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event){
        if(!(event.getEntity() instanceof Player player) || !(event.getDamager() instanceof Player attacker))
            return;
        
        PlayerData playerData = PlayerManager.getPlayerData(player);
        PlayerData attackerData = PlayerManager.getPlayerData(attacker);
        
        
        switch(playerData.getPlayerState()){
            case QUEUE, SPAWN, SPECTATING -> {
                    event.setCancelled(true);
            }
            case DUEL -> {
                Match match = MatchManager.getPlayerMatch(playerData);
                
                if(match != null && MatchManager.getPlayerMatch(attackerData) != null){
                    if(match.getTeamOne().contains(playerData) && match.getTeamOne().contains(attackerData))
                        event.setCancelled(true);
                    else if(match.getTeamTwo().contains(playerData) && match.getTeamTwo().contains(attackerData))
                        event.setCancelled(true);
                    
                    if(match.getKit().isBoxing()){
                        event.setDamage(0);
                        
                        if(match.getTeamOne().contains(attackerData))
                            match.incrementTeamOneHits();
                        else if(match.getTeamTwo().contains(attackerData))
                            match.incrementTeamTwoHits();
                    }
                    
                    if(player.isDead() || player.getHealth() <= 0)
                        Bukkit.getServer().getPluginManager().callEvent(new EmeraldPlayerDeathEvent(player, attacker, match));
                }
            }
        }
    }
}
