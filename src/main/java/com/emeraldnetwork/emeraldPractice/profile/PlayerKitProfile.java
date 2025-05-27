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
                deaths = 0,
                kills = 0;
    
    public void incrementElo(double score, double opponentElo){
        if(score == 0)
            return;
        
        double expectedElo = 1 / (1 + Math.pow(10, (opponentElo - elo) / 400));
        
        elo = elo + 20 * (score - expectedElo);
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
    
    public void incrementDeaths(){
        deaths++;
    }
    
    public void incrementKills(){
        kills++;
    }
    
    public int getKd(){
        return kills / deaths;
    }
}
