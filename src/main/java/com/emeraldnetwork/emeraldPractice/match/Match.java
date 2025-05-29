package com.emeraldnetwork.emeraldPractice.match;

import com.emeraldnetwork.emeraldPractice.EmeraldPractice;
import com.emeraldnetwork.emeraldPractice.kit.Kit;
import com.emeraldnetwork.emeraldPractice.map.Map;
import com.emeraldnetwork.emeraldPractice.player.PlayerData;
import com.emeraldnetwork.emeraldPractice.player.PlayerManager;
import com.emeraldnetwork.emeraldPractice.player.PlayerState;
import com.emeraldnetwork.emeraldPractice.team.Team;
import com.emeraldnetwork.emeraldPractice.utils.MultithreadedUtils;
import com.emeraldnetwork.emeraldPractice.utils.SpawnPointUtils;
import com.emeraldnetwork.emeraldPractice.utils.WorldUtils;
import com.emeraldnetwork.emeraldPractice.utils.WorldEditUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class Match implements Listener{
    
    private final List<PlayerData> players = new CopyOnWriteArrayList<>();
    private final Set<Block> playerPlacedBlocks = new HashSet<>();
    private final Kit kit;
    private final Map map;
    private final World world;
    private final long startTime;
    private int teamOneHits = 0, teamTwoHits = 0;
    private Team teamOne, teamTwo;
    
    public Match(Kit kit, Map map, PlayerData... players){
        world = WorldUtils.createVoidWorld();
        this.kit = kit;
        this.map = map;
        
        WorldEditUtils.loadMap(map.getMapSchematic(), world);
        this.players.addAll(List.of(players));
        for(PlayerData playerData : players){
            Player player = Bukkit.getPlayer(playerData.getUuid());
            
            playerData.setPlayerState(PlayerState.DUEL);
            player.sendMessage("§aFound match!");
            player.sendTitle("§aStarting Game!", "§cHave fun!");
            kit.applyKit(player);
        }
        
        for(int i = 0; i < this.players.size(); i++){
        
        }
        
        Iterator iterator = this.players.iterator();
        int i = 0;
        
        while(iterator.hasNext()){
            PlayerData playerData = (PlayerData) iterator.next();
            
            if(i % 2 == 0)
                teamOne.add(playerData);
            else
                teamTwo.add(playerData);
            
            i++;
        }
        
        for(PlayerData playerData : teamOne){
            Player player = Bukkit.getPlayer(playerData.getUuid());
            
            //player.teleport(new Location(world, map.getPlayerOneX(), map.getPlayerOneY(), map.getPlayerOneZ()));
        }
        
        for(PlayerData playerData : teamTwo){
            Player player = Bukkit.getPlayer(playerData.getUuid());
            
            //player.teleport(new Location(world, map.getPlayerTwoX(), map.getPlayerTwoY(), map.getPlayerTwoZ()));
        }
        
        startTime = System.currentTimeMillis();
        
        MatchManager.ONGOING_MATCHES.add(this);
        
        if(kit.getMaxDurationInSeconds() != 0){
            Bukkit.getScheduler().runTaskTimerAsynchronously(EmeraldPractice.getPlugin(), () -> {
                MultithreadedUtils.EXECUTOR_SERVICE.submit(() -> {
                    if(System.currentTimeMillis() - startTime * 1000 >= kit.getMaxDurationInSeconds())
                        endMatch();
                });
            }, 0L, 10L);
        }
    }
    
    public void onDeath(PlayerData playerData, PlayerData killData){
        playerData.getProfile().getStats(kit).incrementDeaths();
        killData.getProfile().getStats(kit).incrementKills();
    }
    
    public void onLeave(PlayerData playerData){
    
    }
    
    public void endMatch(){
        players.forEach(playerData -> {
            playerData.setPlayerState(PlayerState.SPAWN);
            PlayerManager.giveSpawnItems(Bukkit.getPlayer(playerData.getUuid()));
            Bukkit.getPlayer(playerData.getUuid()).teleport(SpawnPointUtils.getSpawnPoint());
        });
        
        Bukkit.unloadWorld(world, false);
        MatchManager.ONGOING_MATCHES.remove(this);
    }
    
    public Set<PlayerData> getPlayers(){
        return players;
    }
    
    public Set<PlayerData> getTeamOne(){
        return teamOne;
    }
    
    public Set<PlayerData> getTeamTwo(){
        return teamTwo;
    }
    
    public Kit getKit(){
        return kit;
    }
    
    public Map getMap(){
        return map;
    }
    
    public World getWorld(){
        return world;
    }
    
    public long getStartTime(){
        return startTime;
    }
    
    public Set<Block> getPlayerPlacedBlocks(){
        return playerPlacedBlocks;
    }
    
    public void incrementTeamOneHits(){
        teamOneHits++;
    }
    
    public void incrementTeamTwoHits(){
        teamOneHits++;
    }
    
    public int getTeamOneHits(){
        return teamOneHits;
    }
    
    public int getTeamTwoHits(){
        return teamTwoHits;
    }
    
    public Set<PlayerData> getTeam(PlayerData playerData){
        for(PlayerData teamOnePlayer : teamOne){
            if(teamOnePlayer.equals(playerData)){
                return teamOne;
            }
        }
        
        return null;
    }
}
