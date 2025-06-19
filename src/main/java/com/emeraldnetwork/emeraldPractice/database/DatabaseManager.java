package com.emeraldnetwork.emeraldPractice.database;

import com.emeraldnetwork.emeraldPractice.adapter.ItemStackAdapter;
import com.emeraldnetwork.emeraldPractice.kit.KitManager;
import com.emeraldnetwork.emeraldPractice.player.PlayerManager;
import com.emeraldnetwork.emeraldPractice.profile.PlayerKitProfile;
import com.emeraldnetwork.emeraldPractice.profile.PlayerProfile;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.bukkit.Bukkit;
import org.bukkit.WeatherType;
import org.bukkit.inventory.ItemStack;

import java.sql.*;
import java.util.Map;
import java.util.UUID;

public class DatabaseManager{

    private static Connection playerDatabase;
    private static final Gson GSON = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().registerTypeAdapter(ItemStack.class, new ItemStackAdapter()).create();
    private static final String PLAYERDATA_URL = "jdbc:postgresql://209.112.91.51:5432/player_datas",
                                PLAYERDATA_USERNAME = "postgres",
                                PLAYERDATA_PASSWORD = "!aosikop\"sdW2sd$";

    public static void init() {
        try{
            Class.forName("org.postgresql.Driver");
            playerDatabase = DriverManager.getConnection(PLAYERDATA_URL, PLAYERDATA_USERNAME, PLAYERDATA_PASSWORD);
        }catch(SQLException | ClassNotFoundException e){
            Bukkit.getLogger().severe(e.getMessage());
        }
    }
    
    public static void savePlayerProfiles(){
        PlayerManager.PLAYERS.forEach((uuid, playerData) -> {
            savePlayerProfile(playerData.getProfile());
        });
    }
    
    public static void savePlayerProfile(PlayerProfile playerProfile){
        String code = "INSERT INTO player_data (" +
                        "player_uuid, " +
                        "receive_messages, " +
                        "message_sounds, " +
                        "duel_requests, " +
                        "duel_sounds, " +
                        "allow_spectators, " +
                        "score_board, " +
                        "global_chat, " +
                        "edit_mode, " +
                        "player_weather, " +
                        "player_time, " +
                        "ping_range, " +
                        "win_streak, " +
                        "kit_datas) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?::weather_type, ?, ?, ?, ?::jsonb) " +
                        "ON CONFLICT (player_uuid) DO UPDATE SET " +
                        "receive_messages = EXCLUDED.receive_messages, " +
                        "message_sounds = EXCLUDED.message_sounds, " +
                        "duel_requests = EXCLUDED.duel_requests, " +
                        "duel_sounds = EXCLUDED.duel_sounds, " +
                        "allow_spectators = EXCLUDED.allow_spectators, " +
                        "score_board = EXCLUDED.score_board, " +
                        "global_chat = EXCLUDED.global_chat, " +
                        "edit_mode = EXCLUDED.edit_mode, " +
                        "player_weather = EXCLUDED.player_weather, " +
                        "player_time = EXCLUDED.player_time," +
                        "ping_range = EXCLUDED.ping_range, " +
                        "win_streak = EXCLUDED.win_streak," +
                        "kit_datas = EXCLUDED.kit_datas;";
        long now = System.currentTimeMillis();
        
        try{
            PreparedStatement statement = playerDatabase.prepareStatement(code);

            statement.setString(1, playerProfile.getUuid().toString());
            statement.setBoolean(2, playerProfile.isReceiveMessages());
            statement.setBoolean(3, playerProfile.isMessageSounds());
            statement.setBoolean(4, playerProfile.isDuelRequests());
            statement.setBoolean(5, playerProfile.isDuelSounds());
            statement.setBoolean(6, playerProfile.isAllowSpectators());
            statement.setBoolean(7, playerProfile.isScoreBoard());
            statement.setBoolean(8, playerProfile.isGlobalChat());
            statement.setBoolean(9, playerProfile.isEditMode());
            statement.setString(10, playerProfile.getPlayerWeather().name().toLowerCase());
            statement.setInt(11, playerProfile.getPlayerTime());
            statement.setInt(12, playerProfile.getPingRange());
            statement.setInt(13, playerProfile.getWinstreak());
            statement.setString(14, GSON.toJson(playerProfile.getKitDataList()));
            
            statement.executeUpdate();
            
            Bukkit.getLogger().info("Saved " + playerProfile.getUuid() + " profile in " + (System.currentTimeMillis() - now) + "ms!");
        }catch (SQLException se){
            se.printStackTrace();
        }
    }

    public static PlayerProfile loadPlayerProfile(UUID playerUuid){
        String code = "SELECT * FROM player_data WHERE player_uuid = ?";
        long now = System.currentTimeMillis();
        
        try{
            PreparedStatement statement = playerDatabase.prepareStatement(code);

            statement.setString(1, playerUuid.toString());
            
            ResultSet resultSet = statement.executeQuery();
            
            if(resultSet.next()){
                PlayerProfile playerProfile = new PlayerProfile(playerUuid);
                
                playerProfile.setReceiveMessages(resultSet.getBoolean("receive_messages"));
                playerProfile.setMessageSounds(resultSet.getBoolean("message_sounds"));
                playerProfile.setDuelRequests(resultSet.getBoolean("duel_requests"));
                playerProfile.setDuelSounds(resultSet.getBoolean("duel_sounds"));
                playerProfile.setAllowSpectators(resultSet.getBoolean("allow_spectators"));
                playerProfile.setScoreBoard(resultSet.getBoolean("score_board"));
                playerProfile.setGlobalChat(resultSet.getBoolean("global_chat"));
                playerProfile.setEditMode(resultSet.getBoolean("edit_mode"));
                playerProfile.setPlayerWeather(WeatherType.valueOf(resultSet.getString("player_weather").toUpperCase()));
                playerProfile.setPlayerTime(resultSet.getInt("player_time"));
                playerProfile.setPingRange(resultSet.getInt("ping_range"));
                playerProfile.setPlayerTime(resultSet.getInt("win_streak"));
                
                String jsonKitDataList = resultSet.getString("kit_datas");
                
                if(jsonKitDataList != null && !jsonKitDataList.isEmpty()){
                    Map<String, PlayerKitProfile> kitDataList = GSON.fromJson(jsonKitDataList, new TypeToken<Map<String, PlayerKitProfile>>(){}.getType());
                    
                    kitDataList.keySet().removeIf(kitName -> KitManager.KITS.stream().noneMatch(kit -> kit.getName().equalsIgnoreCase(kitName)));
                    
                    KitManager.KITS.forEach(kit -> kitDataList.putIfAbsent(kit.getName(), new PlayerKitProfile()));
                    
                    playerProfile.getKitDataList().putAll(kitDataList);
                }
                
                Bukkit.getLogger().info("Loaded " + playerUuid + " profile in " + (System.currentTimeMillis() - now) + "ms!");
                
                return playerProfile;
            }
        }catch (SQLException se){
            se.printStackTrace();
        }
        
        Bukkit.getLogger().info("No player profile");
        
        return null;
    }
}
