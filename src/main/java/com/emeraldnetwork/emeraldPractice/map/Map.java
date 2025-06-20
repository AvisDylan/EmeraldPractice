package com.emeraldnetwork.emeraldPractice.map;

import com.google.gson.annotations.Expose;
import org.bukkit.Bukkit;
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
    private float playerOneYaw, playerOnePitch, playerTwoYaw, playerTwoPitch;
    @Expose
    private final File worldFolder;
    
    public Map(File sourceWorldFolder, float playerTwoPitch, float playerTwoYaw, float playerOnePitch, float playerOneYaw, double playerTwoZ, double playerTwoY, double playerTwoX, double playerOneZ, double playerOneY, double playerOneX, ItemStack icon, String displayName, String name){
        worldFolder = sourceWorldFolder;
        this.playerTwoPitch = playerTwoPitch;
        this.playerTwoYaw = playerTwoYaw;
        this.playerOnePitch = playerOnePitch;
        this.playerOneYaw = playerOneYaw;
        this.playerTwoZ = playerTwoZ;
        this.playerTwoY = playerTwoY;
        this.playerTwoX = playerTwoX;
        this.playerOneZ = playerOneZ;
        this.playerOneY = playerOneY;
        this.playerOneX = playerOneX;
        this.icon = icon;
        this.displayName = displayName;
        this.name = name;
    }
    
    public Map(String name, String map){
        this.name = name;
        displayName = name;
        icon = new ItemStack(Material.COBBLESTONE, 1);
        worldFolder = new File(Bukkit.getWorldContainer().getParentFile(), map);
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
    
    public double getPlayerOneX(){
        return playerOneX;
    }
    
    public void setPlayerOneX(double playerOneX){
        this.playerOneX = playerOneX;
    }
    
    public double getPlayerOneY(){
        return playerOneY;
    }
    
    public void setPlayerOneY(double playerOneY){
        this.playerOneY = playerOneY;
    }
    
    public double getPlayerOneZ(){
        return playerOneZ;
    }
    
    public void setPlayerOneZ(double playerOneZ){
        this.playerOneZ = playerOneZ;
    }
    
    public double getPlayerTwoX(){
        return playerTwoX;
    }
    
    public void setPlayerTwoX(double playerTwoX){
        this.playerTwoX = playerTwoX;
    }
    
    public double getPlayerTwoY(){
        return playerTwoY;
    }
    
    public void setPlayerTwoY(double playerTwoY){
        this.playerTwoY = playerTwoY;
    }
    
    public double getPlayerTwoZ(){
        return playerTwoZ;
    }
    
    public void setPlayerTwoZ(double playerTwoZ){
        this.playerTwoZ = playerTwoZ;
    }
    
    public File getWorldFolder(){
        return worldFolder;
    }
    
    public float getPlayerOneYaw(){
        return playerOneYaw;
    }
    
    public void setPlayerOneYaw(float playerOneYaw){
        this.playerOneYaw = playerOneYaw;
    }
    
    public float getPlayerOnePitch(){
        return playerOnePitch;
    }
    
    public void setPlayerOnePitch(float playerOnePitch){
        this.playerOnePitch = playerOnePitch;
    }
    
    public float getPlayerTwoYaw(){
        return playerTwoYaw;
    }
    
    public void setPlayerTwoYaw(float playerTwoYaw){
        this.playerTwoYaw = playerTwoYaw;
    }
    
    public float getPlayerTwoPitch(){
        return playerTwoPitch;
    }
    
    public void setPlayerTwoPitch(float playerTwoPitch){
        this.playerTwoPitch = playerTwoPitch;
    }
}
