package com.emeraldnetwork.emeraldPractice;

import com.emeraldnetwork.emeraldPractice.commands.*;
import com.emeraldnetwork.emeraldPractice.database.DatabaseManager;
import com.emeraldnetwork.emeraldPractice.ffa.FfaManager;
import com.emeraldnetwork.emeraldPractice.file.FileManager;
import com.emeraldnetwork.emeraldPractice.kit.KitManager;
import com.emeraldnetwork.emeraldPractice.player.PlayerData;
import com.emeraldnetwork.emeraldPractice.player.PlayerManager;
import com.emeraldnetwork.emeraldPractice.queue.QueueManager;
import com.emeraldnetwork.emeraldPractice.scoreboard.ScoreboardManager;
import com.emeraldnetwork.emeraldPractice.utils.DeathEffectUtils;
import com.emeraldnetwork.emeraldPractice.utils.MessageUtils;
import com.emeraldnetwork.emeraldPractice.utils.MultithreadedUtils;
import com.emeraldnetwork.emeraldPractice.utils.SpawnPointUtils;
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
        plugin = this;
        String packageName = getClass().getPackage().getName();
        
        DatabaseManager.init();
        FileManager.loadPlayersToWipe();
        FileManager.loadKits();
        FileManager.loadSpawnPoint();
        FfaManager.init();
        
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
        getCommand("spectate").setExecutor(new SpectateCommand());
        getCommand("settings").setExecutor(new SettingsCommand());
        getCommand("party").setExecutor(new PartyCommand());
        getCommand("kiteditor").setExecutor(new KitEditorCommand());
        getCommand("pc").setExecutor(new PcCommand());
        getCommand("goldenhead").setExecutor(new GoldenHeadCommand());
        getCommand("leaderboards").setExecutor(new LeaderboardCommand());
        getCommand("stats").setExecutor(new StatsCommand());
        getCommand("ffa").setExecutor(new FfaCommand());
        
        KitManager.KITS.forEach(kit -> Bukkit.getScheduler().runTaskTimer(this, () -> QueueManager.handleQueue(kit), 0L, 10L));
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> MultithreadedUtils.EXECUTOR_SERVICE.submit(() -> PlayerManager.PLAYERS.values().forEach(ScoreboardManager::updateBoard)), 0L, 10L);
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> MultithreadedUtils.EXECUTOR_SERVICE.submit(DatabaseManager::savePlayerProfiles), 12000L, 12000L);
        /*Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> KitManager.KITS.forEach(kit -> MultithreadedUtils.EXECUTOR_SERVICE.submit(() -> {
            kit.getTopRankedPlayers().clear();
            kit.getTopUnrankedPlayers().clear();
            
            kit.getTopUnrankedPlayers().addAll(DatabaseManager.getPlayers(kit, false));
            kit.getTopRankedPlayers().addAll(DatabaseManager.getPlayers(kit, true));
        })), 0L, 12000L);*/
        Bukkit.getScheduler().runTaskTimer(this, MessageUtils::sendMessage, 0L, 6000L);
    }
    
    @Override
    public void onDisable(){
        FileManager.savePlayersToWipe();
        
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
