package com.emeraldnetwork.emeraldPractice.utils;

import org.bukkit.Bukkit;

import java.io.*;

public final class FileUtils{
    
    public static void copy(File source, File destination){
        if(source.isDirectory()){
            if(!destination.exists())
                destination.mkdir();
            
            String[] files = source.list();
            
            if(files == null)
                return;
            
            for(String file : files){
                File newSource = new File(source, file);
                File newDestination = new File(destination, file);
                copy(newSource, newDestination);
            }
        }else{
            try{
                InputStream inputStream = new FileInputStream(source);
                OutputStream outputStream = new FileOutputStream(destination);
                
                byte[] buffer = new byte[1024];
                int length;
                
                while((length = inputStream.read(buffer)) > 0){
                    outputStream.write(buffer, 0, length);
                }
                
                inputStream.close();
                outputStream.close();
            }catch(IOException ie){
                Bukkit.getLogger().severe(ie.getMessage());
            }
            
        }
    }
    
    public static void delete(File file){
        if(file.isDirectory()){
            File[] files = file.listFiles();
            
            if(files == null)
                return;
            
            for(File childFile : files){
                delete(childFile);
            }
        }
        
        file.delete();
    }
}
