package com.emeraldnetwork.emeraldPractice.team;

import com.emeraldnetwork.emeraldPractice.player.PlayerData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TeamAssigner{
    
    private final List<PlayerData> playersToAssign = new ArrayList<>(), teamOne = new ArrayList<>(), teamTwo = new ArrayList<>();
    
    public TeamAssigner(PlayerData... playersToAssign){
        this.playersToAssign.addAll(Arrays.asList(playersToAssign));
    }
    
    public void assignTeams(){
        Collections.shuffle(playersToAssign);
        
        for(int i = 0; i < playersToAssign.size(); i++){
            if(i % 2 == 0)
                teamOne.add(playersToAssign.get(i));
            else
                teamTwo.add(playersToAssign.get(i));
        }
    }
    
    public List<PlayerData> getTeamOne(){
        return teamOne;
    }
    
    public List<PlayerData> getTeamTwo(){
        return teamTwo;
    }
}
