package com.emeraldnetwork.emeraldPractice.profile;

import com.google.gson.annotations.Expose;

public class PlayerKitProfile{
    
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
}
