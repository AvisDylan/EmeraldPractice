package com.emeraldnetwork.emeraldPractice.team;

import com.emeraldnetwork.emeraldPractice.kit.Kit;
import com.emeraldnetwork.emeraldPractice.player.PlayerData;
import com.emeraldnetwork.emeraldPractice.queue.QueueEntry;
import com.emeraldnetwork.emeraldPractice.utils.MathUtils;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

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
    
    public PlayerData getPlayerAtIndex(int index){
        return new ArrayList<>(players).get(index);
    }
    
    public String getPlayerNames(){
        StringBuilder builder = new StringBuilder();
        
        players.forEach(playerData -> {
            builder.append(Bukkit.getPlayer(playerData.getUuid()).getName()).append(", ");
        });
        
        return builder.substring(0, builder.length() - 2);
    }
    
    public TextComponent getClickablePlayerNames(boolean winner){
        TextComponent base = new TextComponent(ChatColor.RESET + (winner ? ChatColor.GREEN + "Winner/s: " : ChatColor.RED + "Loser/s: "));
        
        Iterator<PlayerData> iterator = players.iterator();
        
        while(iterator.hasNext()){
            PlayerData playerData = iterator.next();
            Player player = Bukkit.getPlayer(playerData.getUuid());
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
