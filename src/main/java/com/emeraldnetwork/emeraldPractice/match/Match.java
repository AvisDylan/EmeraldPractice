package com.emeraldnetwork.emeraldPractice.match;

import com.emeraldnetwork.emeraldPractice.EmeraldPractice;
import com.emeraldnetwork.emeraldPractice.kit.Kit;
import com.emeraldnetwork.emeraldPractice.map.ActiveMap;
import com.emeraldnetwork.emeraldPractice.map.Map;
import com.emeraldnetwork.emeraldPractice.player.PlayerData;
import com.emeraldnetwork.emeraldPractice.player.PlayerManager;
import com.emeraldnetwork.emeraldPractice.player.PlayerState;
import com.emeraldnetwork.emeraldPractice.team.Team;
import com.emeraldnetwork.emeraldPractice.team.TeamAssigner;
import com.emeraldnetwork.emeraldPractice.utils.ArrayUtils;
import com.emeraldnetwork.emeraldPractice.utils.FormatUtils;
import com.emeraldnetwork.emeraldPractice.utils.SpawnPointUtils;
import com.emeraldnetwork.emeraldPractice.utils.WebhookUtils;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Match implements Listener{
    
    private final List<PlayerData> players = new CopyOnWriteArrayList<>(), spectators = new CopyOnWriteArrayList<>();
    private final Set<Block> playerPlacedBlocks = new HashSet<>();
    private final Team teamOne, teamTwo;
    private final Kit kit;
    private final ActiveMap activeMap;
    private final boolean ranked;
    private final long startTime;
    private MatchState matchState;
    private int teamOneHits = 0, teamTwoHits = 0, schedulerId;
    
    public Match(Kit kit, Map map, boolean ranked, PlayerData... players){
        this.kit = kit;
        this.ranked = ranked;
        
        activeMap = new ActiveMap(map);
        
        if(!activeMap.load())
            Bukkit.getLogger().severe("Failed to load map!");
        
        this.players.addAll(List.of(players));
        for(PlayerData playerData : players){
            Player player = Bukkit.getPlayer(playerData.getUuid());
            
            playerData.setPlayerState(PlayerState.DUEL);
            player.sendMessage(ChatColor.RESET + "");
            player.sendMessage(ChatColor.RESET + "" + ChatColor.DARK_GREEN + ChatColor.BOLD + kit.getDisplayName());
            player.sendMessage(ChatColor.GRAY + "Map: " + ChatColor.DARK_GREEN + map.getDisplayName());
            player.sendMessage(ChatColor.GRAY + "Time Left: " + ChatColor.DARK_GREEN + (kit.getMaxDurationInSeconds() == 0 ? "Unlimited" : FormatUtils.formateTime(kit.getMaxDurationInSeconds() * 1000)));
            player.sendMessage(ChatColor.GRAY + "Ranked: " + ChatColor.DARK_GREEN + (ranked ? "Yes" : "No"));
            player.sendMessage(ChatColor.RESET + "");
            kit.applyKit(player);
        }
        
        TeamAssigner teamAssigner = new TeamAssigner(players);
        
        teamAssigner.assignTeams();
        
        teamOne = new Team(teamAssigner.getTeamOne());
        teamTwo = new Team(teamAssigner.getTeamTwo());
        
        for(PlayerData playerData : teamOne.getPlayers()){
            Player player = Bukkit.getPlayer(playerData.getUuid());
            
            Bukkit.getLogger().info(activeMap.getWorld().getName());
            
            player.teleport(new Location(activeMap.getWorld(), map.getPlayerOneX(), map.getPlayerOneY(), map.getPlayerOneZ()));
        }
        
        for(PlayerData playerData : teamTwo.getPlayers()){
            Player player = Bukkit.getPlayer(playerData.getUuid());
            
            player.teleport(new Location(activeMap.getWorld(), map.getPlayerTwoX(), map.getPlayerTwoY(), map.getPlayerTwoZ()));
        }
        
        startTime = System.currentTimeMillis();
        
        MatchManager.ONGOING_MATCHES.add(this);
        
        schedulerId = Bukkit.getScheduler().runTaskTimer(EmeraldPractice.getPlugin(), () -> {
            if(teamOne.getAlivePlayers().isEmpty() || (kit.isBoxing() && teamTwoHits >= 100))
                handleGameEnd(teamTwo, teamOne);
            else if(teamTwo.getAlivePlayers().isEmpty() || (kit.isBoxing() && teamOneHits >= 100))
                handleGameEnd(teamOne, teamTwo);
            
            if((System.currentTimeMillis() - startTime) / 1000 >= kit.getMaxDurationInSeconds() && kit.getMaxDurationInSeconds() > 0)
                handleGameEnd(null, null);
        }, 0L, 10L).getTaskId();
    }
    
    public void handleGameEnd(Team winningTeam, Team losingTeam){
        Bukkit.getScheduler().cancelTask(schedulerId);
        
        if(winningTeam == null && losingTeam == null){
            this.players.forEach(playerData -> {
                Player player = Bukkit.getPlayer(playerData.getUuid());
                
                player.sendTitle(ChatColor.translateAlternateColorCodes('&', "&7Tie!"), ChatColor.translateAlternateColorCodes('&', "&7The game ended in a tie as time ran up!"));
                
                WebhookUtils.sendEmbed("Game ended in a tie!", "Player/s: " + teamTwo.getPlayerNames() + ", " + teamTwo.getPlayerNames(), "919191");
            });
        }else{
            winningTeam.getPlayers().forEach(playerData -> {
                Player player = Bukkit.getPlayer(playerData.getUuid());
                
                player.sendTitle(ChatColor.translateAlternateColorCodes('&', "&aWin!"), ChatColor.translateAlternateColorCodes('&', "&aYour team has won the game, GGs!"));
                
                playerData.getProfile().incrementWinStreak();
                playerData.getProfile().getStats(kit).incrementWinStreak();
                
                if(ranked){
                    playerData.getProfile().getStats(kit).increaseElo(1, losingTeam.getAverageElo(kit));
                    playerData.getProfile().getStats(kit).incrementRankedWins();
                }else{
                    playerData.getProfile().getStats(kit).incrementUnrankedWins();
                }
            });
            
            losingTeam.getPlayers().forEach(playerData -> {
                Player player = Bukkit.getPlayer(playerData.getUuid());
                
                player.sendTitle(ChatColor.translateAlternateColorCodes('&', "&cLost!"), ChatColor.translateAlternateColorCodes('&', "&cYour team has lost the game, better luck next time!"));
                
                playerData.getProfile().resetWinStreak();
                playerData.getProfile().getStats(kit).resetWinStreak();
                
                if(ranked){
                    playerData.getProfile().getStats(kit).increaseElo(0, winningTeam.getAverageElo(kit));
                    playerData.getProfile().getStats(kit).incrementRankedLosses();
                }else{
                    playerData.getProfile().getStats(kit).incrementUnrankedLosses();
                }
            });
            
            WebhookUtils.sendEmbed(
                    ranked ? "Ranked game ended" : "Unranked game ended",
                    "**Kit:** " + kit.getDisplayName() + "\n" +
                            "**Map:** " + activeMap.getMap().getDisplayName() + "\n" +
                            "**Winning player/s:** " + winningTeam.getPlayerNames() + "\n" +
                            "**Losing player/s:** " + losingTeam.getPlayerNames(),
                    "2bad4e"
            );
            
        }
        
        cleanUpMatch();
    }
    
    public void cleanUpMatch(){
        players.forEach(playerData -> {
            Player player = Bukkit.getPlayer(playerData.getUuid());
            
            MatchManager.INVENTORY_MAP.put(playerData.getUuid(), ArrayUtils.reverseArrayVertically(player.getInventory().getContents()));
            
            player.sendMessage(ChatColor.RESET + "");
            player.sendMessage(ChatColor.RESET + "" + ChatColor.DARK_GREEN + ChatColor.BOLD +  "Match Results: " + net.md_5.bungee.api.ChatColor.GRAY + "(click name to view inventory)");
            player.sendMessage(ChatColor.RESET + "");
            player.spigot().sendMessage(teamOne.getClickablePlayerNames(true));
            player.spigot().sendMessage(teamTwo.getClickablePlayerNames(false));
            player.sendMessage(ChatColor.RESET + "");
            player.sendMessage(ChatColor.RESET + "" + ChatColor.DARK_GREEN + ChatColor.BOLD +  "Stats: ");
            if(ranked)
                player.sendMessage(ChatColor.RESET + "" + ChatColor.GRAY + "Elo: " + ChatColor.DARK_GREEN + Math.round(playerData.getProfile().getStats(kit).getElo()));
            player.sendMessage(ChatColor.RESET + "" + ChatColor.GRAY + "Wins: " + ChatColor.DARK_GREEN + (ranked ? playerData.getProfile().getStats(kit).getRankedWins() : playerData.getProfile().getStats(kit).getUnrankedWins()));
            player.sendMessage(ChatColor.RESET + "" + ChatColor.GRAY + "Losses: " + ChatColor.DARK_GREEN + (ranked ? playerData.getProfile().getStats(kit).getRankedLosses() : playerData.getProfile().getStats(kit).getUnrankedLosses()));
            player.sendMessage(ChatColor.RESET + "" + ChatColor.GRAY + "Kills: " + ChatColor.DARK_GREEN + playerData.getProfile().getStats(kit).getKills());
            player.sendMessage(ChatColor.RESET + "" + ChatColor.GRAY + "Deaths: " + ChatColor.DARK_GREEN + playerData.getProfile().getStats(kit).getDeaths());
            player.sendMessage(ChatColor.RESET + "" + ChatColor.GRAY + "K/D: " + ChatColor.DARK_GREEN + playerData.getProfile().getStats(kit).getKd());
            player.sendMessage(ChatColor.RESET + "");
            
            TextComponent playAgainComponent = new TextComponent(ChatColor.RESET + "" + ChatColor.DARK_GREEN + ChatColor.BOLD + "(Play Again)");
            
            playAgainComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/queue join " + (ranked ? "ranked" : "unranked") + " " + kit.getName()));
            player.spigot().sendMessage(playAgainComponent);
            player.sendMessage(ChatColor.RESET + "");
            
            for(PotionEffect potionEffect : player.getActivePotionEffects()){
                player.removePotionEffect(potionEffect.getType());
            }
            
            player.setLevel(0);
            player.setExp(0.0f);
            player.setFireTicks(0);
            player.setFoodLevel(20);
            player.setHealth(player.getMaxHealth());
            playerData.setPlayerState(PlayerState.SPAWN);
            PlayerManager.giveSpawnItems(Bukkit.getPlayer(playerData.getUuid()));
            player.teleport(SpawnPointUtils.getSpawnPoint());
        });
        
        activeMap.cleanUp();
        MatchManager.ONGOING_MATCHES.remove(this);
    }
    
    public void onDeath(PlayerData playerData, PlayerData killData){
        Player player = Bukkit.getPlayer(playerData.getUuid());
        Player killer = Bukkit.getPlayer(killData.getUuid());
        
        playerData.getProfile().getStats(kit).incrementDeaths();
        killData.getProfile().getStats(kit).incrementKills();
        
        getTeam(playerData).getAlivePlayers().remove(playerData);
        getTeam(playerData).getDeadPlayer().add(playerData);
        
        players.forEach(playerData1 -> {
            Player player1 = Bukkit.getPlayer(playerData1.getUuid());
            
            player1.sendMessage(ChatColor.DARK_GREEN + player.getName() + ChatColor.GRAY + " has been killed by " + ChatColor.DARK_GREEN + killer.getName() + ChatColor.GRAY + "!");
        });
    }
    
    public void onLeave(PlayerData playerData){
        Player player = Bukkit.getPlayer(playerData.getUuid());
        
        playerData.getProfile().getStats(kit).incrementDeaths();
        playerData.getProfile().getStats(kit).resetWinStreak();
        playerData.getProfile().resetWinStreak();
        
        getTeam(playerData).getAlivePlayers().remove(playerData);
        getTeam(playerData).getDeadPlayer().add(playerData);
        players.remove(playerData);
        
        for(PotionEffect potionEffect : player.getActivePotionEffects()){
            player.removePotionEffect(potionEffect.getType());
        }
        
        player.setFireTicks(0);
        player.setFoodLevel(20);
        player.setHealth(player.getMaxHealth());
        playerData.setPlayerState(PlayerState.SPAWN);
        PlayerManager.giveSpawnItems(Bukkit.getPlayer(playerData.getUuid()));
        player.teleport(SpawnPointUtils.getSpawnPoint());
        
        if(ranked){
            playerData.getProfile().getStats(kit).increaseElo(0, getOtherTeam(playerData).getAverageElo(kit));
            playerData.getProfile().getStats(kit).incrementRankedLosses();
        }else{
            playerData.getProfile().getStats(kit).incrementUnrankedLosses();
        }
        
        players.forEach(playerData1 -> {
            Player player1 = Bukkit.getPlayer(playerData1.getUuid());
            
            player1.sendMessage(ChatColor.DARK_GREEN + player.getName() + ChatColor.GRAY + " has left!");
        });
    }
    
    public Team getTeam(PlayerData playerData){
        for(PlayerData teamOnePlayer : teamOne.getPlayers()){
            if(teamOnePlayer.equals(playerData))
                return teamOne;
        }
        
        for(PlayerData teamTwoPlayer : teamTwo.getPlayers()){
            if(teamTwoPlayer.equals(playerData))
                return teamTwo;
        }
        
        return null;
    }
    
    public Team getOtherTeam(PlayerData playerData){
        for(PlayerData teamOnePlayer : teamOne.getPlayers()){
            if(teamOnePlayer.equals(playerData))
                return teamTwo;
        }
        
        for(PlayerData teamTwoPlayer : teamTwo.getPlayers()){
            if(teamTwoPlayer.equals(playerData))
                return teamOne;
        }
        
        return null;
    }
    
    public int getTeamHits(PlayerData playerData){
        int teamHits = 0;
        Team playerTeam = getTeam(playerData);
        
        if(playerTeam.equals(teamOne))
            teamHits = teamOneHits;
        else if(playerTeam.equals(teamTwo))
            teamHits = teamTwoHits;
        
        return teamHits;
    }
    
    public int getOtherTeamHits(PlayerData playerData){
        int teamHits = 0;
        Team playerTeam = getOtherTeam(playerData);
        
        if(playerTeam.equals(teamOne))
            teamHits = teamOneHits;
        else if(playerTeam.equals(teamTwo))
            teamHits = teamTwoHits;
        
        return teamHits;
    }
    
    public List<PlayerData> getPlayers(){
        return players;
    }
    
    public Team getTeamOne(){
        return teamOne;
    }
    
    public Team getTeamTwo(){
        return teamTwo;
    }
    
    public Kit getKit(){
        return kit;
    }
    
    public Set<Block> getPlayerPlacedBlocks(){
        return playerPlacedBlocks;
    }
    
    public void incrementTeamOneHits(){
        teamOneHits++;
    }
    
    public void incrementTeamTwoHits(){
        teamTwoHits++;
    }
    
    public int getTeamOneHits(){
        return teamOneHits;
    }
    
    public int getTeamTwoHits(){
        return teamTwoHits;
    }
    
    public ActiveMap getActiveMap(){
        return activeMap;
    }
    
    public boolean isRanked(){
        return ranked;
    }
    
    public long getStartTime(){
        return startTime;
    }
    
    public void setTeamOneHits(int teamOneHits){
        this.teamOneHits = teamOneHits;
    }
    
    public void setTeamTwoHits(int teamTwoHits){
        this.teamTwoHits = teamTwoHits;
    }
    
    public int getSchedulerId(){
        return schedulerId;
    }
    
    public void setSchedulerId(int schedulerId){
        this.schedulerId = schedulerId;
    }
    
    public List<PlayerData> getSpectators(){
        return spectators;
    }
    
    public MatchState getMatchState(){
        return matchState;
    }
    
    public void setMatchState(MatchState matchState){
        this.matchState = matchState;
    }
    
    public int getTeamSize(){
        return Math.max(teamOne.getPlayers().size(), teamTwo.getPlayers().size());
    }
}
