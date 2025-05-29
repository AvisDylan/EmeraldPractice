package com.emeraldnetwork.emeraldPractice.events;

import com.emeraldnetwork.emeraldPractice.match.Match;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class EmeraldPlayerDeathEvent extends Event{
    
    private static final HandlerList HANDLER_LIST = new HandlerList();
    private final Player player;
    private final Player killer;
    private final Match match;
    
    @Override
    public HandlerList getHandlers(){
        return HANDLER_LIST;
    }
    
    public EmeraldPlayerDeathEvent(Player player, Player killer, Match match){
        this.player = player;
        this.killer = killer;
        this.match = match;
    }
    
    public Player getPlayer(){
        return player;
    }
    
    public Player getKiller(){
        return killer;
    }
    
    public Match getMatch(){
        return match;
    }
}
