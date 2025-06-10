package com.emeraldnetwork.emeraldPractice;

import com.emeraldnetwork.emeraldPractice.commands.*;
import com.emeraldnetwork.emeraldPractice.database.DatabaseManager;
import com.emeraldnetwork.emeraldPractice.file.FileManager;
import com.emeraldnetwork.emeraldPractice.kit.KitManager;
import com.emeraldnetwork.emeraldPractice.listeners.entity.*;
import com.emeraldnetwork.emeraldPractice.listeners.world.MobSpawnListener;
import com.emeraldnetwork.emeraldPractice.listeners.world.WeatherChangeListener;
import com.emeraldnetwork.emeraldPractice.player.PlayerData;
import com.emeraldnetwork.emeraldPractice.player.PlayerManager;
import com.emeraldnetwork.emeraldPractice.queue.QueueManager;
import com.emeraldnetwork.emeraldPractice.scoreboard.ScoreboardManager;
import com.emeraldnetwork.emeraldPractice.utils.MultithreadedUtils;
import com.emeraldnetwork.emeraldPractice.utils.SpawnPointUtils;
import org.apache.commons.lang.SystemUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;

public final class EmeraldPractice extends JavaPlugin{
    
    private static Plugin plugin;
    
    @Override
    public void onEnable(){
        if(!SystemUtils.isJavaVersionAtLeast(17f))
            getLogger().warning("Your java version is too low consider updating to java version 17+");
        
        plugin = this;
        String packageName = getClass().getPackage().getName();
        
        DatabaseManager.init();
        FileManager.loadKits();
        FileManager.loadSpawnPoint();
        
        for(Class<?> clazz : new Reflections(packageName + ".listeners").getSubTypesOf(Listener.class)){
            try{
                Listener listener = (Listener) clazz.getDeclaredConstructor().newInstance();
                
                getServer().getPluginManager().registerEvents(listener, this);
            }catch(InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e){
                throw new RuntimeException(e);
            }
        }
        
        getCommand("kit").setExecutor(new KitCommand());
        getCommand("map").setExecutor(new MapCommand());
        getCommand("queue").setExecutor(new QueueCommand());
        getCommand("edit").setExecutor(new EditModeCommand());
        getCommand("setspawn").setExecutor(new SpawnPointCommand());
        getCommand("leave").setExecutor(new LeaveCommand());
        getCommand("world").setExecutor(new WorldCommand());
        getCommand("inventory").setExecutor(new InventoryCommand());
        getCommand("resetstats").setExecutor(new ResetStatsCommand());
        getCommand("duel").setExecutor(new DuelCommand());
        getCommand("accept").setExecutor(new AcceptCommand());
        
        KitManager.KITS.forEach(kit -> Bukkit.getScheduler().runTaskTimer(this, () -> QueueManager.handleQueue(kit), 0L, 10L));
        
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> MultithreadedUtils.EXECUTOR_SERVICE.submit(() -> {
            for(PlayerData playerData : PlayerManager.PLAYERS.values()){
                ScoreboardManager.updateBoard(playerData);
            }
        }), 0L, 10L);
        
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> MultithreadedUtils.EXECUTOR_SERVICE.submit(DatabaseManager::savePlayerProfiles), 12000L, 12000L);
    }
    
    @Override
    public void onDisable(){
        if(!KitManager.KITS.isEmpty())
            FileManager.saveKits();
        
        if(SpawnPointUtils.getSpawnPoint() != null)
            FileManager.saveSpawnPoint();
        
        if(!PlayerManager.PLAYERS.isEmpty())
            DatabaseManager.savePlayerProfiles();
    }
    
    public static Plugin getPlugin(){
        return plugin;
    }
}
