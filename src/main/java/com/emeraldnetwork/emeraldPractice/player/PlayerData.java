package com.emeraldnetwork.emeraldPractice.player;

import com.emeraldnetwork.emeraldPractice.profile.PlayerProfile;
import org.bukkit.entity.Player;

import java.util.Objects;
import java.util.UUID;

public class PlayerData{
    
    private final UUID uuid;
    private final PlayerProfile profile;
    private PlayerState playerState;
    
    public PlayerData(Player player){
        uuid = player.getUniqueId();
        profile = new PlayerProfile(player);
    }
    
    public UUID getUuid(){
        return uuid;
    }
    
    public PlayerProfile getProfile(){
        return profile;
    }
    
    public PlayerState getPlayerState(){
        return playerState;
    }
    
    public void setPlayerState(PlayerState playerState){
        this.playerState = playerState;
    }
    
    @Override
    public boolean equals(Object o){
        if(!(o instanceof PlayerData that)) return false;
        return Objects.equals(getUuid(), that.getUuid());
    }
    
    @Override
    public int hashCode(){
        return Objects.hashCode(getUuid());
    }
}
