package com.emeraldnetwork.emeraldPractice.math;

public class BoundingBox{
    
    private Vec3 min, max;
    
    public BoundingBox(Vec3 min, Vec3 max){
        this.min = min;
        this.max = max;
    }
    
    public Vec3 getMin(){
        return min;
    }
    
    public void setMin(Vec3 min){
        this.min = min;
    }
    
    public Vec3 getMax(){
        return max;
    }
    
    public void setMax(Vec3 max){
        this.max = max;
    }
    
    public static boolean containsVector(BoundingBox box, Vec3 vec3){
        return vec3.getX() >= box.getMin().getX() && vec3.getX() <= box.getMax().getX() &&
                vec3.getY() >= box.getMin().getY() && vec3.getY() <= box.getMax().getY() &&
                vec3.getZ() >= box.getMin().getZ() && vec3.getZ() <= box.getMax().getZ();
    }
}
