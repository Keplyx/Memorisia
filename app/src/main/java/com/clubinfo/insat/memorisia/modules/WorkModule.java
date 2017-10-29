package com.clubinfo.insat.memorisia.modules;

/**
 * Class representing a work
 */
public class WorkModule extends Module {
    private int agendaId;
    private int subjectId;
    private int workTypeId;
    private int priority;
    private boolean state;
    
    public WorkModule(int id, int agendaId, int subjectId, int workTypeId, int priority,
                      String text, boolean notificationsEnabled, boolean state) {
        super(id, text, notificationsEnabled);
        this.agendaId = agendaId;
        this.subjectId = subjectId;
        this.workTypeId = workTypeId;
        this.priority = priority;
        this.state = state;
    }
    
    public WorkModule() {
        super(-1, "", false);
        agendaId = -1;
        subjectId = -1;
        workTypeId = -1;
        priority = 0;
        state = false;
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
    
    public boolean isState() {
        return state;
    }
    
    public void setState(boolean state) {
        this.state = state;
    }
}
