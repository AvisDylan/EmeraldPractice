package com.emeraldnetwork.emeraldPractice.team;

import com.emeraldnetwork.emeraldPractice.kit.Kit;
import com.emeraldnetwork.emeraldPractice.player.PlayerData;
import com.emeraldnetwork.emeraldPractice.utils.MathUtils;
import org.bukkit.Bukkit;

import java.util.*;

public class Team{
    
    private final Set<PlayerData> alivePlayers = new HashSet<>(), deadPlayer = new HashSet<>(), players = new HashSet<>();
    
    public Team(List<PlayerData> players){
        this.players.addAll(players);
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
    
    public String getPlayerNames(){
        StringBuilder builder = new StringBuilder();
        
        players.forEach(playerData -> {
            builder.append(Bukkit.getPlayer(playerData.getUuid()).getName()).append(", ");
        });
        
        return builder.substring(0, builder.length() - 2);
    }
    
    public double getAverageElo(Kit kit){
        List<Double> elos = new ArrayList<>();
        
        for(PlayerData playerData : players){
            elos.add(playerData.getProfile().getStats(kit).getElo());
        }
        
        return MathUtils.getMean(elos.stream().mapToDouble(Double::doubleValue).toArray());
    }
    
    @Override
    public boolean equals(Object o){
        if(!(o instanceof Team team)) return false;
        return Objects.equals(getAlivePlayers(), team.getAlivePlayers()) && Objects.equals(getDeadPlayer(), team.getDeadPlayer()) && Objects.equals(getPlayers(), team.getPlayers());
    }
    
    @Override
    public int hashCode(){
        return Objects.hash(getAlivePlayers(), getDeadPlayer(), getPlayers());
    }
}
