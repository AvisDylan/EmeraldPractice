package com.emeraldnetwork.emeraldPractice.file;

import com.emeraldnetwork.emeraldPractice.EmeraldPractice;
import com.emeraldnetwork.emeraldPractice.adapter.LocationAdapter;
import com.emeraldnetwork.emeraldPractice.kit.Kit;
import com.emeraldnetwork.emeraldPractice.kit.KitManager;
import com.emeraldnetwork.emeraldPractice.utils.SpawnPointUtils;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.io.*;
import java.lang.reflect.Type;
import java.util.List;
import java.util.UUID;

public class FileManager{
    
    private static final File KITS_FILE = new File(EmeraldPractice.getPlugin().getDataFolder().getAbsoluteFile() + "/kits.json");
    private static final File SPAWN_POINT_FILE = new File(EmeraldPractice.getPlugin().getDataFolder().getAbsoluteFile() + "/spawnpoint.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
    
    public static void saveSpawnPoint(){
        try{
            SPAWN_POINT_FILE.getParentFile().mkdir();
            SPAWN_POINT_FILE.createNewFile();
            
            Writer writer = new BufferedWriter(new FileWriter(SPAWN_POINT_FILE, false));
            
            GSON.toJson(new LocationAdapter(SpawnPointUtils.getSpawnPoint().getWorld().getName(), SpawnPointUtils.getSpawnPoint().getX(), SpawnPointUtils.getSpawnPoint().getY(), SpawnPointUtils.getSpawnPoint().getZ(), SpawnPointUtils.getSpawnPoint().getYaw(), SpawnPointUtils.getSpawnPoint().getPitch()), writer);
            writer.flush();
            writer.close();
            Bukkit.getLogger().fine("Saved spawn point!");
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
            Type type = new TypeToken<LocationAdapter>(){}.getType();
            LocationAdapter locationAdapter = GSON.fromJson(reader, type);
            
            SpawnPointUtils.setSpawnPoint(new Location(Bukkit.getWorld(locationAdapter.getWorldName()), locationAdapter.getX(), locationAdapter.getY(), locationAdapter.getZ(), locationAdapter.getYaw(), locationAdapter.getPitch()));
            Bukkit.getLogger().fine("Loaded spawn point!");
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
            Bukkit.getLogger().fine("Saved kits!");
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
            Bukkit.getLogger().fine("Loaded kits!");
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
