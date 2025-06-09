package com.emeraldnetwork.emeraldPractice.match;

import com.emeraldnetwork.emeraldPractice.kit.Kit;
import com.emeraldnetwork.emeraldPractice.map.Map;
import com.emeraldnetwork.emeraldPractice.player.PlayerData;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class MatchManager{
    
    public static final List<Match> ONGOING_MATCHES = new CopyOnWriteArrayList<>();
    public static final java.util.Map<UUID, ItemStack[]> INVENTORY_MAP = new ConcurrentHashMap<>();
    
    public static void startMatch(Kit kit, boolean ranked, PlayerData... players){
        Map map = kit.getMaps().stream().skip(new Random().nextInt(kit.getMaps().size())).findFirst().orElse(null);
        new Match(kit, map, ranked, players);
    }
    
    /*public static void startMatch(PlayerData playerData1, PlayerData playerData2, Kit kit, Map map){
        Match match = new Match(kit, map, playerData1, playerData2);
    }*/
    
    public static Match getPlayerMatch(PlayerData playerData){
        for(Match match : ONGOING_MATCHES){
            if(match.getPlayers().contains(playerData))
                return match;
        }
        
        return null;
    }
    
    public static int getPlayersInMatches(){
        AtomicInteger playersInMatches = new AtomicInteger();
        
        ONGOING_MATCHES.forEach(match -> {
            playersInMatches.addAndGet(match.getPlayers().size());
        });
        
        return playersInMatches.get();
    }
}
