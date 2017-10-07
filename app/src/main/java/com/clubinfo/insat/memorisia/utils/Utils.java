package com.clubinfo.insat.memorisia.utils;


import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;

import java.io.IOException;

public class Utils {

    public static Bitmap getBitmapFromAsset(Context context, String filePath) {
        if (filePath == null)
            return null;
        
        AssetManager assetManager = context.getAssets();
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(assetManager.open(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
    
}
