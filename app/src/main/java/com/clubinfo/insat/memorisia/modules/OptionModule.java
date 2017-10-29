package com.clubinfo.insat.memorisia.modules;


/**
 * Class representing an agenda, a subject, or a work type
 */
public class OptionModule extends Module{
    private int type;
    private String logo;
    private String color;
    
    public OptionModule(int id, int type, String text, String logo, String color, boolean notificationsEnabled) {
        super(id, text, notificationsEnabled);
        this.type = type;
        this.logo = logo;
        this.color = color;
    }
    
    public OptionModule(int type) {
        super(-1, "", false);
        this.type = type;
        this.logo = "";
        this.color = "#cccccc";
    }
    
    public int getType() {
        return type;
    }
    
    public void setType(int type) {
        this.type = type;
    }
    
    public String getLogo() {
        return logo;
    }
    
    public void setLogo(String logo) {
        this.logo = logo;
    }
    
    public String getColor() {
        return color;
    }
    
    public void setColor(String color) {
        this.color = color;
    }
}
