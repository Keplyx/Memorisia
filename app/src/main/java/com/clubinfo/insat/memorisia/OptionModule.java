package com.clubinfo.insat.memorisia;


import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.Image;

public class OptionModule {
    private int id;
    private String name;
    private int logo;
    private int optionColor;
    private boolean notificationsEnabled;
    
    public OptionModule(int id, String name, int logo, int optionColor, boolean notificationsEnabled) {
        this.id = id;
        this.name = name;
        this.logo = logo;
        this.optionColor = optionColor;
        this.notificationsEnabled = notificationsEnabled;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public int getLogo() {
        return logo;
    }
    
    public void setLogo(int logo) {
        this.logo = logo;
    }
    
    public int getOptionColor() {
        return optionColor;
    }
    
    public void setOptionColor(int optionColor) {
        this.optionColor = optionColor;
    }
    
    public boolean isNotificationsEnabled() {
        return notificationsEnabled;
    }
    
    public void setNotificationsEnabled(boolean notificationsEnabled) {
        this.notificationsEnabled = notificationsEnabled;
    }
}
