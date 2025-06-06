package com.emeraldnetwork.emeraldPractice.utils;

import org.bukkit.Bukkit;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public final class WebhookUtils {

    public static final String WEBHOOK_URL = "https://discord.com/api/webhooks/1379084930927165440/ssyujFEh3nEZhE5VhoRzXQbymB0YjPus3jhFolq2iqt9Bi4ImbwARDMZ526AaUvB3OI0";

    public static void sendEmbed(String title, String description, String colorHex)  {
        String message = "{\"embeds\":[" + "{\"title\":\"" + title + "\"," + "\"description\":\"" + description.replace("\n", "\\n") + "\"," + "\"color\":" + Integer.parseInt(colorHex, 16) + "}]}";
        
        try{
            URL url = new URL(WEBHOOK_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setDoOutput(true);

            try(OutputStream outputStream = connection.getOutputStream()){
                byte[] input = message.getBytes("utf-8");

                outputStream.write(input, 0, input.length);
            }
            
            int responseCode = connection.getResponseCode();
            
            Bukkit.getLogger().info("code: " + responseCode);
            
            if(responseCode != 204){
                try(BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))){
                    StringBuilder builder = new StringBuilder();
                    String line;
                    
                    while((line = reader.readLine()) != null){
                        builder.append(line).append("\n");
                    }
                    
                    Bukkit.getLogger().info(line);
                }
            }

            connection.disconnect();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
