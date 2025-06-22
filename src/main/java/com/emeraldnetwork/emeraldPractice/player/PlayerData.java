package com.emeraldnetwork.emeraldPractice.player;

import com.emeraldnetwork.emeraldPractice.database.DatabaseManager;
import com.emeraldnetwork.emeraldPractice.duel.DuelRequest;
import com.emeraldnetwork.emeraldPractice.kit.KitManager;
import com.emeraldnetwork.emeraldPractice.party.PartyInviteRequest;
import com.emeraldnetwork.emeraldPractice.profile.PlayerKitProfile;
import com.emeraldnetwork.emeraldPractice.profile.PlayerProfile;
import fr.mrmicky.fastboard.FastBoard;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class PlayerData{
    
    private final UUID uuid;
    private PlayerProfile profile;
    private PlayerState playerState;
    private DuelRequest tempRequest;
    private final FastBoard fastBoard;
    private final List<DuelRequest> duelRequests = new LinkedList<>();
    private final List<PartyInviteRequest> partyRequests = new LinkedList<>();
    
    public PlayerData(UUID uuid){
        this.uuid = uuid;
        this.fastBoard = new FastBoard(Bukkit.getPlayer(uuid));
        PlayerProfile loadedProfile = DatabaseManager.loadPlayerProfile(uuid);
        
        fastBoard.updateTitle(ChatColor.RESET + "" + ChatColor.DARK_GREEN + ChatColor.BOLD + "Emerald " + ChatColor.GREEN + ChatColor.BOLD + "Network");
        
        if(loadedProfile == null){
            profile = new PlayerProfile(uuid);
            
            KitManager.KITS.forEach(kit -> profile.getKitDataList().put(kit.getName(), new PlayerKitProfile()));
        }else
            profile = loadedProfile;
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
    
    public FastBoard getFastBoard(){
        return fastBoard;
    }
    
    public void setProfile(PlayerProfile profile){
        this.profile = profile;
    }
    
    public List<DuelRequest> getDuelRequests(){
        return duelRequests;
    }
    
    public DuelRequest getTempRequest(){
        return tempRequest;
    }
    
    public void setTempRequest(DuelRequest tempRequest){
        this.tempRequest = tempRequest;
    }
    
    public DuelRequest getDuelRequest(PlayerData playerData){
        for(DuelRequest duelRequest : duelRequests){
            if(duelRequest.getSender().equals(playerData))
                return duelRequest;
        }
        
        return null;
    }
    
    public void resetProfile(){
        profile = new PlayerProfile(uuid);
    }
    
    public int getPing(){
        Player player = Bukkit.getPlayer(uuid);
        
        if(player == null)
            return -1;
        
        try{
            Object craftPlayer = player.getClass().getMethod("getHandle").invoke(player);
            
            return (int) craftPlayer.getClass().getField("ping").get(craftPlayer);
        }catch(Exception e){
            Bukkit.getLogger().warning(e.getMessage());
            return -1;
        }
    }
    
    public List<PartyInviteRequest> getPartyRequests(){
        return partyRequests;
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
