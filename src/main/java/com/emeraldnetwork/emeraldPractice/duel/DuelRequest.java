/**
 * Created by dylan on 6/10/2025
 */

package com.emeraldnetwork.emeraldPractice.duel;

import com.emeraldnetwork.emeraldPractice.kit.Kit;
import com.emeraldnetwork.emeraldPractice.map.Map;
import com.emeraldnetwork.emeraldPractice.player.PlayerData;

public class DuelRequest{
    
    private PlayerData sender, receiver;
    private Kit kit;
    private Map map;
    
    public DuelRequest(Map map, PlayerData sender, PlayerData receiver){
        this.map = map;
        this.sender = sender;
        this.receiver = receiver;
    }
    
    public PlayerData getSender(){
        return sender;
    }
    
    public void setSender(PlayerData sender){
        this.sender = sender;
    }
    
    public PlayerData getReceiver(){
        return receiver;
    }
    
    public void setReceiver(PlayerData receiver){
        this.receiver = receiver;
    }
    
    public Kit getKit(){
        return kit;
    }
    
    public void setKit(Kit kit){
        this.kit = kit;
    }
    
    public Map getMap(){
        return map;
    }
    
    public void setMap(Map map){
        this.map = map;
    }
}
