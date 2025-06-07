package com.emeraldnetwork.emeraldPractice.database;

import com.emeraldnetwork.emeraldPractice.kit.Kit;
import com.emeraldnetwork.emeraldPractice.kit.KitManager;
import com.emeraldnetwork.emeraldPractice.player.PlayerManager;
import com.emeraldnetwork.emeraldPractice.profile.PlayerKitProfile;
import com.emeraldnetwork.emeraldPractice.profile.PlayerProfile;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.bukkit.Bukkit;
import org.bukkit.WeatherType;

import java.sql.*;
import java.util.Map;
import java.util.UUID;

public class DatabaseManager{

    private static Connection playerDatabase;
    private static final Gson GSON = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    private static final String PLAYERDATA_URL = "jdbc:mysql://209.112.91.51:3306/s2_playerdata",
                                PLAYERDATA_USERNAME = "u2_BY1Ur1T9LN",
                                PLAYERDATA_PASSWORD = "=pKjeyhYM6F6mA.++Xgo6xJw";

    public static void init() {
        try{
            playerDatabase = DriverManager.getConnection(PLAYERDATA_URL, PLAYERDATA_USERNAME, PLAYERDATA_PASSWORD);
        }catch(SQLException se){
            se.printStackTrace();
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
                        "score_board, " +
                        "global_chat, " +
                        "edit_mode, " +
                        "weather_type, " +
                        "ping_range, " +
                        "player_time, " +
                        "kit_datas) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                        "ON DUPLICATE KEY UPDATE " +
                        "receive_messages = VALUES(receive_messages), " +
                        "message_sounds = VALUES(message_sounds), " +
                        "duel_requests = VALUES(duel_requests), " +
                        "score_board = VALUES(score_board), " +
                        "global_chat = VALUES(global_chat), " +
                        "edit_mode = VALUES(edit_mode), " +
                        "weather_type = VALUES(weather_type), " +
                        "ping_range = VALUES(ping_range), " +
                        "player_time = VALUES(player_time)," +
                        "kit_datas = VALUES(kit_datas);";
        long now = System.currentTimeMillis();
        
        try{
            PreparedStatement statement = playerDatabase.prepareStatement(code);

            statement.setString(1, playerProfile.getUuid().toString());
            statement.setInt(2, playerProfile.isReceiveMessages() ? 1 : 0);
            statement.setInt(3, playerProfile.isMessageSounds() ? 1 : 0);
            statement.setInt(4, playerProfile.isDuelRequests() ? 1 : 0);
            statement.setInt(5, playerProfile.isScoreBoard() ? 1 : 0);
            statement.setInt(6, playerProfile.isGlobalChat() ? 1 : 0);
            statement.setInt(7, playerProfile.isEditMode() ? 1 : 0);
            statement.setString(8, playerProfile.getPlayerWeather().name());
            statement.setInt(9, playerProfile.getPingRange());
            statement.setInt(10, playerProfile.getPlayerTime());
            statement.setString(11, GSON.toJson(playerProfile.getKitDataList()));
            
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
                playerProfile.setScoreBoard(resultSet.getBoolean("score_board"));
                playerProfile.setGlobalChat(resultSet.getBoolean("global_chat"));
                playerProfile.setEditMode(resultSet.getBoolean("edit_mode"));
                playerProfile.setPlayerWeather(WeatherType.valueOf(resultSet.getString("weather_type")));
                playerProfile.setPingRange(resultSet.getInt("ping_range"));
                playerProfile.setPlayerTime(resultSet.getInt("player_time"));
                
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
