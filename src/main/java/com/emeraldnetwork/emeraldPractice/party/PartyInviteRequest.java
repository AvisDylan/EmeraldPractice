/**
 * Created by dylan on 6/17/2025
 */

package com.emeraldnetwork.emeraldPractice.party;

import com.emeraldnetwork.emeraldPractice.player.PlayerData;

public class PartyInviteRequest{
    
    private PlayerData sender;
    private PlayerData receiver;
    
    public PartyInviteRequest(PlayerData sender, PlayerData receiver){
        this.sender = sender;
        this.receiver = receiver;
    }
}
