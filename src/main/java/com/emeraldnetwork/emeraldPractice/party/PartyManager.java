/**
 * Created by dylan on 6/13/2025
 */

package com.emeraldnetwork.emeraldPractice.party;

import com.emeraldnetwork.emeraldPractice.player.PlayerData;

import java.util.LinkedList;
import java.util.List;

public final class PartyManager{
    
    public static final List<Party> PARTIES = new LinkedList<>();
    
    public static Party getPlayerParty(PlayerData playerData){
        for(Party party : PARTIES){
            if(party.getPlayers().contains(playerData))
                return party;
        }
        
        return null;
    }
}
