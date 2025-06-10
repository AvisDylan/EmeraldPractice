package com.emeraldnetwork.emeraldPractice.listeners.entity;

import com.emeraldnetwork.emeraldPractice.match.Match;
import com.emeraldnetwork.emeraldPractice.match.MatchManager;
import com.emeraldnetwork.emeraldPractice.match.MatchState;
import com.emeraldnetwork.emeraldPractice.player.PlayerData;
import com.emeraldnetwork.emeraldPractice.player.PlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EntityDamageByEntityListener implements Listener{
    
    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event){
        if(!(event.getEntity() instanceof Player player) || !(event.getDamager() instanceof Player attacker))
            return;
        
        PlayerData playerData = PlayerManager.getPlayerData(player.getUniqueId());
        PlayerData attackerData = PlayerManager.getPlayerData(attacker.getUniqueId());
        
        
        switch(playerData.getPlayerState()){
            case QUEUE, SPAWN, SPECTATING -> {
                    event.setCancelled(true);
            }
            case DUEL -> {
                Match match = MatchManager.getPlayerMatch(playerData);
                
                if(match != null && MatchManager.getPlayerMatch(attackerData) != null){
                    if(match.getMatchState() == MatchState.STARTING || match.getMatchState() == MatchState.ENDING){
                        event.setCancelled(true);
                        return;
                    }
                    
                    //this shit is fucking unreadable ill js put what it checks for it checks if the two players r both on team one or two
                    if((match.getTeamOne().getPlayers().contains(playerData) && match.getTeamOne().getPlayers().contains(attackerData)) ||
                        (match.getTeamTwo().getPlayers().contains(playerData) && match.getTeamTwo().getPlayers().contains(attackerData))){
                        event.setCancelled(true);
                        return;
                    }
                    
                    if(match.getKit().isBoxing()){
                        event.setDamage(0);
                        
                        if(match.getTeamOne().getPlayers().contains(attackerData))
                            match.incrementTeamOneHits();
                        else if(match.getTeamTwo().getPlayers().contains(attackerData))
                            match.incrementTeamTwoHits();
                    }
                    
                    if(player.getHealth() <= 0 || player.isDead() || (player.getHealth() - event.getFinalDamage()) <= 0)
                        match.onDeath(playerData, attackerData);
                }
            }
        }
    }
}
