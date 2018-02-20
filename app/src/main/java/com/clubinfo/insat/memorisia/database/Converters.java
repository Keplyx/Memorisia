package com.clubinfo.insat.memorisia.database;


import android.arch.persistence.room.TypeConverter;

public class Converters {
    @TypeConverter
    public static int[] arrayFromString(String intString) {
        String[] intStringSplit = intString.split(" "); //Split by spaces
        int[] result = new int[intStringSplit.length];
    
        for (int i = 0; i < intStringSplit.length; i++) {
            result[i] = Integer.parseInt(intStringSplit[i]);
        }
        return result;
    }
    
    @TypeConverter
    public static String arrayToString(int[] array) {
        String result = "";
        for (int i = 0; i < array.length; i++){
            result += array[i] + " ";
        }
        return result;
    }
    
}
