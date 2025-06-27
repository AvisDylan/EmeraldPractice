package com.emeraldnetwork.emeraldPractice.team;

import com.emeraldnetwork.emeraldPractice.kit.Kit;
import com.emeraldnetwork.emeraldPractice.player.PlayerData;
import com.emeraldnetwork.emeraldPractice.utils.MathUtils;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.*;

public class Team{
    
    private final Set<PlayerData> players = new HashSet<>(), alivePlayers = new HashSet<>();
    private final Set<UUID> disconnectedPlayers = new HashSet<>();
    private boolean winningTeam = false;
    
    public Team(List<PlayerData> players){
        this.players.addAll(players);
        alivePlayers.addAll(players);
    }
    
    public Set<UUID> getDisconnectedPlayers(){
        return disconnectedPlayers;
    }
    
    public Set<PlayerData> getAlivePlayers(){
        return alivePlayers;
    }
    
    public Set<PlayerData> getPlayers(){
        return players;
    }
    
    public PlayerData getPlayerAtIndex(int index){
        return new ArrayList<>(players).get(index);
    }
    
    public boolean isWinningTeam(){
        return winningTeam;
    }
    
    public void setWinningTeam(boolean winningTeam){
        this.winningTeam = winningTeam;
    }
    
    public String getPlayerNames(){
        StringBuilder builder = new StringBuilder();
        
        players.forEach(playerData -> builder.append(Bukkit.getOfflinePlayer(playerData.getUuid()).getName()).append(", "));
        
        return builder.substring(0, builder.length() - 2);
    }
    
    public TextComponent getClickablePlayerNames(boolean winner){
        TextComponent base = new TextComponent(ChatColor.RESET + (winner ? ChatColor.GREEN + "Winner/s: " : ChatColor.RED + "Loser/s: "));
        
        Iterator<PlayerData> iterator = players.iterator();
        
        while(iterator.hasNext()){
            PlayerData playerData = iterator.next();
            OfflinePlayer player = Bukkit.getOfflinePlayer(playerData.getUuid());
            TextComponent playerComponent = new TextComponent(net.md_5.bungee.api.ChatColor.DARK_GREEN + player.getName());
            
            playerComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/inventory " + playerData.getUuid()));
            playerComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{ new TextComponent(ChatColor.GRAY + "Click to view") }));
            base.addExtra(playerComponent);
            
            if(iterator.hasNext())
                base.addExtra(new TextComponent(ChatColor.GRAY + ", "));
        }
        
        return base;
    }
    
    public double getAverageElo(Kit kit){
        List<Double> elos = new ArrayList<>();
        
        for(PlayerData playerData : players){
            elos.add(playerData.getProfile().getKitProfile(kit).getElo());
        }
        
        return MathUtils.getMean(elos.stream().mapToDouble(Double::doubleValue).toArray());
    }
    
    @Override
    public boolean equals(Object o){
        if(!(o instanceof Team team)) return false;
        return isWinningTeam() == team.isWinningTeam() && Objects.equals(getPlayers(), team.getPlayers()) && Objects.equals(getAlivePlayers(), team.getAlivePlayers()) && Objects.equals(getDisconnectedPlayers(), team.getDisconnectedPlayers());
    }
    
    @Override
    public int hashCode(){
        return Objects.hash(getPlayers(), getAlivePlayers(), getDisconnectedPlayers(), isWinningTeam());
    }
}
