/*
 * Copyright (c) 2018.
 * This file is part of Memorisia.
 *
 * Memorisia is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Memorisia is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Memorisia.  If not, see <http://www.gnu.org/licenses/>.
 */

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
