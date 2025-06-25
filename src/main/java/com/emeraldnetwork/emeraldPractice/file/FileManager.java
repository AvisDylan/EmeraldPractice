package com.emeraldnetwork.emeraldPractice.file;

import com.emeraldnetwork.emeraldPractice.EmeraldPractice;
import com.emeraldnetwork.emeraldPractice.adapter.FileAdapter;
import com.emeraldnetwork.emeraldPractice.adapter.ItemStackAdapter;
import com.emeraldnetwork.emeraldPractice.adapter.LocationAdapter;
import com.emeraldnetwork.emeraldPractice.adapter.PotionEffectAdapter;
import com.emeraldnetwork.emeraldPractice.kit.Kit;
import com.emeraldnetwork.emeraldPractice.kit.KitManager;
import com.emeraldnetwork.emeraldPractice.profile.PlayerProfile;
import com.emeraldnetwork.emeraldPractice.utils.SpawnPointUtils;
import com.emeraldnetwork.emeraldPractice.utils.StatResetUtils;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.io.*;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Queue;
import java.util.UUID;

public class FileManager{
    
    private static final File KITS_FILE = new File(EmeraldPractice.getPlugin().getDataFolder().getAbsoluteFile() + "/kits.json");
    private static final File SPAWN_POINT_FILE = new File(EmeraldPractice.getPlugin().getDataFolder().getAbsoluteFile() + "/spawnpoint.json");
    private static final File PLAYERS_TO_WIPE_FILE = new File(EmeraldPractice.getPlugin().getDataFolder().getAbsoluteFile() + "profiles/playerstowipe.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting()
                                                        .excludeFieldsWithoutExposeAnnotation()
                                                        .registerTypeAdapter(Location.class, new LocationAdapter())
                                                        .registerTypeAdapter(ItemStack.class, new ItemStackAdapter())
                                                        .registerTypeAdapter(PotionEffect.class, new PotionEffectAdapter())
                                                        .registerTypeAdapter(File.class, new FileAdapter()).create();
    
    public static void loadPlayersToWipe(){
        try{
            PLAYERS_TO_WIPE_FILE.getParentFile().mkdir();
            PLAYERS_TO_WIPE_FILE.createNewFile();
            
            Reader reader = new BufferedReader(new FileReader(PLAYERS_TO_WIPE_FILE));
            Type type = new TypeToken<Queue<UUID>>(){}.getType();
            
            StatResetUtils.PLAYERS_TO_RESET.addAll(GSON.fromJson(reader, type));
            reader.close();
        }catch(FileNotFoundException fnfe){
            Bukkit.getLogger().severe("File not found!");
        }catch(NullPointerException npe){
            Bukkit.getLogger().severe("Empty file!");
        }catch(IOException ie){
            Bukkit.getLogger().severe(ie.getMessage());
        }
    }
    
    public static void savePlayersToWipe(){
        try{
            PLAYERS_TO_WIPE_FILE.getParentFile().mkdir();
            PLAYERS_TO_WIPE_FILE.createNewFile();
            
            Writer writer = new BufferedWriter(new FileWriter(PLAYERS_TO_WIPE_FILE, false));
            
            GSON.toJson(StatResetUtils.PLAYERS_TO_RESET, writer);
            writer.flush();
            writer.close();
        }catch(FileNotFoundException fnfe){
            Bukkit.getLogger().severe("File not found!");
        }catch(IOException ie){
            Bukkit.getLogger().severe(ie.getMessage());
        }
    }
    
    public static void saveProfile(PlayerProfile playerProfile){
        File file = new File(EmeraldPractice.getPlugin().getDataFolder().getAbsoluteFile() + "/profiles/save_" + playerProfile.getUuid() + "_" + System.currentTimeMillis());
        
        try{
            file.getParentFile().mkdir();
            file.createNewFile();
            
            Writer writer = new BufferedWriter(new FileWriter(file, false));
            
            GSON.toJson(playerProfile, writer);
            writer.flush();
            writer.close();
            Bukkit.getLogger().info("Saved player profile!");
        }catch(FileNotFoundException fnfe){
            Bukkit.getLogger().severe("File not found!");
        }catch(IOException ie){
            Bukkit.getLogger().severe(ie.getMessage());
        }
    }
    
    public static void saveSpawnPoint(){
        try{
            SPAWN_POINT_FILE.getParentFile().mkdir();
            SPAWN_POINT_FILE.createNewFile();
            
            Writer writer = new BufferedWriter(new FileWriter(SPAWN_POINT_FILE, false));
            
            GSON.toJson(SpawnPointUtils.getSpawnPoint(), writer);
            writer.flush();
            writer.close();
            Bukkit.getLogger().info("Saved spawn point!");
        }catch(FileNotFoundException fnfe){
            Bukkit.getLogger().severe("File not found!");
        }catch(IOException ie){
            Bukkit.getLogger().severe(ie.getMessage());
        }
    }
    
    public static void loadSpawnPoint(){
        try{
            SPAWN_POINT_FILE.getParentFile().mkdir();
            SPAWN_POINT_FILE.createNewFile();
            
            Reader reader = new BufferedReader(new FileReader(SPAWN_POINT_FILE));
            Type type = new TypeToken<Location>(){}.getType();
            
            SpawnPointUtils.setSpawnPoint(GSON.fromJson(reader, type));
            Bukkit.getLogger().info("Loaded spawn point!");
            reader.close();
        }catch(FileNotFoundException fnfe){
            Bukkit.getLogger().severe("File not found!");
        }catch(NullPointerException npe){
            Bukkit.getLogger().severe("Empty file!");
        }catch(IOException ie){
            Bukkit.getLogger().severe(ie.getMessage());
        }
    }
    
    public static void saveKits(){
        try{
            KITS_FILE.getParentFile().mkdir();
            KITS_FILE.createNewFile();
            
            Writer writer = new BufferedWriter(new FileWriter(KITS_FILE, false));
            
            GSON.toJson(KitManager.KITS, writer);
            writer.flush();
            writer.close();
            Bukkit.getLogger().info("Saved kits!");
        }catch(FileNotFoundException fnfe){
            Bukkit.getLogger().severe("File not found!");
        }catch(IOException ie){
            Bukkit.getLogger().severe(ie.getMessage());
        }
    }
    
    public static void loadKits(){
        try{
            KITS_FILE.getParentFile().mkdir();
            KITS_FILE.createNewFile();
            
            Reader reader = new BufferedReader(new FileReader(KITS_FILE));
            Type type = new TypeToken<List<Kit>>(){}.getType();
            
            KitManager.KITS.addAll(GSON.fromJson(reader, type));
            Bukkit.getLogger().info("Loaded kits!");
            reader.close();
        }catch(FileNotFoundException fnfe){
            Bukkit.getLogger().severe("File not found!");
        }catch(NullPointerException npe){
            Bukkit.getLogger().severe("Empty file!");
        }catch(IOException ie){
            Bukkit.getLogger().severe(ie.getMessage());
        }
    }
}
