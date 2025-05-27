package com.emeraldnetwork.emeraldPractice;

import com.emeraldnetwork.emeraldPractice.commands.*;
import com.emeraldnetwork.emeraldPractice.file.FileManager;
import com.emeraldnetwork.emeraldPractice.kit.KitManager;
import com.emeraldnetwork.emeraldPractice.listeners.entity.*;
import com.emeraldnetwork.emeraldPractice.listeners.world.MobSpawnListener;
import com.emeraldnetwork.emeraldPractice.listeners.world.WeatherChangeListener;
import com.emeraldnetwork.emeraldPractice.queue.QueueManager;
import org.apache.commons.lang.SystemUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class EmeraldPractice extends JavaPlugin{
    
    private static Plugin plugin;
    
    @Override
    public void onEnable(){
        if(!SystemUtils.isJavaVersionAtLeast(17f))
            getLogger().warning("Your java version is too low consider updating to java version 17+");
        
        plugin = this;
        
        FileManager.loadKits();
        FileManager.loadSpawnPoint();
        
        getServer().getPluginManager().registerEvents(new JoinListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(), this);
        getServer().getPluginManager().registerEvents(new BlockPlaceListener(), this);
        getServer().getPluginManager().registerEvents(new BlockBreakListener(), this);
        getServer().getPluginManager().registerEvents(new EntityDamageByEntityListener(), this);
        getServer().getPluginManager().registerEvents(new LeaveListener(), this);
        getServer().getPluginManager().registerEvents(new MobSpawnListener(), this);
        getServer().getPluginManager().registerEvents(new WeatherChangeListener(), this);
        getServer().getPluginManager().registerEvents(new FoodLevelChangeListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerDropItemListener(), this);
        getServer().getPluginManager().registerEvents(new EntityDamageListener(), this);
        
        getCommand("kit").setExecutor(new KitCommand());
        getCommand("map").setExecutor(new MapCommand());
        getCommand("queue").setExecutor(new QueueCommand());
        getCommand("edit").setExecutor(new EditModeCommand());
        getCommand("setspawn").setExecutor(new SpawnPointCommand());
        getCommand("leave").setExecutor(new LeaveCommand());
        
        KitManager.KITS.forEach(kit -> {
            Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
                QueueManager.handleQueue(kit);
            }, 0L, 10L);
        });
    }
    
    @Override
    public void onDisable(){
        FileManager.saveKits();
        FileManager.saveSpawnPoint();
    }
    
    public static Plugin getPlugin(){
        return plugin;
    }
}
