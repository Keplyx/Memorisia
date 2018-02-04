package com.clubinfo.insat.memorisia;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class CustomFrameLayout extends FrameLayout{
    
    public CustomFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    
    public float getXFraction() {
        return getX() / getWidth(); // TODO: guard divide-by-zero
    }
    
    public void setXFraction(float xFraction) {
        // TODO: cache width
        final int width = getWidth();
        setX((width > 0) ? (xFraction * width) : -9999);
    }
}
