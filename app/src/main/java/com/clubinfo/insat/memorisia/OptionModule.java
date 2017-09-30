package com.clubinfo.insat.memorisia;



public class OptionModule {
    private int id;
    private int type;
    private String name;
    private int logo;
    private String color;
    private boolean notificationsEnabled;
    
    
    public OptionModule(int id, int type, String name, int logo, String color, boolean notificationsEnabled) {
        this.id = id;
        this.type = type;
        
        this.name = name;
        this.logo = logo;
        this.color = color;
        this.notificationsEnabled = notificationsEnabled;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getType() {
        return type;
    }
    
    public void setType(int type) {
        this.type = type;
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
    
    public String getColor() {
        return color;
    }
    
    public void setColor(String color) {
        this.color = color;
    }
    
    public boolean isNotificationsEnabled() {
        return notificationsEnabled;
    }
    
    public void setNotificationsEnabled(boolean notificationsEnabled) {
        this.notificationsEnabled = notificationsEnabled;
    }
}
