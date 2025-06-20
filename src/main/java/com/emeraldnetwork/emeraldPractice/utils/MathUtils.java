package com.emeraldnetwork.emeraldPractice.utils;

import java.util.Arrays;

public final class MathUtils{
    
    public static int getMean(int... nums){
        return Arrays.stream(nums).sum() / nums.length;
    }
    
    public static double getMean(double... nums){
        return Arrays.stream(nums).sum() / nums.length;
    }
    
    public static int getMedian(int... nums){
        Arrays.sort(nums);
        
        return nums.length % 2 == 0 ? nums[nums.length / 2] : (nums[nums.length / 2 - 1] + nums[nums.length / 2]) / 2;
    }
    
    public static double getMedian(double... nums){
        Arrays.sort(nums);
        
        return nums.length % 2 == 0 ? nums[nums.length / 2] : (nums[nums.length / 2 - 1] + nums[nums.length / 2]) / 2;
    }
    
    public static float roundYaw(float yaw){
        yaw = ((yaw + 180) % 360 + 360) % 360 - 180;
        return Math.round(yaw / 90.0f) * 90.0f;
    }
    
    public static float roundPitch(float pitch){
        pitch = Math.max(-90, Math.min(90, pitch));
        return Math.round(pitch / 45.0f) * 45f;
    }
}
