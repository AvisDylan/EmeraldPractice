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
}
