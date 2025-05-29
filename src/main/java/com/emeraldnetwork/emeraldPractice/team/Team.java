package com.emeraldnetwork.emeraldPractice.team;

import com.emeraldnetwork.emeraldPractice.player.PlayerData;

import java.util.*;

public class Team{
    
    private final Set<PlayerData> alivePlayers = new HashSet<>(), deadPlayer = new HashSet<>(), players = new HashSet<>();
    
    public Team(PlayerData... players){
        this.players.addAll(List.of(players));
        alivePlayers.addAll(this.players);
    }
    
    public Set<PlayerData> getAlivePlayers(){
        return alivePlayers;
    }
    
    public Set<PlayerData> getDeadPlayer(){
        return deadPlayer;
    }
    
    public Set<PlayerData> getPlayers(){
        return players;
    }
}
