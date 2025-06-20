package com.emeraldnetwork.emeraldPractice.utils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import xyz.krypton.spigot.SatireSpigotAPI;
import xyz.krypton.spigot.knockback.KnockbackAPI;

import java.util.Arrays;

public final class ItemUtils{

    public static final ItemStack[] SPAWN_ITEMS = { createItem(Material.IRON_SWORD, 1, "§7Unranked Queue (right click)", true),
                                                        createItem(Material.DIAMOND_SWORD, 1, "§2Ranked Queue §7(right click)", true),
                                                        createItem(Material.GOLD_SWORD, 1, "§7FFA (right click)", true),
                                                        createSkull(1, "§7Bot Queue (right click)"),
                                                        createItem(Material.NAME_TAG, 1, "§7Create a Party (right click)"),
                                                        createItem(Material.BOOK, 1, "§7Kit Editor (right click)"),
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
                                    SPEC_ITEMS = { createItem(Material.COMPASS, 1, ChatColor.GRAY + "Teleport to a Player (right click)"),
                                                    null,
                                                    null,
                                                    null,
                                                    null,
                                                    null,
                                                    null,
                                                    null,
                                                    createItem(Material.INK_SACK, 1, (short) 1, ChatColor.RED + "Stop Spectating " + ChatColor.GRAY + "(right click)") },
                                    PARTY_ITEMS = { createItem(Material.IRON_AXE, 1, ChatColor.GRAY + "Start a Party Fight (right click)", true),
                                                    createItem(Material.DIAMOND_AXE, 1, ChatColor.DARK_GREEN + "Fight Another Party" + ChatColor.GRAY + " (right click)", true),
                                                    createItem(Material.PAPER, 1, ChatColor.GRAY + "Party List (right click)"),
                                                    null,
                                                    null,
                                                    null,
                                                    createItem(Material.BOOK, 1, "§7Kit Editor (right click)"),
                                                    createItem(Material.COMPASS, 1, "§7Settings (right click)"),
                                                    createItem(Material.INK_SACK, 1, (short) 1, ChatColor.RED + "Leave Party " + ChatColor.GRAY + "(right click)") };
    ;
    
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
    
    public static ItemStack createSkull(int amount, String name){
        ItemStack playerHead = new ItemStack(Material.SKULL_ITEM, amount, (short) 3);
        SkullMeta skullMeta = (SkullMeta) playerHead.getItemMeta();
        
        skullMeta.setDisplayName(name);
        playerHead.setItemMeta(skullMeta);
        
        return playerHead;
    }
    
    public static ItemStack createSkull(Player owner, int amount, String name){
        ItemStack playerHead = new ItemStack(Material.SKULL_ITEM, amount, (short) 3);
        SkullMeta skullMeta = (SkullMeta) playerHead.getItemMeta();
        
        skullMeta.setDisplayName(name);
        skullMeta.setOwner(owner.getName());
        playerHead.setItemMeta(skullMeta);
        
        return playerHead;
    }
    
    public static ItemStack createGoldenHead(){
        return createItem(Material.GOLDEN_APPLE, 1, ChatColor.GOLD + "Golden Head");
    }
    
    public static ItemStack createFillerItem(){
        return createItem(Material.STAINED_GLASS_PANE, 1, (short) 7, " ");
    }
}
