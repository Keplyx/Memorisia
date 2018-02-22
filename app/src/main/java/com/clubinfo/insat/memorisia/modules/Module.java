package com.clubinfo.insat.memorisia.modules;

import android.arch.persistence.room.PrimaryKey;

/**
 * Module that can contain an int, a String, and a boolean, serving as a base for more complexe modules
 */
public class Module {
    @PrimaryKey (autoGenerate = true)
    private int id;
    private String text;
    
    public Module(String text) {
        this.text = text;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getText() {
        return text;
    }
    
    public void setText(String text) {
        this.text = text;
    }
    
}
