/**
 * Created by dylan on 6/21/2025
 */

package com.emeraldnetwork.emeraldPractice.utils;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.emeraldnetwork.emeraldPractice.misc.DeathEffect;
import com.emeraldnetwork.emeraldPractice.player.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;

public final class DeathEffectUtils{
    
    public static void playDeathEffect(PlayerData killerData, PlayerData victimData){
        if(killerData.getProfile().getDeathEffect() == DeathEffect.NONE)
            return;
        
        Player killer = Bukkit.getPlayer(killerData.getUuid());
        Player victim = Bukkit.getPlayer(victimData.getUuid());
        
        switch(killerData.getProfile().getDeathEffect()){
            case BLOOD -> playBlood(killer, victim);
            case EXPLOSION -> playExplosion(killer, victim);
            case LIGHTING -> playLightning(killer, victim);
        }
    }
    
    private static void playExplosion(Player killer, Player victim){
        PacketContainer packetContainer = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.EXPLOSION);
        
        packetContainer.getDoubles().write(0, victim.getLocation().getX());
        packetContainer.getDoubles().write(1, victim.getLocation().getY());
        packetContainer.getDoubles().write(2, victim.getLocation().getZ());
        packetContainer.getFloat().write(0, 4.0f);
        
        try{
            ProtocolLibrary.getProtocolManager().sendServerPacket(killer, packetContainer);
        }catch(InvocationTargetException ite){
            Bukkit.getLogger().severe(ite.getMessage());
        }
    }
    
    private static void playLightning(Player killer, Player victim){
        PacketContainer packetContainer = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.SPAWN_ENTITY_WEATHER);
        
        packetContainer.getIntegers().write(0, 128);
        packetContainer.getIntegers().write(1, 1);
        packetContainer.getDoubles().write(0, victim.getLocation().getX());
        packetContainer.getDoubles().write(1, victim.getLocation().getY());
        packetContainer.getDoubles().write(2, victim.getLocation().getZ());
        
        try{
            ProtocolLibrary.getProtocolManager().sendServerPacket(killer, packetContainer);
        }catch(InvocationTargetException ite){
            Bukkit.getLogger().severe(ite.getMessage());
        }
    }
    
    private static void playBlood(Player killer, Player victim){
        PacketContainer packetContainer = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.SPAWN_ENTITY_WEATHER);
        
        packetContainer.getIntegers().write(0, 128);
        packetContainer.getIntegers().write(1, 1);
        packetContainer.getDoubles().write(0, victim.getLocation().getX());
        packetContainer.getDoubles().write(1, victim.getLocation().getY());
        packetContainer.getDoubles().write(2, victim.getLocation().getZ());
        
        try{
            ProtocolLibrary.getProtocolManager().sendServerPacket(killer, packetContainer);
        }catch(InvocationTargetException ite){
            Bukkit.getLogger().severe(ite.getMessage());
        }
    }
}
