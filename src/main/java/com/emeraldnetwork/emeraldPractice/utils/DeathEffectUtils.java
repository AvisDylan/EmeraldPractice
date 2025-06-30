/**
 * Created by dylan on 6/21/2025
 */

package com.emeraldnetwork.emeraldPractice.utils;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.emeraldnetwork.emeraldPractice.misc.DeathEffect;
import com.emeraldnetwork.emeraldPractice.player.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import xyz.xenondevs.particle.ParticleBuilder;
import xyz.xenondevs.particle.ParticleEffect;

import java.lang.reflect.InvocationTargetException;

public final class DeathEffectUtils{
    
    private static final ProtocolManager MANAGER = ProtocolLibrary.getProtocolManager();
    
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
        PacketContainer packet = MANAGER.createPacket(PacketType.Play.Server.EXPLOSION);
        
        packet.getDoubles().write(0, victim.getLocation().getX());
        packet.getDoubles().write(1, victim.getLocation().getY());
        packet.getDoubles().write(2, victim.getLocation().getZ());
        packet.getFloat().write(0, 5.0f);
        
        try{
            MANAGER.sendServerPacket(killer, packet);
        }catch(InvocationTargetException ite){
            Bukkit.getLogger().severe(ite.getMessage());
        }
    }
    
    private static void playLightning(Player killer, Player victim){
        killer.getWorld().strikeLightningEffect(victim.getLocation());
    }
    
    private static void playBlood(Player killer, Player victim){
        killer.getWorld().playSound(victim.getLocation(), Sound.DIG_WOOD, 100.0f, 1.0f);
        
        new ParticleBuilder(ParticleEffect.DAMAGE_INDICATOR).setLocation(victim.getLocation()).setAmount(10).setOffset(0, 1, 0).display();
    }
}
