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
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import xyz.xenondevs.particle.ParticleBuilder;
import xyz.xenondevs.particle.ParticleEffect;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;

public final class DeathEffectUtils{
    
    //TODO FIX DEATH EFFECTS
    
    public static void playDeathEffect(PlayerData killerData, PlayerData victimData){
        if(killerData.getProfile().getDeathEffect() == DeathEffect.NONE)
            return;
        
        Player killer = Bukkit.getPlayer(killerData.getUuid());
        Player victim = Bukkit.getPlayer(victimData.getUuid());
        
        switch(killerData.getProfile().getDeathEffect()){
            case BLOOD -> playBlood(killer, victim);
            case EXPLOSION -> playExplosion(killer, victim);
            case LIGHTNING -> playLightning(killer, victim);
        }
    }
    
    private static void playExplosion(Player killer, Player victim){
        PacketContainer packetContainer = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.EXPLOSION);
        
        Bukkit.getLogger().info("Doubles: " + packetContainer.getDoubles().size());
        Bukkit.getLogger().info("BlockPositionCollectionModifiers: " + packetContainer.getBlockPositionCollectionModifier().size());
        Bukkit.getLogger().info("Floats: " + packetContainer.getFloat().size());
        
        packetContainer.getDoubles().write(0, victim.getLocation().getX());
        packetContainer.getDoubles().write(1, victim.getLocation().getY());
        packetContainer.getDoubles().write(2, victim.getLocation().getZ());
        packetContainer.getBlockPositionCollectionModifier().write(0, Collections.emptyList());
        packetContainer.getFloat().write(0, 4.0f);
        
        try{
            ProtocolLibrary.getProtocolManager().sendServerPacket(killer, packetContainer);
        }catch(InvocationTargetException ite){
            Bukkit.getLogger().severe(ite.getMessage());
        }
    }
    
    private static void playLightning(Player killer, Player victim){
        PacketContainer packetContainer = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.SPAWN_ENTITY_WEATHER);
        
        Bukkit.getLogger().info("Integers: " + packetContainer.getIntegers().size());
        Bukkit.getLogger().info("Doubles: " + packetContainer.getDoubles().size());
        
        packetContainer.getIntegers().write(0, (int) (Math.random() * Integer.MAX_VALUE));
        packetContainer.getIntegers().write(1, 1);
        packetContainer.getIntegers().write(0, (int) victim.getLocation().getX());
        packetContainer.getIntegers().write(1, (int) victim.getLocation().getY());
        packetContainer.getIntegers().write(2, (int) victim.getLocation().getZ());
        
        try{
            ProtocolLibrary.getProtocolManager().sendServerPacket(killer, packetContainer);
        }catch(InvocationTargetException ite){
            Bukkit.getLogger().severe(ite.getMessage());
        }
    }
    
    private static void playBlood(Player killer, Player victim){
        killer.playSound(victim.getLocation(), Sound.DIG_WOOD, 10.0f, 10.0f);
        
        new ParticleBuilder(ParticleEffect.DAMAGE_INDICATOR).setLocation(victim.getLocation()).setAmount(10).setOffset(0, 1, 0).display(killer);
    }
    
    private static void playFireWork(Player killer, Player victim){
    
    }
}
