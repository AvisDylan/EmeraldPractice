/**
 * Created by dylan on 6/21/2025
 */

package com.emeraldnetwork.emeraldPractice.utils;

import com.emeraldnetwork.emeraldPractice.misc.DeathEffect;
import com.emeraldnetwork.emeraldPractice.player.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import xyz.xenondevs.particle.ParticleBuilder;
import xyz.xenondevs.particle.ParticleEffect;

public final class DeathEffectUtils{
    
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
        killer.getWorld().createExplosion(victim.getLocation().getX(), victim.getLocation().getY(), victim.getLocation().getZ(), 5.0f, false, false);
    }
    
    private static void playLightning(Player killer, Player victim){
        killer.getWorld().strikeLightningEffect(victim.getLocation());
    }
    
    private static void playBlood(Player killer, Player victim){
        killer.getWorld().playSound(victim.getLocation(), Sound.DIG_WOOD, 100.0f, 1.0f);
        
        new ParticleBuilder(ParticleEffect.DAMAGE_INDICATOR).setLocation(victim.getLocation()).setAmount(10).setOffset(0, 1, 0).display();
    }
}
