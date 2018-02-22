package com.clubinfo.insat.memorisia.modules;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;

/**
 * Class representing an agenda, a subject, or a work type
 */
@Entity (tableName = "option_modules")
public class OptionModule extends Module{
    private int type;
    private String logo;
    private String color;
    
    @Ignore
    public static final int SUBJECT = 0;
    @Ignore
    public static final int WORK_TYPE = 1;
    @Ignore
    public static final int AGENDA = 2;
    
    public OptionModule(int type, String text, String logo, String color) {
        super(text);
        this.type = type;
        this.logo = logo;
        this.color = color;
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
