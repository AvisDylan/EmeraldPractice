package com.emeraldnetwork.emeraldPractice.profile;

import com.google.gson.annotations.Expose;
import org.bukkit.inventory.ItemStack;

public class PlayerKitProfile{
    
    @Expose
    private ItemStack[] kitLayoutItems = null;
    @Expose
    private double elo = 1000;
    @Expose
    private int rankedWins = 0,
                rankedLosses = 0,
                unrankedWins = 0,
                unrankedLosses = 0,
                winstreak = 0,
                deaths = 0,
                kills = 0;
    
    public void increaseElo(double score, double opponentElo){
        double expectedElo = 1 / (1 + Math.pow(10, (opponentElo - elo) / 400));
        
        elo = elo + 32 * (score - expectedElo);
    }
    
    public void incrementRankedWins(){
        rankedWins++;
    }
    
    public void incrementRankedLosses(){
        rankedLosses++;
    }
    
    public void incrementUnrankedWins(){
        unrankedWins++;
    }
    
    public void incrementUnrankedLosses(){
        unrankedLosses++;
    }
    
    public void incrementWinStreak(){
        winstreak++;
    }
    
    public void incrementDeaths(){
        deaths++;
    }
    
    public void incrementKills(){
        kills++;
    }
    
    public void resetWinStreak(){
        winstreak = 0;
    }
    
    public int getKd(){
        return kills / deaths;
    }
    
    public double getElo(){
        return elo;
    }
    
    public int getRankedWins(){
        return rankedWins;
    }
    
    public int getRankedLosses(){
        return rankedLosses;
    }
    
    public int getUnrankedWins(){
        return unrankedWins;
    }
    
    public int getUnrankedLosses(){
        return unrankedLosses;
    }
    
    public int getDeaths(){
        return deaths;
    }
    
    public int getKills(){
        return kills;
    }
    
    public int getWinstreak(){
        return winstreak;
    }
    
    public ItemStack[] getKitLayoutItems(){
        return kitLayoutItems;
    }
    
    public void setKitLayoutItems(ItemStack[] kitLayoutItems){
        this.kitLayoutItems = kitLayoutItems;
    }
    
    public void setElo(double elo){
        this.elo = elo;
    }
    
    public void setRankedWins(int rankedWins){
        this.rankedWins = rankedWins;
    }
    
    public void setRankedLosses(int rankedLosses){
        this.rankedLosses = rankedLosses;
    }
    
    public void setUnrankedWins(int unrankedWins){
        this.unrankedWins = unrankedWins;
    }
    
    public void setUnrankedLosses(int unrankedLosses){
        this.unrankedLosses = unrankedLosses;
    }
    
    public void setWinstreak(int winstreak){
        this.winstreak = winstreak;
    }
    
    public void setDeaths(int deaths){
        this.deaths = deaths;
    }
    
    public void setKills(int kills){
        this.kills = kills;
    }
}
