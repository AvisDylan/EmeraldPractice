package com.emeraldnetwork.emeraldPractice.match;

import com.emeraldnetwork.emeraldPractice.kit.Kit;
import com.emeraldnetwork.emeraldPractice.map.Map;
import com.emeraldnetwork.emeraldPractice.player.PlayerData;
import com.emeraldnetwork.emeraldPractice.player.PlayerState;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class MatchManager{
    
    public static final List<Match> ONGOING_MATCHES = new CopyOnWriteArrayList<>();
    
    public static void startMatch(PlayerData playerData1, PlayerData playerData2, Kit kit){
        Map map = (Map) kit.getMaps().toArray()[0];
        new Match(kit, map, playerData1, playerData2);
    }
    
    public static void startMatch(PlayerData playerData1, PlayerData playerData2, Kit kit, Map map){
        Match match = new Match(kit, map, playerData1, playerData2);
    }
    
    public static Match getPlayerMatch(PlayerData playerData){
        for(Match match : ONGOING_MATCHES){
            if(match.getPlayers().contains(playerData))
                return match;
        }
        
        return null;
    }
}
