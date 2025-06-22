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
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.ReplaceOptions;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.WeatherType;
import org.bukkit.inventory.ItemStack;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class DatabaseManager{
    
    private static MongoClient mongoClient;
    private static MongoDatabase playerDatabase;
    private static MongoCollection<Document> players;
    private static final Gson GSON = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().registerTypeAdapter(ItemStack.class, new ItemStackAdapter()).create();

    public static void init(){
        mongoClient = MongoClients.create("mongodb://niceman:!aosikop\"sdW2sd$@209.112.91.51:27017");
        playerDatabase = mongoClient.getDatabase("practice_data");
        DatabaseManager.players = playerDatabase.getCollection("player_data");
    }
    
    public static void savePlayerProfiles(){
        PlayerManager.PLAYERS.forEach((uuid, playerData) -> savePlayerProfile(playerData.getProfile()));
    }
    
    public static void savePlayerProfile(PlayerProfile playerProfile){
        long now = System.currentTimeMillis();
        Document filterDocument = new Document("_id", playerProfile.getUuid().toString());
        Document playerDocument = new Document("_id", playerProfile.getUuid().toString())
                .append("receive_messages", playerProfile.isReceiveMessages())
                .append("message_sounds", playerProfile.isMessageSounds())
                .append("duel_requests", playerProfile.isDuelRequests())
                .append("duel_sounds", playerProfile.isDuelSounds())
                .append("allow_spectators", playerProfile.isAllowSpectators())
                .append("score_board", playerProfile.isScoreBoard())
                .append("global_chat", playerProfile.isGlobalChat())
                .append("player_weather", playerProfile.getPlayerWeather().name().toLowerCase())
                .append("player_time", playerProfile.getPlayerTime())
                .append("ping_range", playerProfile.getPingRange())
                .append("win_streak", playerProfile.getWinstreak())
                .append("party_requests", playerProfile.isPartyInvites())
                .append("party_sounds", playerProfile.isPartySounds())
                .append("death_effect", playerProfile.getDeathEffect().name().toLowerCase());
        Document kitDataDocument = new Document();
        
        playerProfile.getKitDataList().forEach((key, playerKitProfile) -> {
            Document kitProfileDocument = new Document()
                    .append("elo", playerKitProfile.getElo())
                    .append("rankedWins", playerKitProfile.getRankedWins())
                    .append("rankedLosses", playerKitProfile.getRankedLosses())
                    .append("unrankedWins", playerKitProfile.getUnrankedWins())
                    .append("unrankedLosses", playerKitProfile.getUnrankedLosses())
                    .append("winstreak", playerKitProfile.getWinstreak())
                    .append("kills", playerKitProfile.getKills())
                    .append("deaths", playerKitProfile.getDeaths())
                    .append("kitLayoutItems", GSON.toJson(playerKitProfile.getKitLayoutItems()));
            
            kitDataDocument.append(key.getName(), kitProfileDocument);
        });
        
        playerDocument.append("kit_datas", kitDataDocument);
        
        players.replaceOne(filterDocument, playerDocument, new ReplaceOptions().upsert(true));
        
        Bukkit.getLogger().info("Saved " + playerProfile.getUuid() + " profile in " + (System.currentTimeMillis() - now) + "ms!");
    }

    public static PlayerProfile loadPlayerProfile(UUID playerUuid){
        Document resultDocument = players.find(new Document("_id", playerUuid.toString())).first();
        
        if(resultDocument != null){
            long now = System.currentTimeMillis();
            PlayerProfile playerProfile = new PlayerProfile(playerUuid);
            
            playerProfile.setReceiveMessages(resultDocument.getBoolean("receive_messages", true));
            playerProfile.setMessageSounds(resultDocument.getBoolean("message_sounds", true));
            playerProfile.setDuelRequests(resultDocument.getBoolean("duel_requests", true));
            playerProfile.setDuelSounds(resultDocument.getBoolean("duel_sounds", true));
            playerProfile.setAllowSpectators(resultDocument.getBoolean("allow_spectators", true));
            playerProfile.setScoreBoard(resultDocument.getBoolean("score_board", true));
            playerProfile.setGlobalChat(resultDocument.getBoolean("global_chat", true));
            playerProfile.setPlayerWeather(WeatherType.valueOf(resultDocument.getString("player_weather").toUpperCase()));
            playerProfile.setPlayerTime(resultDocument.getInteger("player_time", 0));
            playerProfile.setPingRange(resultDocument.getInteger("ping_range", 100));
            playerProfile.setWinstreak(resultDocument.getInteger("win_streak", 0));
            playerProfile.setPartyInvites(resultDocument.getBoolean("party_requests", true));
            playerProfile.setPartySounds(resultDocument.getBoolean("party_sounds", true));
            playerProfile.setDeathEffect(DeathEffect.valueOf(resultDocument.getString("death_effect").toUpperCase()));
            
            Document kitDataDocument = resultDocument.get("kit_datas", Document.class);
            
            if(kitDataDocument != null){
                Map<Kit, PlayerKitProfile> kitDataMap = new HashMap<>();
                
                for(String kitName : kitDataDocument.keySet()){
                    Document kitData = kitDataDocument.get(kitName, Document.class);
                    Kit kit = KitManager.getKit(kitName);
                    
                    if(kitData == null || kit == null)
                        continue;
                    
                    PlayerKitProfile playerKitProfile = new PlayerKitProfile();
                    
                    playerKitProfile.setElo(kitData.getDouble("elo"));
                    playerKitProfile.setRankedWins(kitData.getInteger("rankedWins"));
                    playerKitProfile.setRankedLosses(kitData.getInteger("rankedLosses"));
                    playerKitProfile.setUnrankedWins(kitData.getInteger("unrankedWins"));
                    playerKitProfile.setUnrankedLosses(kitData.getInteger("unrankedLosses"));
                    playerKitProfile.setWinstreak(kitData.getInteger("winstreak"));
                    playerKitProfile.setKills(kitData.getInteger("kills"));
                    playerKitProfile.setDeaths(kitData.getInteger("deaths"));
                    playerKitProfile.setKitLayoutItems(GSON.fromJson(kitData.getString("kitLayoutItems"), ItemStack[].class));
                    
                    kitDataMap.put(kit, playerKitProfile);
                }
                
                kitDataMap.keySet().removeIf(kit -> KitManager.KITS.stream().noneMatch(kit1 -> kit1.getName().equalsIgnoreCase(kit.getName())));
                playerProfile.getKitDataList().putAll(kitDataMap);
            }
            
            Bukkit.getLogger().info("Loaded " + playerProfile.getUuid() + " profile in " + (System.currentTimeMillis() - now) + "ms!");
            
            return playerProfile;
        }
        
        Bukkit.getLogger().info("No player profile");
        
        return null;
    }
    
    public static List<String> getPlayers(Kit kit, boolean ranked){
        //TODO FINISH LEADERBOARDS
        /*String code = "SELECT * FROM player_data";
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
        
        return unsortedPlayers.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).map(Map.Entry::getKey).collect(Collectors.toList());*/
        return null;
    }
}
