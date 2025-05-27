package com.emeraldnetwork.emeraldPractice.kit;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class KitManager{
    
    public static final List<Kit> KITS = new CopyOnWriteArrayList<>();
    public static final List<Kit> RANKED_KITS = new ArrayList<>();
    
    public KitManager(){
        KITS.forEach(kit -> {
            if(kit.isRanked())
                RANKED_KITS.add(kit);
        });
    }
}
