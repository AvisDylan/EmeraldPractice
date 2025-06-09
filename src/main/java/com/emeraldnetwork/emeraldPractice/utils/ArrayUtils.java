package com.emeraldnetwork.emeraldPractice.utils;

public final class ArrayUtils{

    public static <T> T[] reverseArray(T[] array){
        for(int i = 0; i < array.length / 2; i++){
            T temp = array[i];
            array[i] = array[array.length - 1 - i];
            array[array.length - 1 - i] = temp;
        }
        
        return array;
    }
    
    public static <T> T[] reverseArrayVertically(T[] array){
        for(int i = 0; i < (array.length / 9) / 2; i++){
            for(int j = 0; j < 9; j++){
                int topIndex = i * 9 + j;
                int bottomIndex = ((array.length / 9) - 1 - i) * 9 + j;
                
                T temp = array[topIndex];
                array[topIndex] = array[bottomIndex];
                array[bottomIndex] = temp;
            }
        }
        
        return array;
    }
}
