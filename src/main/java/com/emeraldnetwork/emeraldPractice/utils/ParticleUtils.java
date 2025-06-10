/**
 * Created by dylan on 6/10/2025
 */

package com.emeraldnetwork.emeraldPractice.utils;

import org.bukkit.Effect;
import org.bukkit.entity.Player;
import xyz.xenondevs.particle.ParticleBuilder;
import xyz.xenondevs.particle.ParticleEffect;

public final class ParticleUtils{

    public static void showParticles(ParticleEffect particleEffect, Player player){
        particleEffect.display(player.getLocation());
    }
}
