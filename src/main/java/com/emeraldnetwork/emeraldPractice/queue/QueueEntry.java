package com.emeraldnetwork.emeraldPractice.queue;

import com.emeraldnetwork.emeraldPractice.kit.Kit;
import com.emeraldnetwork.emeraldPractice.player.PlayerData;

import java.util.Objects;

public class QueueEntry{
    
    private PlayerData playerData;
    private Kit kit;
    private boolean ranked;
    
    public QueueEntry(PlayerData playerData, Kit kit, boolean ranked){
        this.playerData = playerData;
        this.kit = kit;
        this.ranked = ranked;
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
