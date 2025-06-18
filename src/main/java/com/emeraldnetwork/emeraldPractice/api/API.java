package com.emeraldnetwork.emeraldPractice.api;

import com.emeraldnetwork.emeraldPractice.player.PlayerManager;
import com.emeraldnetwork.emeraldPractice.profile.PlayerProfile;

import java.util.UUID;

public class API{

    public boolean isReceiveMessages(UUID uuid){
        PlayerProfile playerProfile = PlayerManager.getPlayerData(uuid).getProfile();
    
        if(playerProfile == null)
            return false;
        
        return playerProfile.isReceiveMessages();
    }
    
    public boolean isMessageSounds(UUID uuid){
        PlayerProfile playerProfile = PlayerManager.getPlayerData(uuid).getProfile();
        
        if(playerProfile == null)
            return false;
        
        return playerProfile.isMessageSounds();
    }
    
    public boolean isGlobalChat(UUID uuid){
        PlayerProfile playerProfile = PlayerManager.getPlayerData(uuid).getProfile();
        
        if(playerProfile == null)
            return false;
        
        return playerProfile.isGlobalChat();
    }
}
