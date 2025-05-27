package com.emeraldnetwork.emeraldPractice.profile;

import com.emeraldnetwork.emeraldPractice.kit.Kit;
import com.emeraldnetwork.emeraldPractice.kit.KitManager;
import com.google.gson.annotations.Expose;
import org.bukkit.Bukkit;
import org.bukkit.WeatherType;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerProfile{
    
    @Expose
    private final UUID uuid;
    @Expose
    private boolean receiveMessages = true, messageSounds = true, duelRequests = true, scoreBoard = true, globalChat = true, editMode = false;
    @Expose
    private WeatherType playerWeather = WeatherType.CLEAR;
    @Expose
    private int pingRange = 200;
    @Expose
    private long playerTime = 0;
    @Expose
    private final Map<Kit, PlayerKitProfile> kitDataList = new HashMap<>();
    
    public PlayerProfile(Player player){
        uuid = player.getUniqueId();
        for(Kit kit : KitManager.KITS){
            kitDataList.put(kit, new PlayerKitProfile());
        }
    }
    
    public UUID getUuid(){
        return uuid;
    }
    
    public boolean isReceiveMessages(){
        return receiveMessages;
    }
    
    public void setReceiveMessages(boolean receiveMessages){
        this.receiveMessages = receiveMessages;
    }
    
    public boolean isMessageSounds(){
        return messageSounds;
    }
    
    public void setMessageSounds(boolean messageSounds){
        this.messageSounds = messageSounds;
    }
    
    public boolean isDuelRequests(){
        return duelRequests;
    }
    
    public void setDuelRequests(boolean duelRequests){
        this.duelRequests = duelRequests;
    }
    
    public boolean isScoreBoard(){
        return scoreBoard;
    }
    
    public void setScoreBoard(boolean scoreBoard){
        this.scoreBoard = scoreBoard;
    }
    
    public boolean isGlobalChat(){
        return globalChat;
    }
    
    public void setGlobalChat(boolean globalChat){
        this.globalChat = globalChat;
    }
    
    public boolean isEditMode(){
        return editMode;
    }
    
    public void setEditMode(boolean editMode){
        this.editMode = editMode;
    }
    
    public WeatherType getPlayerWeather(){
        return playerWeather;
    }
    
    public void setPlayerWeather(WeatherType playerWeather){
        this.playerWeather = playerWeather;
    }
    
    public int getPingRange(){
        return pingRange;
    }
    
    public void setPingRange(int pingRange){
        this.pingRange = pingRange;
    }
    
    public long getPlayerTime(){
        return playerTime;
    }
    
    public void setPlayerTime(long playerTime){
        this.playerTime = playerTime;
    }
    
    public PlayerKitProfile getStats(Kit kit){
        return kitDataList.get(kit);
    }
}
