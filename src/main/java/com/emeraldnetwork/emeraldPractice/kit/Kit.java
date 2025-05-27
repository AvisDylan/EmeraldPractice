package com.emeraldnetwork.emeraldPractice.kit;

import com.emeraldnetwork.emeraldPractice.map.Map;
import com.google.gson.annotations.Expose;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.*;

public class Kit{
    
    @Expose
    private String name, displayName;
    @Expose
    private ItemStack[] editorItems, items, armourItems;
    private PotionEffect[] potionEffects;
    @Expose
    private ItemStack icon;
    @Expose
    private boolean ranked, enabled, editable, bedwars, boxing, noHitDelay, blocks, ffa, hunger, deathDrops, drop, fallDamage;
    @Expose
    private int rounds;
    @Expose
    private long maxDurationInSeconds;
    @Expose
    private final List<String> startCommand = new ArrayList<>();
    @Expose
    private final Set<Map> maps = new HashSet<>();
    
    public Kit(String name, String displayName, PotionEffect[] potionEffects, ItemStack[] editorItems, ItemStack[] items, ItemStack[] armourItems, ItemStack icon, boolean ranked, boolean enabled, boolean editable, boolean bedwars, boolean boxing, boolean noHitDelay, boolean blocks, int rounds, long maxDurationInSeconds, boolean ffa, boolean hunger, boolean deathDrops, boolean drop, boolean fallDamage){
        this.name = name;
        this.displayName = displayName;
        this.editorItems = editorItems;
        this.items = items;
        this.armourItems = armourItems;
        this.icon = icon;
        this.ranked = ranked;
        this.enabled = enabled;
        this.editable = editable;
        this.bedwars = bedwars;
        this.boxing = boxing;
        this.noHitDelay = noHitDelay;
        this.blocks = blocks;
        this.rounds = rounds;
        this.maxDurationInSeconds = maxDurationInSeconds;
        this.ffa = ffa;
        this.hunger = hunger;
        this.deathDrops = deathDrops;
        this.drop = drop;
        this.fallDamage = fallDamage;
        this.potionEffects = potionEffects;
    }
    
    public Kit(String name){
        this.name = name;
        displayName = name;
        icon = new ItemStack(Material.COBBLESTONE, 1);
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
        maxDurationInSeconds = 600;
        ffa = false;
        hunger = false;
        drop = false;
        deathDrops = false;
        fallDamage = false;
    }
    
    public void applyKit(Player player){
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        player.getInventory().setContents(items);
        player.getInventory().setArmorContents(armourItems);
        
        for(PotionEffect potionEffect : potionEffects){
            if(potionEffect != null)
                player.addPotionEffect(potionEffect);
        }
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
    
    public List<String> getStartCommand(){
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
    
    @Override
    public String toString(){
        return "Kit{" +
                "name='" + name + '\'' +
                ", displayName='" + displayName + '\'' +
                ", editorItems=" + Arrays.toString(editorItems) +
                ", items=" + Arrays.toString(items) +
                ", armourItems=" + Arrays.toString(armourItems) +
                ", icon=" + icon +
                ", ranked=" + ranked +
                ", enabled=" + enabled +
                ", editable=" + editable +
                ", bedwars=" + bedwars +
                ", boxing=" + boxing +
                ", noHitDelay=" + noHitDelay +
                ", breakBlocks=" + blocks +
                ", rounds=" + rounds +
                ", startCommand=" + startCommand +
                ", maps=" + maps +
                '}';
    }
    
    @Override
    public boolean equals(Object o){
        if(!(o instanceof Kit kit)) return false;
        return isRanked() == kit.isRanked() && isEnabled() == kit.isEnabled() && isEditable() == kit.isEditable() && isBedwars() == kit.isBedwars() && isBoxing() == kit.isBoxing() && isNoHitDelay() == kit.isNoHitDelay() && isBlocks() == kit.isBlocks() && getRounds() == kit.getRounds() && Objects.equals(getName(), kit.getName()) && Objects.equals(getDisplayName(), kit.getDisplayName()) && Objects.deepEquals(getEditorItems(), kit.getEditorItems()) && Objects.deepEquals(getItems(), kit.getItems()) && Objects.deepEquals(getArmourItems(), kit.getArmourItems()) && Objects.equals(getIcon(), kit.getIcon()) && Objects.equals(getStartCommand(), kit.getStartCommand()) && Objects.equals(getMaps(), kit.getMaps());
    }
    
    @Override
    public int hashCode(){
        return Objects.hash(getName(), getDisplayName(), Arrays.hashCode(getEditorItems()), Arrays.hashCode(getItems()), Arrays.hashCode(getArmourItems()), getIcon(), isRanked(), isEnabled(), isEditable(), isBedwars(), isBoxing(), isNoHitDelay(), isBlocks(), getRounds(), getStartCommand(), getMaps());
    }
}
