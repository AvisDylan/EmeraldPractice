package com.emeraldnetwork.emeraldPractice.queue;

import com.emeraldnetwork.emeraldPractice.EmeraldPractice;
import com.emeraldnetwork.emeraldPractice.kit.Kit;
import com.emeraldnetwork.emeraldPractice.player.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Objects;

public class QueueEntry{
    
    private PlayerData playerData;
    private Kit kit;
    private boolean ranked;
    private int teamSize, minPing, maxPing, pingIncrement;
    private final int schedulerId;
    private final long startTime;
    
    public QueueEntry(PlayerData playerData, Kit kit, boolean ranked, int teamSize){
        this.playerData = playerData;
        this.kit = kit;
        this.ranked = ranked;
        this.teamSize = teamSize;
        startTime = System.currentTimeMillis();
        pingIncrement = 0;
        schedulerId = Bukkit.getScheduler().runTaskTimer(EmeraldPractice.getPlugin(), () -> {
            Player player = Bukkit.getPlayer(playerData.getUuid());
            
            minPing = Math.max(playerData.getPing() - (playerData.getProfile().getPingRange() / 2) - pingIncrement, 0);
            maxPing = playerData.getPing() + (playerData.getProfile().getPingRange() / 2) + pingIncrement;
            pingIncrement += 5;
            
            player.sendMessage(ChatColor.RESET + "");
            player.sendMessage(ChatColor.RESET + "" + ChatColor.DARK_GREEN + ChatColor.BOLD + kit.getDisplayName());
            player.sendMessage(ChatColor.GRAY + "Ping range: " + ChatColor.DARK_GREEN + minPing + ChatColor.GRAY + " - " + ChatColor.DARK_GREEN + maxPing);
            player.sendMessage(ChatColor.GRAY + "Searching for players...");
            player.sendMessage(ChatColor.RESET + "");
        }, 0L, 200L).getTaskId();
    }
    
    public void cancelTask(){
        Bukkit.getScheduler().cancelTask(schedulerId);
    }
    
    public PlayerData getPlayerData(){
        return playerData;
    }
    
    public void setPlayerData(PlayerData playerData){
        this.playerData = playerData;
    }
    
    public Kit getKit(){
        return kit;
    }
    
    public void setKit(Kit kit){
        this.kit = kit;
    }
    
    public boolean isRanked(){
        return ranked;
    }
    
    public void setRanked(boolean ranked){
        this.ranked = ranked;
    }
    
    public int getTeamSize(){
        return teamSize;
    }
    
    public void setTeamSize(int teamSize){
        this.teamSize = teamSize;
    }
    
    public int getMinPing(){
        return minPing;
    }
    
    public void setMinPing(int minPing){
        this.minPing = minPing;
    }
    
    public int getMaxPing(){
        return maxPing;
    }
    
    public void setMaxPing(int maxPing){
        this.maxPing = maxPing;
    }
    
    public int getSchedulerId(){
        return schedulerId;
    }
    
    public long getStartTime(){
        return startTime;
    }
    
    public boolean isWithinPingRange(PlayerData playerData){
        return playerData.getPing() >= minPing && playerData.getPing() <= maxPing;
    }
    
    @Override
    public boolean equals(Object o){
        if(!(o instanceof QueueEntry that)) return false;
        return isRanked() == that.isRanked() && Objects.equals(getPlayerData(), that.getPlayerData()) && Objects.equals(getKit(), that.getKit());
    }
    
    @Override
    public int hashCode(){
        return Objects.hash(getPlayerData(), getKit(), isRanked());
    }
}
