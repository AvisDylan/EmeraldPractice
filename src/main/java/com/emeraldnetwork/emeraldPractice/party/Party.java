/**
 * Created by dylan on 6/11/2025
 */

package com.emeraldnetwork.emeraldPractice.party;

import com.emeraldnetwork.emeraldPractice.player.PlayerData;

import java.util.LinkedList;
import java.util.Queue;

public class Party{
    
    private PlayerData partyLeader;
    private final Queue<PlayerData> players = new LinkedList<>();
    
    public Party(PlayerData partyLeader){
        this.partyLeader = partyLeader;
    }
    
    public void joinParty(PlayerData playerData){
    
    }
    
    public void leaveParty(PlayerData playerData){
    
    }
    
    public PlayerData getPartyLeader(){
        return partyLeader;
    }
    
    public void setPartyLeader(PlayerData partyLeader){
        this.partyLeader = partyLeader;
    }
    
    public Queue<PlayerData> getPlayers(){
        return players;
    }
}
