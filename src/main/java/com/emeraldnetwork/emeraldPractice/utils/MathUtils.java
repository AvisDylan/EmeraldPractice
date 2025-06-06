package com.emeraldnetwork.emeraldPractice.utils;

import java.util.Arrays;

public class MathUtils{
    
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
}
