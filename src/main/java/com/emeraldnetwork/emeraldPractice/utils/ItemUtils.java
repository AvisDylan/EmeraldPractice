package com.emeraldnetwork.emeraldPractice.utils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public final class ItemUtils{

    public static final ItemStack[] SPAWN_ITEMS = { createItem(Material.IRON_SWORD, 1, "§7Unranked Queue (right click)", true),
                                                        createItem(Material.DIAMOND_SWORD, 1, "§2Ranked Queue §7(right click)", true),
                                                        createItem(Material.GOLD_SWORD, 1, "§7FFA (right click)", true),
                                                        createItem(Material.SKULL_ITEM, 1, "§7Bot Queue (right click)"),
                                                        createItem(Material.NAME_TAG, 1, "§7Create a party (right click)"),
                                                        createItem(Material.BOOK, 1, "§7Kit editor (right click)"),
                                                        createItem(Material.WATCH, 1, "§7Tournament (right click)"),
                                                        createItem(Material.ITEM_FRAME, 1, "§7Leaderboards (right click)"),
                                                        createItem(Material.COMPASS, 1, "§7Settings (right click)") },
                                    QUEUE_ITEMS = { null,
                                                    null,
                                                    null,
                                                    null,
                                                    null,
                                                    null,
                                                    null,
                                                    null,
                                                    createItem(Material.INK_SACK, 1, (short) 1, "§cLeave Queue §7(right click)") },
            SPEC_ITEMS = { createItem(Material.COMPASS, 1, ChatColor.GRAY + "Teleport to a player (right click)"),
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            createItem(Material.INK_SACK, 1, (short) 1, ChatColor.RED + "Stop Spectating " + ChatColor.GRAY + "(right click)") };
    
    public static ItemStack createItem(Material material, int amount){
        return new ItemStack(material, amount);
    }
    
    public static ItemStack createItem(Material material, int amount, short health){
        return new ItemStack(material, amount, health);
    }
    
    public static ItemStack createItem(Material material, int amount, short health, String name){
        ItemStack itemStack = new ItemStack(material, amount, health);
        ItemMeta itemMeta = itemStack.getItemMeta();
        
        itemMeta.setDisplayName(name);
        itemStack.setItemMeta(itemMeta);
        
        return itemStack;
    }
    
    public static ItemStack createItem(Material material, int amount, short health, String name, String... lore){
        ItemStack itemStack = new ItemStack(material, amount, health);
        ItemMeta itemMeta = itemStack.getItemMeta();
        
        itemMeta.setDisplayName(name);
        itemMeta.setLore(Arrays.asList(lore));
        itemStack.setItemMeta(itemMeta);
        
        return itemStack;
    }
    
    public static ItemStack createItem(Material material, int amount, String name){
        ItemStack itemStack = new ItemStack(material, amount);
        ItemMeta itemMeta = itemStack.getItemMeta();
        
        itemMeta.setDisplayName(name);
        itemStack.setItemMeta(itemMeta);
        
        return itemStack;
    }
    
    public static ItemStack createItem(Material material, int amount, String name, String... lore){
        ItemStack itemStack = new ItemStack(material, amount);
        ItemMeta itemMeta = itemStack.getItemMeta();
        
        itemMeta.setDisplayName(name);
        itemMeta.setLore(Arrays.asList(lore));
        itemStack.setItemMeta(itemMeta);
        
        return itemStack;
    }
    
    public static ItemStack createItem(Material material, int amount, String name, boolean unbreakable){
        ItemStack itemStack = new ItemStack(material, amount);
        ItemMeta itemMeta = itemStack.getItemMeta();
        
        itemMeta.setDisplayName(name);
        itemMeta.spigot().setUnbreakable(unbreakable);
        itemStack.setItemMeta(itemMeta);
        
        return itemStack;
    }
    
    public static ItemStack createItem(Material material, int amount, String name, boolean unbreakable, String... lore){
        ItemStack itemStack = new ItemStack(material, amount);
        ItemMeta itemMeta = itemStack.getItemMeta();
        
        itemMeta.setDisplayName(name);
        itemMeta.setLore(Arrays.asList(lore));
        itemMeta.spigot().setUnbreakable(unbreakable);
        itemStack.setItemMeta(itemMeta);
        
        return itemStack;
    }
    
    public static ItemStack createGoldenHead(){
        ItemStack goldenHead = new ItemStack(Material.GOLDEN_APPLE, 1);
        ItemMeta itemMeta = goldenHead.getItemMeta();
        
        itemMeta.setDisplayName("§6Golden Head");
        goldenHead.setItemMeta(itemMeta);
        
        return goldenHead;
    }
}
