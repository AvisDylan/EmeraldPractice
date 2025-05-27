package com.emeraldnetwork.emeraldPractice.map;

import com.emeraldnetwork.emeraldPractice.EmeraldPractice;
import com.google.gson.annotations.Expose;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.io.File;

public class Map{
    
    @Expose
    private String name, displayName;
    @Expose
    private ItemStack icon;
    @Expose
    private double playerOneX, playerOneY, playerOneZ, playerTwoX, playerTwoY, playerTwoZ;
    @Expose
    private final File mapSchematic;
    
    public Map(String name, String schematic){
        this.name = name;
        displayName = name;
        mapSchematic = new File("plugins/WorldEdit/schematics/" + schematic + ".schematic");
        icon = new ItemStack(Material.COBBLESTONE, 1);
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
    
    public ItemStack getIcon(){
        return icon;
    }
    
    public void setIcon(ItemStack icon){
        this.icon = icon;
    }
    
    public void setPlayerOneSpawn(double x, double y, double z){
        this.playerOneX = x;
        this.playerOneY = y;
        this.playerOneZ = z;
    }
    
    public void setPlayerTwoSpawn(double x, double y, double z){
        this.playerTwoX = x;
        this.playerTwoY = y;
        this.playerTwoZ = z;
    }
    
    public double getPlayerOneX(){
        return playerOneX;
    }
    
    public double getPlayerOneY(){
        return playerOneY;
    }
    
    public double getPlayerOneZ(){
        return playerOneZ;
    }
    
    public double getPlayerTwoX(){
        return playerTwoX;
    }
    
    public double getPlayerTwoY(){
        return playerTwoY;
    }
    
    public double getPlayerTwoZ(){
        return playerTwoZ;
    }
    
    public File getMapSchematic(){
        return mapSchematic;
    }
    
    @Override
    public String toString(){
        return "Map{" +
                "name='" + name + '\'' +
                ", displayName='" + displayName + '\'' +
                ", icon=" + icon +
                ", playerOneX=" + playerOneX +
                ", playerOneY=" + playerOneY +
                ", playerOneZ=" + playerOneZ +
                ", playerTwoX=" + playerTwoX +
                ", playerTwoY=" + playerTwoY +
                ", playerTwoZ=" + playerTwoZ +
                ", mapSchematic=" + mapSchematic +
                '}';
    }
}
