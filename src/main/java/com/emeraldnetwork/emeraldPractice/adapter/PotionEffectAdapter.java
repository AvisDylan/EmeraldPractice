package com.emeraldnetwork.emeraldPractice.adapter;

import com.google.gson.*;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.lang.reflect.Type;

public class PotionEffectAdapter implements JsonSerializer<PotionEffect>, JsonDeserializer<PotionEffect>{
    
    @Override
    public JsonElement serialize(PotionEffect src, Type typeOfSrc, JsonSerializationContext context){
        JsonObject object = new JsonObject();
        
        object.add("type", context.serialize(src.getType()));
        object.addProperty("amplifier", src.getAmplifier());
        object.addProperty("duration", src.getDuration());
        
        return object;
    }
    
    @Override
    public PotionEffect deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException{
        JsonObject object = json.getAsJsonObject();
        
        PotionEffectType type = context.deserialize(object.get("type"), PotionEffectType.class);
        int amplifier = object.get("amplifier").getAsInt();
        int duration = object.get("duration").getAsInt();
        
        return new PotionEffect(type, amplifier, duration);
    }
}
