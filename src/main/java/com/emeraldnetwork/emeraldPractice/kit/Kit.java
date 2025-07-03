package com.emeraldnetwork.emeraldPractice.kit;

import com.emeraldnetwork.emeraldPractice.map.Map;
import com.emeraldnetwork.emeraldPractice.math.BoundingBox;
import com.emeraldnetwork.emeraldPractice.player.PlayerData;
import com.emeraldnetwork.emeraldPractice.player.PlayerManager;
import com.emeraldnetwork.emeraldPractice.profile.PlayerKitProfile;
import com.google.gson.annotations.Expose;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class Kit{
    
    @Expose
    private String name, displayName, kbProfile;
    @Expose
    private ItemStack[] editorItems, items, armourItems;
    @Expose
    private PotionEffect[] potionEffects;
    @Expose
    private ItemStack icon;
    @Expose
    private BoundingBox buildableArea;
    @Expose
    private boolean ranked, enabled, editable, bedwars, boxing, noHitDelay, blocks, ffa, hunger, deathDrops, drop, fallDamage, voidInstaKill, healthRegen;
    @Expose
    private int rounds, maxBuildHeight, minBuildHeight;
    @Expose
    private long maxDurationInSeconds;
    @Expose
    private final Set<String> startCommand = new HashSet<>();
    @Expose
    private final Set<Map> maps = new HashSet<>();
    @Expose
    private Location ffaLocation;
    private final List<PlayerData> ffaPlayers = new CopyOnWriteArrayList<>();
    private final List<String> topUnrankedPlayers = new CopyOnWriteArrayList<>();
    private final List<String> topRankedPlayers = new CopyOnWriteArrayList<>();
    
    public Kit(String name, String displayName, PotionEffect[] potionEffects, ItemStack[] editorItems, ItemStack[] items, ItemStack[] armourItems, ItemStack icon, BoundingBox buildableArea, boolean ranked, boolean enabled, boolean editable, boolean bedwars, boolean boxing, boolean noHitDelay, boolean blocks, int rounds, int maxBuildHeight, int minBuildHeight, long maxDurationInSeconds, boolean ffa, boolean hunger, boolean deathDrops, boolean drop, boolean fallDamage, boolean voidInstaKill, String kbProfile, boolean healthRegen, Location ffaLocation){
        this.name = name;
        this.displayName = displayName;
        this.editorItems = editorItems;
        this.items = items;
        this.armourItems = armourItems;
        this.icon = icon;
        this.buildableArea = buildableArea;
        this.ranked = ranked;
        this.enabled = enabled;
        this.editable = editable;
        this.bedwars = bedwars;
        this.boxing = boxing;
        this.noHitDelay = noHitDelay;
        this.blocks = blocks;
        this.rounds = rounds;
        this.maxBuildHeight = maxBuildHeight;
        this.minBuildHeight = minBuildHeight;
        this.maxDurationInSeconds = maxDurationInSeconds;
        this.ffa = ffa;
        this.hunger = hunger;
        this.deathDrops = deathDrops;
        this.drop = drop;
        this.fallDamage = fallDamage;
        this.potionEffects = potionEffects;
        this.voidInstaKill = voidInstaKill;
        this.kbProfile = kbProfile;
        this.healthRegen = healthRegen;
        this.ffaLocation = ffaLocation;
    }
    
    public Kit(String name){
        this.name = name;
        displayName = name;
        icon = new ItemStack(Material.COBBLESTONE, 1);
        buildableArea = null;
        potionEffects = new PotionEffect[13];
        editorItems = new ItemStack[36];
        items = new ItemStack[36];
        armourItems = new ItemStack[4];
        ranked = true;
        enabled = true;
        editable = true;
        bedwars = false;
        boxing = false;
        noHitDelay = false;
        blocks = false;
        rounds = 0;
        maxBuildHeight = 12;
        minBuildHeight = 12;
        maxDurationInSeconds = 600;
        ffa = false;
        hunger = false;
        drop = false;
        deathDrops = false;
        fallDamage = false;
        voidInstaKill = true;
        healthRegen = true;
        kbProfile = "default";
        ffaLocation = new Location(Bukkit.getWorld("world"), 0, 0, 0);
    }
    
    public void applyKit(Player player){
        PlayerKitProfile playerKitProfile = PlayerManager.getPlayerData(player.getUniqueId()).getProfile().getKitProfile(this);
        
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        player.getInventory().setContents(playerKitProfile.getKitLayoutItems() != null ? playerKitProfile.getKitLayoutItems() : items);
        player.getInventory().setArmorContents(armourItems);
        
        /*for(PotionEffect potionEffect : potionEffects){
            if(potionEffect != null)
                player.addPotionEffect(potionEffect);
        }*/
    }
    
    public String getName(){
        return name;
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    public String getDisplayName(){
        return displayName;
    }
    
    public void setDisplayName(String displayName){
        this.displayName = displayName;
    }
    
    public PotionEffect[] getPotionEffects(){
        return potionEffects;
    }
    
    public void setPotionEffects(PotionEffect[] potionEffects){
        this.potionEffects = potionEffects;
    }
    
    public ItemStack[] getEditorItems(){
        return editorItems;
    }
    
    public void setEditorItems(ItemStack[] editorItems){
        this.editorItems = editorItems;
    }
    
    public ItemStack[] getItems(){
        return items;
    }
    
    public void setItems(ItemStack[] items){
        this.items = items;
    }
    
    public ItemStack[] getArmourItems(){
        return armourItems;
    }
    
    public void setArmourItems(ItemStack[] armourItems){
        this.armourItems = armourItems;
    }
    
    public ItemStack getIcon(){
        return icon;
    }
    
    public void setIcon(ItemStack icon){
        this.icon = icon;
    }
    
    public boolean isRanked(){
        return ranked;
    }
    
    public void setRanked(boolean ranked){
        this.ranked = ranked;
    }
    
    public boolean isEnabled(){
        return enabled;
    }
    
    public void setEnabled(boolean enabled){
        this.enabled = enabled;
    }
    
    public boolean isEditable(){
        return editable;
    }
    
    public void setEditable(boolean editable){
        this.editable = editable;
    }
    
    public void addMap(Map map){
        maps.add(map);
    }
    
    public void removeMap(Map map){
        maps.remove(map);
    }
    
    public Set<Map> getMaps(){
        return maps;
    }
    
    public boolean isBedwars(){
        return bedwars;
    }
    
    public void setBedwars(boolean bedwars){
        this.bedwars = bedwars;
    }
    
    public boolean isBoxing(){
        return boxing;
    }
    
    public void setBoxing(boolean boxing){
        this.boxing = boxing;
    }
    
    public boolean isNoHitDelay(){
        return noHitDelay;
    }
    
    public void setNoHitDelay(boolean noHitDelay){
        this.noHitDelay = noHitDelay;
    }
    
    public boolean isBlocks(){
        return blocks;
    }
    
    public void setBlocks(boolean blocks){
        this.blocks = blocks;
    }
    
    public int getRounds(){
        return rounds;
    }
    
    public void setRounds(int rounds){
        this.rounds = rounds;
    }
    
    public Set<String> getStartCommand(){
        return startCommand;
    }
    
    public long getMaxDurationInSeconds(){
        return maxDurationInSeconds;
    }
    
    public void setMaxDurationInSeconds(long maxDurationInSeconds){
        this.maxDurationInSeconds = maxDurationInSeconds;
    }
    
    public boolean isFfa(){
        return ffa;
    }
    
    public void setFfa(boolean ffa){
        this.ffa = ffa;
    }
    
    public boolean isHunger(){
        return hunger;
    }
    
    public void setHunger(boolean hunger){
        this.hunger = hunger;
    }
    
    public boolean isDeathDrops(){
        return deathDrops;
    }
    
    public void setDeathDrops(boolean deathDrops){
        this.deathDrops = deathDrops;
    }
    
    public boolean isDrop(){
        return drop;
    }
    
    public void setDrop(boolean drop){
        this.drop = drop;
    }
    
    public boolean isFallDamage(){
        return fallDamage;
    }
    
    public void setFallDamage(boolean fallDamage){
        this.fallDamage = fallDamage;
    }
    
    public BoundingBox getBuildableArea(){
        return buildableArea;
    }
    
    public void setBuildableArea(BoundingBox buildableArea){
        this.buildableArea = buildableArea;
    }
    
    public boolean isVoidInstaKill(){
        return voidInstaKill;
    }
    
    public void setVoidInstaKill(boolean voidInstaKill){
        this.voidInstaKill = voidInstaKill;
    }
    
    public int getMaxBuildHeight(){
        return maxBuildHeight;
    }
    
    public void setMaxBuildHeight(int maxBuildHeight){
        this.maxBuildHeight = maxBuildHeight;
    }
    
    public int getMinBuildHeight(){
        return minBuildHeight;
    }
    
    public void setMinBuildHeight(int minBuildHeight){
        this.minBuildHeight = minBuildHeight;
    }
    
    public String getKbProfile(){
        return kbProfile;
    }
    
    public void setKbProfile(String kbProfile){
        this.kbProfile = kbProfile;
    }
    
    public boolean isHealthRegen(){
        return healthRegen;
    }
    
    public Location getFfaLocation(){
        return ffaLocation;
    }
    
    public List<PlayerData> getFfaPlayers(){
        return ffaPlayers;
    }
    
    public void setFfaLocation(Location ffaLocation){
        this.ffaLocation = ffaLocation;
    }
    
    public void setHealthRegen(boolean healthRegen){
        this.healthRegen = healthRegen;
    }
    
    public List<String> getTopUnrankedPlayers(){
        return topUnrankedPlayers;
    }
    
    public List<String> getTopRankedPlayers(){
        return topRankedPlayers;
    }
    
    public int getPlaceInUnrankedLeaderboard(PlayerData playerData){
        Player player = Bukkit.getPlayer(playerData.getUuid());
        
        return topUnrankedPlayers.indexOf(player.getName());
    }
    
    public int getPlaceInRankedLeaderboard(PlayerData playerData){
        Player player = Bukkit.getPlayer(playerData.getUuid());
        
        return topRankedPlayers.indexOf(player.getName());
    }
    
    @Override
    public boolean equals(Object o){
        if(!(o instanceof Kit kit)) return false;
        return isRanked() == kit.isRanked() && isEnabled() == kit.isEnabled() && isEditable() == kit.isEditable() && isBedwars() == kit.isBedwars() && isBoxing() == kit.isBoxing() && isNoHitDelay() == kit.isNoHitDelay() && isBlocks() == kit.isBlocks() && isFfa() == kit.isFfa() && isHunger() == kit.isHunger() && isDeathDrops() == kit.isDeathDrops() && isDrop() == kit.isDrop() && isFallDamage() == kit.isFallDamage() && isVoidInstaKill() == kit.isVoidInstaKill() && isHealthRegen() == kit.isHealthRegen() && getRounds() == kit.getRounds() && getMaxBuildHeight() == kit.getMaxBuildHeight() && getMinBuildHeight() == kit.getMinBuildHeight() && getMaxDurationInSeconds() == kit.getMaxDurationInSeconds() && Objects.equals(getName(), kit.getName()) && Objects.equals(getDisplayName(), kit.getDisplayName()) && Objects.equals(getKbProfile(), kit.getKbProfile()) && Objects.deepEquals(getEditorItems(), kit.getEditorItems()) && Objects.deepEquals(getItems(), kit.getItems()) && Objects.deepEquals(getArmourItems(), kit.getArmourItems()) && Objects.deepEquals(getPotionEffects(), kit.getPotionEffects()) && Objects.equals(getIcon(), kit.getIcon()) && Objects.equals(getBuildableArea(), kit.getBuildableArea()) && Objects.equals(getStartCommand(), kit.getStartCommand()) && Objects.equals(getMaps(), kit.getMaps()) && Objects.equals(getFfaLocation(), kit.getFfaLocation());
    }
    
    @Override
    public int hashCode(){
        return Objects.hash(getName(), getDisplayName(), getKbProfile(), Arrays.hashCode(getEditorItems()), Arrays.hashCode(getItems()), Arrays.hashCode(getArmourItems()), Arrays.hashCode(getPotionEffects()), getIcon(), getBuildableArea(), isRanked(), isEnabled(), isEditable(), isBedwars(), isBoxing(), isNoHitDelay(), isBlocks(), isFfa(), isHunger(), isDeathDrops(), isDrop(), isFallDamage(), isVoidInstaKill(), isHealthRegen(), getRounds(), getMaxBuildHeight(), getMinBuildHeight(), getMaxDurationInSeconds(), getStartCommand(), getMaps(), getFfaLocation());
    }
    
    @Override
    public String toString(){
        return "Kit{" +
                "ffaLocation=" + ffaLocation +
                ", maps=" + maps +
                ", startCommand=" + startCommand +
                ", maxDurationInSeconds=" + maxDurationInSeconds +
                ", minBuildHeight=" + minBuildHeight +
                ", maxBuildHeight=" + maxBuildHeight +
                ", rounds=" + rounds +
                ", healthRegen=" + healthRegen +
                ", voidInstaKill=" + voidInstaKill +
                ", fallDamage=" + fallDamage +
                ", drop=" + drop +
                ", deathDrops=" + deathDrops +
                ", hunger=" + hunger +
                ", ffa=" + ffa +
                ", blocks=" + blocks +
                ", noHitDelay=" + noHitDelay +
                ", boxing=" + boxing +
                ", bedwars=" + bedwars +
                ", editable=" + editable +
                ", enabled=" + enabled +
                ", ranked=" + ranked +
                ", buildableArea=" + buildableArea +
                ", icon=" + icon +
                ", potionEffects=" + Arrays.toString(potionEffects) +
                ", armourItems=" + Arrays.toString(armourItems) +
                ", items=" + Arrays.toString(items) +
                ", editorItems=" + Arrays.toString(editorItems) +
                ", kbProfile='" + kbProfile + '\'' +
                ", displayName='" + displayName + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
