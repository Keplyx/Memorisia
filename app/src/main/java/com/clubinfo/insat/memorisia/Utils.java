package com.clubinfo.insat.memorisia;


import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;

import java.io.IOException;

public class Utils {

    public static Bitmap getBitmapFromAsset(Context context, String filePath) {
        AssetManager assetManager = context.getAssets();
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(assetManager.open(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
    
    public static Drawable getDrawableFromAsset(Context context, String filePath){
        AssetManager assetManager = context.getAssets();
        Drawable drawable = null;
        try {
            drawable = Drawable.createFromStream(assetManager.open(filePath), null);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return drawable;
    }
    
}