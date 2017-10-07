package com.clubinfo.insat.memorisia.modules;


public class Module{
    private int id;
    private String text;
    private boolean notificationsEnabled;
    
    public Module(int id, String text, boolean notificationsEnabled) {
        this.id = id;
        this.text = text;
        this.notificationsEnabled = notificationsEnabled;
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
    
    public boolean isNotificationsEnabled() {
        return notificationsEnabled;
    }
    
    public void setNotificationsEnabled(boolean notificationsEnabled) {
        this.notificationsEnabled = notificationsEnabled;
    }
}
