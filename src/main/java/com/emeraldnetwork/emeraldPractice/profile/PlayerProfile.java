package com.emeraldnetwork.emeraldPractice.profile;

import com.emeraldnetwork.emeraldPractice.kit.Kit;
import com.emeraldnetwork.emeraldPractice.kit.KitManager;
import com.emeraldnetwork.emeraldPractice.misc.DeathEffect;
import com.emeraldnetwork.emeraldPractice.utils.MathUtils;
import org.bukkit.WeatherType;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class PlayerProfile{
    
    private final UUID uuid;
    private boolean receiveMessages = true, messageSounds = true, duelRequests = true, duelSounds = true, allowSpectators = true, scoreBoard = true, globalChat = true, partyInvites = true, partySounds = true, editMode = false;
    private WeatherType playerWeather = WeatherType.CLEAR;
    private int pingRange = 200;
    private int playerTime = 0;
    private int winstreak = 0;
    private DeathEffect deathEffect = DeathEffect.NONE;
    private final Map<String, PlayerKitProfile> kitDataList = new HashMap<>();
    
    public PlayerProfile(UUID uuid){
        this.uuid = uuid;
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
    
    public int getPlayerTime(){
        return playerTime;
    }
    
    public void setPlayerTime(int playerTime){
        this.playerTime = playerTime;
    }
    
    public boolean isDuelSounds(){
        return duelSounds;
    }
    
    public void setDuelSounds(boolean duelSounds){
        this.duelSounds = duelSounds;
    }
    
    public boolean isAllowSpectators(){
        return allowSpectators;
    }
    
    public void setAllowSpectators(boolean allowSpectators){
        this.allowSpectators = allowSpectators;
    }
    
    public void setWinstreak(int winstreak){
        this.winstreak = winstreak;
    }
    
    public Map<String, PlayerKitProfile> getKitDataList(){
        return kitDataList;
    }
    
    public boolean isPartyInvites(){
        return partyInvites;
    }
    
    public void setPartyInvites(boolean partyInvites){
        this.partyInvites = partyInvites;
    }
    
    public boolean isPartySounds(){
        return partySounds;
    }
    
    public void setPartySounds(boolean partySounds){
        this.partySounds = partySounds;
    }
    
    public DeathEffect getDeathEffect(){
        return deathEffect;
    }
    
    public void setDeathEffect(DeathEffect deathEffect){
        this.deathEffect = deathEffect;
    }
    
    public PlayerKitProfile getKitProfile(Kit kit){
        return kitDataList.get(kit.getName());
    }
    
    public int getTotalUnrankedWins(){
        AtomicInteger totalWins = new AtomicInteger();
        
        KitManager.KITS.forEach(kit -> totalWins.addAndGet(getKitProfile(kit).getUnrankedWins()));
        
        return totalWins.get();
    }
    
    public int getTotalRankedWins(){
        AtomicInteger totalWins = new AtomicInteger();
        
        KitManager.KITS.forEach(kit -> totalWins.addAndGet(getKitProfile(kit).getRankedWins()));
        
        return totalWins.get();
    }
    
    public int getTotalUnrankedLosses(){
        AtomicInteger totalLosses = new AtomicInteger();
        
        KitManager.KITS.forEach(kit -> totalLosses.addAndGet(getKitProfile(kit).getUnrankedLosses()));
        
        return totalLosses.get();
    }
    
    public int getTotalRankedLosses(){
        AtomicInteger totalLosses = new AtomicInteger();
        
        KitManager.KITS.forEach(kit -> totalLosses.addAndGet(getKitProfile(kit).getRankedLosses()));
        
        return totalLosses.get();
    }
    
    public double getAverageElo(){
        double[] elos = new double[kitDataList.size()];
        Iterator<PlayerKitProfile> iterator = kitDataList.values().iterator();
        int index = 0;
        
        while(iterator.hasNext()){
            PlayerKitProfile kitProfile = iterator.next();
            elos[index] = kitProfile.getElo();
            index++;
        }
        
        return MathUtils.getMean(elos);
    }
    
    public void incrementWinStreak(){
        winstreak++;
    }
    
    public void resetWinStreak(){
        winstreak = 0;
    }
    
    public int getWinstreak(){
        return winstreak;
    }
}
