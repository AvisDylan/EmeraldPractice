package com.emeraldnetwork.emeraldPractice.database;

import com.emeraldnetwork.emeraldPractice.adapter.ItemStackAdapter;
import com.emeraldnetwork.emeraldPractice.kit.Kit;
import com.emeraldnetwork.emeraldPractice.kit.KitManager;
import com.emeraldnetwork.emeraldPractice.misc.DeathEffect;
import com.emeraldnetwork.emeraldPractice.player.PlayerManager;
import com.emeraldnetwork.emeraldPractice.profile.PlayerKitProfile;
import com.emeraldnetwork.emeraldPractice.profile.PlayerProfile;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.WeatherType;
import org.bukkit.inventory.ItemStack;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

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
        PlayerManager.PLAYERS.forEach((uuid, playerData) -> savePlayerProfile(playerData.getProfile()));
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
                        "player_weather, " +
                        "player_time, " +
                        "ping_range, " +
                        "win_streak, " +
                        "party_requests, " +
                        "party_sounds, " +
                        "death_effect, " +
                        "kit_datas) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?::weather_type, ?, ?, ?, ?, ?, ?::death_effect, ?::jsonb) " +
                        "ON CONFLICT (player_uuid) DO UPDATE SET " +
                        "receive_messages = EXCLUDED.receive_messages, " +
                        "message_sounds = EXCLUDED.message_sounds, " +
                        "duel_requests = EXCLUDED.duel_requests, " +
                        "duel_sounds = EXCLUDED.duel_sounds, " +
                        "allow_spectators = EXCLUDED.allow_spectators, " +
                        "score_board = EXCLUDED.score_board, " +
                        "global_chat = EXCLUDED.global_chat, " +
                        "player_weather = EXCLUDED.player_weather, " +
                        "player_time = EXCLUDED.player_time," +
                        "ping_range = EXCLUDED.ping_range, " +
                        "win_streak = EXCLUDED.win_streak," +
                        "party_requests = EXCLUDED.party_requests," +
                        "party_sounds = EXCLUDED.party_sounds," +
                        "death_effect = EXCLUDED.death_effect," +
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
            statement.setString(9, playerProfile.getPlayerWeather().name().toLowerCase());
            statement.setInt(10, playerProfile.getPlayerTime());
            statement.setInt(11, playerProfile.getPingRange());
            statement.setInt(12, playerProfile.getWinstreak());
            statement.setBoolean(13, playerProfile.isPartyInvites());
            statement.setBoolean(14, playerProfile.isPartySounds());
            statement.setString(15, playerProfile.getDeathEffect().name().toLowerCase());
            statement.setString(16, GSON.toJson(playerProfile.getKitDataList()));
            
            statement.executeUpdate();
            
            Bukkit.getLogger().info("Saved " + playerProfile.getUuid() + " profile in " + (System.currentTimeMillis() - now) + "ms!");
        }catch (SQLException se){
            se.printStackTrace();
        }
    }

    public static PlayerProfile loadPlayerProfile(UUID playerUuid){
        String code = "SELECT * FROM player_data WHERE player_uuid = ?";
        long now = System.currentTimeMillis();
        
        try(PreparedStatement statement = playerDatabase.prepareStatement(code)){
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
                playerProfile.setPlayerWeather(WeatherType.valueOf(resultSet.getString("player_weather").toUpperCase()));
                playerProfile.setPlayerTime(resultSet.getInt("player_time"));
                playerProfile.setPingRange(resultSet.getInt("ping_range"));
                playerProfile.setPartyInvites(resultSet.getBoolean("party_requests"));
                playerProfile.setPartySounds(resultSet.getBoolean("party_sounds"));
                playerProfile.setDeathEffect(DeathEffect.valueOf(resultSet.getString("death_effect").toUpperCase()));
                playerProfile.setWinstreak(resultSet.getInt("win_streak"));
                
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
    
    public static List<String> getPlayers(Kit kit, boolean ranked){
        String code = "SELECT * FROM player_data";
        Map<String, Double> unsortedPlayers = new HashMap<>();
        
        try(PreparedStatement statement = playerDatabase.prepareStatement(code)){
            ResultSet resultSet = statement.executeQuery();
            
            while(resultSet.next()){
                String jsonKitDataList = resultSet.getString("kit_datas");
                String uuid = resultSet.getString("player_uuid");
                
                if(jsonKitDataList.isEmpty())
                    continue;
                
                Map<String, PlayerKitProfile> kitDataList = GSON.fromJson(jsonKitDataList, new TypeToken<Map<String, PlayerKitProfile>>(){}.getType());
                kitDataList.keySet().removeIf(kitName -> KitManager.KITS.stream().noneMatch(kit1 -> kit1.getName().equalsIgnoreCase(kitName)));
                
                if(!kitDataList.containsKey(kit.getName()))
                    continue;
                
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(UUID.fromString(uuid));
                
                if(offlinePlayer == null)
                    continue;
                
                if(ranked)
                    unsortedPlayers.put(offlinePlayer.getName(), kitDataList.get(kit.getName()).getElo());
                else
                    unsortedPlayers.put(offlinePlayer.getName(), (double) kitDataList.get(kit.getName()).getUnrankedWins());
            }
        }catch (SQLException se){
            se.printStackTrace();
        }
        
        return unsortedPlayers.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).map(Map.Entry::getKey).collect(Collectors.toList());
    }
}
