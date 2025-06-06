package com.emeraldnetwork.emeraldPractice.adapter;

import com.google.gson.*;

import java.io.File;
import java.lang.reflect.Type;

public class FileAdapter implements JsonSerializer<File>, JsonDeserializer<File>{
    
    @Override
    public JsonElement serialize(File src, Type typeOfSrc, JsonSerializationContext context){
        JsonObject object = new JsonObject();
        
        object.addProperty("path", src.getPath());
        
        return object;
    }
    
    @Override
    public File deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException{
        JsonObject object = json.getAsJsonObject();
        
        return new File(object.get("path").getAsString());
    }
}
