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
import com.emeraldnetwork.emeraldPractice.utils.*;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import xyz.krypton.spigot.knockback.KnockbackAPI;

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
    private int teamOneHits = 0, teamTwoHits = 0, hitsToWin, schedulerId;
    
    public Match(Kit kit, Map map, boolean ranked, PlayerData... players){
        this.kit = kit;
        this.ranked = ranked;
        
        activeMap = new ActiveMap(map);
        
        if(!activeMap.load())
            Bukkit.getLogger().severe("Failed to load map!");
        
        this.players.addAll(List.of(players));
        this.players.forEach(playerData -> {
            Player player = Bukkit.getPlayer(playerData.getUuid());
            
            playerData.setPlayerState(PlayerState.DUEL);
            player.sendMessage(ChatColor.RESET + "");
            player.sendMessage(ChatColor.RESET + "" + ChatColor.DARK_GREEN + ChatColor.BOLD + kit.getDisplayName());
            player.sendMessage(ChatColor.GRAY + "Map: " + ChatColor.DARK_GREEN + map.getDisplayName());
            player.sendMessage(ChatColor.GRAY + "Time Left: " + ChatColor.DARK_GREEN + (kit.getMaxDurationInSeconds() == 0 ? "Unlimited" : FormatUtils.formateTime(kit.getMaxDurationInSeconds() * 1000)));
            player.sendMessage(ChatColor.GRAY + "Ranked: " + ChatColor.DARK_GREEN + (ranked ? "Yes" : "No"));
            player.sendMessage(ChatColor.RESET + "");
            kit.applyKit(player);
            KnockbackAPI.setPlayerProfile(playerData.getUuid(), kit.getKbProfile());
            
            /*if(kit.isNoHitDelay())
                player.setMaximumNoDamageTicks(4);*/
            
            player.setSaturation(20.0f);
        });
        
        TeamAssigner teamAssigner = new TeamAssigner(players);
        
        teamAssigner.assignTeams();
        
        teamOne = new Team(teamAssigner.getTeamOne());
        teamTwo = new Team(teamAssigner.getTeamTwo());
        
        teamOne.getPlayers().forEach(playerData -> {
            Player player = Bukkit.getPlayer(playerData.getUuid());
            
            player.teleport(new Location(activeMap.getWorld(), map.getPlayerOneX(), map.getPlayerOneY(), map.getPlayerOneZ(), map.getPlayerOneYaw(), map.getPlayerOnePitch()));
        });
        
        teamTwo.getPlayers().forEach(playerData -> {
            Player player = Bukkit.getPlayer(playerData.getUuid());
            
            player.teleport(new Location(activeMap.getWorld(), map.getPlayerTwoX(), map.getPlayerTwoY(), map.getPlayerTwoZ(), map.getPlayerTwoYaw(), map.getPlayerTwoPitch()));
        });
        
        hitsToWin = getTeamSize() * 100;
        startTime = System.currentTimeMillis();
        matchState = MatchState.STARTING;
        
        MatchManager.ONGOING_MATCHES.add(this);
        
        schedulerId = Bukkit.getScheduler().runTaskTimer(EmeraldPractice.getPlugin(), () -> {
            int timeBeforeStart = Math.toIntExact((System.currentTimeMillis() - startTime) / 1000);
            
            if(timeBeforeStart <= 5){
                this.players.forEach(playerData -> {
                    Player player = Bukkit.getPlayer(playerData.getUuid());
                    
                    player.playSound(player.getLocation(), Sound.NOTE_PLING, 1.0f, 1.0f);
                    player.sendMessage(ChatColor.GRAY + "Starting in " + ChatColor.DARK_GREEN + (5 - timeBeforeStart) + ChatColor.GRAY + " seconds!");
                });
            }else{
                this.players.forEach(playerData -> {
                    Player player = Bukkit.getPlayer(playerData.getUuid());
                    
                    player.playSound(player.getLocation(), Sound.FIREWORK_LARGE_BLAST, 1.0f, 1.0f);
                    player.sendMessage(ChatColor.DARK_GREEN + "Game Started!");
                });
                
                teamOne.getPlayers().forEach(playerData -> {
                    Player player = Bukkit.getPlayer(playerData.getUuid());
                    
                    player.teleport(new Location(activeMap.getWorld(), map.getPlayerOneX(), map.getPlayerOneY(), map.getPlayerOneZ(), map.getPlayerOneYaw(), map.getPlayerOnePitch()));
                });
                
                teamTwo.getPlayers().forEach(playerData -> {
                    Player player = Bukkit.getPlayer(playerData.getUuid());
                    
                    player.teleport(new Location(activeMap.getWorld(), map.getPlayerTwoX(), map.getPlayerTwoY(), map.getPlayerTwoZ(), map.getPlayerTwoYaw(), map.getPlayerTwoPitch()));
                });
                
                matchState = MatchState.ONGOING;
                Bukkit.getScheduler().cancelTask(schedulerId);
                
                schedulerId = Bukkit.getScheduler().runTaskTimer(EmeraldPractice.getPlugin(), () -> {
                    if(teamOne.getAlivePlayers().isEmpty() || (kit.isBoxing() && teamTwoHits >= hitsToWin))
                        handleGameEnd(teamTwo, teamOne);
                    else if(teamTwo.getAlivePlayers().isEmpty() || (kit.isBoxing() && teamOneHits >= hitsToWin))
                        handleGameEnd(teamOne, teamTwo);
                    
                    if((System.currentTimeMillis() - startTime) / 1000 >= kit.getMaxDurationInSeconds() && kit.getMaxDurationInSeconds() > 0){
                        if(kit.isBoxing()){
                            if(teamOneHits > teamTwoHits)
                                handleGameEnd(teamOne, teamTwo);
                            else if(teamTwoHits > teamOneHits)
                                handleGameEnd(teamTwo, teamOne);
                        }else{
                            if(teamOne.getAlivePlayers().size() > teamTwo.getAlivePlayers().size())
                                handleGameEnd(teamOne, teamTwo);
                            else if(teamTwo.getAlivePlayers().size() > teamOne.getAlivePlayers().size())
                                handleGameEnd(teamTwo, teamOne);
                        }
                    }
                }, 0L, 10L).getTaskId();
            }
        }, 0L, 20L).getTaskId();
    }
    
    public void handleGameEnd(Team winningTeam, Team losingTeam){
        Bukkit.getScheduler().cancelTask(schedulerId);
        
        winningTeam.getPlayers().forEach(playerData -> {
            if(winningTeam.equals(teamOne))
                teamOne.setWinningTeam(true);
            else if(winningTeam.equals(teamTwo))
                teamTwo.setWinningTeam(true);
            
            Player player = Bukkit.getPlayer(playerData.getUuid());
            
            player.sendTitle(ChatColor.translateAlternateColorCodes('&', "&aWin!"), ChatColor.translateAlternateColorCodes('&', "&aYour team has won the game, GGs!"));
            
            playerData.getProfile().incrementWinStreak();
            playerData.getProfile().getKitProfile(kit).incrementWinStreak();
            
            if(ranked){
                playerData.getProfile().getKitProfile(kit).increaseElo(1, losingTeam.getAverageElo(kit));
                playerData.getProfile().getKitProfile(kit).incrementRankedWins();
            }else{
                playerData.getProfile().getKitProfile(kit).incrementUnrankedWins();
            }
        });
        
        losingTeam.getPlayers().forEach(playerData -> {
            Player player = Bukkit.getPlayer(playerData.getUuid());
            
            player.sendTitle(ChatColor.translateAlternateColorCodes('&', "&cLost!"), ChatColor.translateAlternateColorCodes('&', "&cYour team has lost the game, better luck next time!"));
            
            playerData.getProfile().resetWinStreak();
            playerData.getProfile().getKitProfile(kit).resetWinStreak();
            
            if(ranked){
                playerData.getProfile().getKitProfile(kit).increaseElo(0, winningTeam.getAverageElo(kit));
                playerData.getProfile().getKitProfile(kit).incrementRankedLosses();
            }else{
                playerData.getProfile().getKitProfile(kit).incrementUnrankedLosses();
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
        
        cleanUpMatch();
    }
    
    public void cleanUpMatch(){
        matchState = MatchState.ENDING;
        
        spectators.forEach(spectatorData -> {
            Player player = Bukkit.getPlayer(spectatorData.getUuid());
            
            player.sendMessage(ChatColor.RESET + "");
            player.sendMessage(ChatColor.RESET + "" + ChatColor.DARK_GREEN + ChatColor.BOLD +  "Match Results: " + net.md_5.bungee.api.ChatColor.GRAY + "(click name to view inventory)");
            player.sendMessage(ChatColor.RESET + "");
            player.spigot().sendMessage(getWinningTeam().getClickablePlayerNames(true));
            player.spigot().sendMessage(getLosingTeam().getClickablePlayerNames(false));
            player.sendMessage(ChatColor.RESET + "");
        });
        
        players.forEach(playerData -> {
            Player player = Bukkit.getPlayer(playerData.getUuid());
            
            MatchManager.INVENTORY_MAP.put(playerData.getUuid(), ArrayUtils.reverseArrayVertically(player.getInventory().getContents()));
            
            if(getTeamSize() == 1 && kit.isBoxing())
                DeathEffectUtils.playDeathEffect(playerData, getOtherTeam(playerData).getPlayerAtIndex(0));
            
            player.sendMessage(ChatColor.RESET + "");
            player.sendMessage(ChatColor.RESET + "" + ChatColor.DARK_GREEN + ChatColor.BOLD +  "Match Results: " + net.md_5.bungee.api.ChatColor.GRAY + "(click name to view inventory)");
            player.sendMessage(ChatColor.RESET + "");
            player.spigot().sendMessage(getWinningTeam().getClickablePlayerNames(true));
            player.spigot().sendMessage(getLosingTeam().getClickablePlayerNames(false));
            player.sendMessage(ChatColor.RESET + "");
            player.sendMessage(ChatColor.RESET + "" + ChatColor.GRAY + "Spectators: " + ChatColor.DARK_GREEN + spectators.size()    );
            player.sendMessage(ChatColor.RESET + "");
            player.sendMessage(ChatColor.RESET + "" + ChatColor.DARK_GREEN + ChatColor.BOLD + "Stats: ");
            if(ranked)
                player.sendMessage(ChatColor.RESET + "" + ChatColor.GRAY + "Elo: " + ChatColor.DARK_GREEN + Math.round(playerData.getProfile().getKitProfile(kit).getElo()));
            player.sendMessage(ChatColor.RESET + "" + ChatColor.GRAY + "Global Winstreak: " + ChatColor.DARK_GREEN + playerData.getProfile().getWinstreak());
            player.sendMessage(ChatColor.RESET + "" + ChatColor.GRAY + "Kit Winsteak: " + ChatColor.DARK_GREEN + playerData.getProfile().getKitProfile(kit).getWinstreak());
            player.sendMessage(ChatColor.RESET + "");
            
            TextComponent playAgainComponent = new TextComponent(ChatColor.RESET + "" + ChatColor.DARK_GREEN + ChatColor.BOLD + "(Play Again)");
            
            playAgainComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/queue join " + (ranked ? "ranked" : "unranked") + " " + kit.getName()));
            playAgainComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{ new TextComponent(ChatColor.GRAY + "Click to play again") }));
            player.spigot().sendMessage(playAgainComponent);
            player.sendMessage(ChatColor.RESET + "");
            
            for(PotionEffect potionEffect : player.getActivePotionEffects()){
                player.removePotionEffect(potionEffect.getType());
            }
            
            player.setSaturation(20.0f);
            player.setLevel(0);
            player.setExp(0.0f);
            player.setFireTicks(0);
            player.setFoodLevel(20);
            player.setHealth(player.getMaxHealth());
            //player.setMaximumNoDamageTicks(20);
        });
        
        Bukkit.getScheduler().runTaskLater(EmeraldPractice.getPlugin(), () -> {
            spectators.forEach(spectatorData -> {
                Player player = Bukkit.getPlayer(spectatorData.getUuid());
                
                player.setAllowFlight(false);
                spectatorData.setPlayerState(PlayerState.SPAWN);
                PlayerManager.giveSpawnItems(Bukkit.getPlayer(spectatorData.getUuid()));
                player.teleport(SpawnPointUtils.getSpawnPoint());
            });
            
            players.forEach(playerData -> {
                Player player = Bukkit.getPlayer(playerData.getUuid());
                
                playerData.setPlayerState(PlayerState.SPAWN);
                PlayerManager.giveSpawnItems(Bukkit.getPlayer(playerData.getUuid()));
                player.teleport(SpawnPointUtils.getSpawnPoint());
                
                spectators.forEach(spectatorData -> {
                    Player spectator = Bukkit.getPlayer(spectatorData.getUuid());
                    
                    player.showPlayer(spectator);
                });
            });
            
            activeMap.cleanUp();
            MatchManager.ONGOING_MATCHES.remove(this);
        }, 100L);
    }
    
    public void onDeath(PlayerData playerData){
        Player player = Bukkit.getPlayer(playerData.getUuid());
        
        getTeam(playerData).getAlivePlayers().remove(playerData);
        getTeam(playerData).getDeadPlayers().add(playerData);
        
        if(player.getKiller() != null){
            players.forEach(playerData1 -> {
                Player player1 = Bukkit.getPlayer(playerData1.getUuid());
                
                player1.sendMessage(ChatColor.DARK_GREEN + player.getName() + ChatColor.GRAY + " has been killed by " + ChatColor.DARK_GREEN + player.getKiller().getName() + ChatColor.GRAY + "!");
            });
        }else{
            players.forEach(playerData1 -> {
                Player player1 = Bukkit.getPlayer(playerData1.getUuid());
                
                player1.sendMessage(ChatColor.DARK_GREEN + player.getName() + ChatColor.GRAY + " has died!");
            });
        }
    }
    
    public void onForfeit(PlayerData playerData){
        Player player = Bukkit.getPlayer(playerData.getUuid());
        
        playerData.getProfile().getKitProfile(kit).resetWinStreak();
        playerData.getProfile().resetWinStreak();
        
        getTeam(playerData).getAlivePlayers().remove(playerData);
        getTeam(playerData).getDeadPlayers().add(playerData);
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
        
        spectators.forEach(spectatorData -> {
            Player spectator = Bukkit.getPlayer(spectatorData.getUuid());
            
            player.showPlayer(spectator);
        });
        
        if(ranked){
            playerData.getProfile().getKitProfile(kit).increaseElo(0, getOtherTeam(playerData).getAverageElo(kit));
            playerData.getProfile().getKitProfile(kit).incrementRankedLosses();
        }else{
            playerData.getProfile().getKitProfile(kit).incrementUnrankedLosses();
        }
        
        players.forEach(playerData1 -> {
            Player player1 = Bukkit.getPlayer(playerData1.getUuid());
            
            player1.sendMessage(ChatColor.DARK_GREEN + player.getName() + ChatColor.GRAY + " has left!");
        });
    }
    
    public void onLeave(PlayerData playerData){
        Player player = Bukkit.getPlayer(playerData.getUuid());
        
        playerData.getProfile().getKitProfile(kit).resetWinStreak();
        playerData.getProfile().resetWinStreak();
        
        getTeam(playerData).getAlivePlayers().remove(playerData);
        getTeam(playerData).getPlayers().remove(playerData);
        getTeam(playerData).getDeadPlayers().add(playerData);
        players.remove(playerData);
        
        if(ranked){
            playerData.getProfile().getKitProfile(kit).increaseElo(0, getOtherTeam(playerData).getAverageElo(kit));
            playerData.getProfile().getKitProfile(kit).incrementRankedLosses();
        }else{
            playerData.getProfile().getKitProfile(kit).incrementUnrankedLosses();
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
    
    public Team getWinningTeam(){
        if(teamOne.isWinningTeam())
            return teamOne;
        else if(teamTwo.isWinningTeam())
            return teamTwo;
        
        return null;
    }
    
    public Team getLosingTeam(){
        if(teamOne.isWinningTeam())
            return teamTwo;
        else if(teamTwo.isWinningTeam())
            return teamOne;
        
        return null;
    }
}
