package com.emeraldnetwork.emeraldPractice.adapter;

import com.google.common.reflect.TypeToken;
import com.google.gson.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.lang.reflect.Type;
import java.util.List;

public class ItemStackAdapter implements JsonSerializer<ItemStack>, JsonDeserializer<ItemStack>{
    
    @Override
    public JsonElement serialize(ItemStack src, Type typeOfSrc, JsonSerializationContext context){
        JsonObject object = new JsonObject();
        
        object.addProperty("material", src.getType().name());
        object.addProperty("amount", src.getAmount());
        
        if(src.getData() != null)
            object.add("data", context.serialize(src.getData()));
        
        object.addProperty("durability", src.getDurability());
        
        if(src.hasItemMeta()){
            JsonObject metaObject = new JsonObject();
            ItemMeta itemMeta = src.getItemMeta();
            
            if(itemMeta.hasDisplayName())
                metaObject.addProperty("display-name", itemMeta.getDisplayName());
            
            if(itemMeta.hasLore())
                metaObject.add("lore", new Gson().toJsonTree(itemMeta.getLore()));
            
            if(itemMeta.spigot().isUnbreakable())
                metaObject.addProperty("unbreakable", true);
            
            if(itemMeta.hasEnchants()){
                JsonObject enchantObject = new JsonObject();
                
                itemMeta.getEnchants().forEach((enchantment, level) -> enchantObject.addProperty(enchantment.getName(), level));
                metaObject.add("enchants", enchantObject);
            }
            
            if(!itemMeta.getItemFlags().isEmpty()){
                JsonArray flagArray = new JsonArray();
                
                itemMeta.getItemFlags().forEach(flag -> flagArray.add(new JsonPrimitive(flag.name())));
                
                metaObject.add("item-flags", flagArray);
            }
            
            if(itemMeta instanceof PotionMeta potionMeta){
                JsonArray potionArray = new JsonArray();
                
                potionMeta.getCustomEffects().forEach(potionEffect -> {
                    JsonObject effectObject = new JsonObject();
                    
                    effectObject.addProperty("type", potionEffect.getType().getName());
                    effectObject.addProperty("amplifier", potionEffect.getAmplifier());
                    effectObject.addProperty("duration", potionEffect.getDuration());
                    potionArray.add(effectObject);
                });
                
                object.addProperty("meta-type", "POTION");
                metaObject.add("custom-effects", potionArray);
            }
            
            object.add("meta", metaObject);
        }
        
        return object;
    }
    
    @Override
    public ItemStack deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException{
        JsonObject object = json.getAsJsonObject();
        
        Material material = Material.getMaterial(object.get("material").getAsString());
        int amount = object.get("amount").getAsInt();
        short durability = object.get("durability").getAsShort();
        
        ItemStack itemStack = new ItemStack(material, amount, durability);
        
        if(object.has("data")){
            MaterialData data = context.deserialize(object.get("data"), MaterialData.class);
            
            itemStack.setData(data);
        }
        
        if(object.has("meta")){
            JsonObject metaObject = object.getAsJsonObject("meta");
            ItemMeta itemMeta = Bukkit.getItemFactory().getItemMeta(material);
            
            if(metaObject.has("display-name"))
                itemMeta.setDisplayName(metaObject.get("display-name").getAsString());
            
            if(metaObject.has("lore")){
                List<String> lore = new Gson().fromJson(metaObject.get("lore"), new TypeToken<List<String>>(){}.getType());
                
                itemMeta.setLore(lore);
            }
            
            if(metaObject.has("unbreakable"))
                itemMeta.spigot().setUnbreakable(metaObject.get("unbreakable").getAsBoolean());
            
            if(metaObject.has("enchants")){
                JsonObject enchantObject = metaObject.getAsJsonObject("enchants");
                
                enchantObject.entrySet().forEach(entry -> {
                    Enchantment enchantment = Enchantment.getByName(entry.getKey());
                    int level = entry.getValue().getAsInt();
                    
                    if(enchantment != null)
                        itemMeta.addEnchant(enchantment, level, true);
                });
            }
            
            if(metaObject.has("item-flags")){
                JsonArray flagArray = metaObject.getAsJsonArray("item-flags");
                
                flagArray.forEach(flag -> {
                    ItemFlag itemFlag = ItemFlag.valueOf(flag.getAsString());
                    
                    itemMeta.addItemFlags(itemFlag);
                });
            }
            
            if(itemMeta instanceof PotionMeta potionMeta && metaObject.has("custom-effects")){
                JsonArray potionArray = metaObject.getAsJsonArray("custom-effects");
                
                potionArray.forEach(potionEffect -> {
                    JsonObject potionObject = potionEffect.getAsJsonObject();
                    PotionEffectType potionEffectType = PotionEffectType.getByName(potionObject.get("type").getAsString());
                    int amplifier = potionObject.get("amplifier").getAsInt();
                    int duration = potionObject.get("duration").getAsInt();
                    
                    if(potionEffectType != null)
                        potionMeta.addCustomEffect(new PotionEffect(potionEffectType, duration, amplifier), true);
                });
            }
            itemStack.setItemMeta(itemMeta);
        }
        
        return itemStack;
    }
}
