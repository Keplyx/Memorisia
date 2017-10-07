package com.clubinfo.insat.memorisia.modules;


public class WorkModule {
    private int id;
    private int agendaId;
    private int subjectId;
    private int workTypeId;
    private int priority;
    private String description;
    private boolean notificationsEnabled;
    private boolean state;
    
    public WorkModule(int id, int agendaId, int subjectId, int workTypeId, int priority,
                      String description, boolean notificationsEnabled, boolean state) {
        this.id = id;
        this.agendaId = agendaId;
        this.subjectId = subjectId;
        this.workTypeId = workTypeId;
        this.priority = priority;
        this.description = description;
        this.notificationsEnabled = notificationsEnabled;
        this.state = state;
        
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getAgendaId() {
        return agendaId;
    }
    
    public void setAgendaId(int agendaId) {
        this.agendaId = agendaId;
    }
    
    public int getSubjectId() {
        return subjectId;
    }
    
    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }
    
    public int getWorkTypeId() {
        return workTypeId;
    }
    
    public void setWorkTypeId(int workTypeId) {
        this.workTypeId = workTypeId;
    }
    
    public int getPriority() {
        return priority;
    }
    
    public void setPriority(int priority) {
        this.priority = priority;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public boolean isNotificationsEnabled() {
        return notificationsEnabled;
    }
    
    public void setNotificationsEnabled(boolean notificationsEnabled) {
        this.notificationsEnabled = notificationsEnabled;
    }
    
    public boolean isState() {
        return state;
    }
    
    public void setState(boolean state) {
        this.state = state;
    }
}
