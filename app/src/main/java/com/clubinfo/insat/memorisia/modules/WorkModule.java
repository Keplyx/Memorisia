/*
 * Copyright (c) 2018.
 * This file is part of Memorisia.
 *
 * Memorisia is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Memorisia is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Memorisia.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.clubinfo.insat.memorisia.modules;

import android.arch.persistence.room.Entity;

import java.util.Calendar;

/**
 * Class representing a work
 */
@Entity (tableName = "work_modules")
public class WorkModule extends Module {
    private int agendaId;
    private int subjectId;
    private int workTypeId;
    private int priority;
    private int[] date;
    private int[] time;
    private boolean state;
    
    public WorkModule(int agendaId, int subjectId, int workTypeId, int priority, int[] date, int[] time,
                      String text, boolean state) {
        super(text);
        this.agendaId = agendaId;
        this.subjectId = subjectId;
        this.workTypeId = workTypeId;
        this.priority = priority;
        this.date = date;
        this.time = time;
        
        this.state = state;
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
    
    /**
     * Format : day of month / month / year
     *
     * @return array representing the due date
     */
    public int[] getDate() {
        return date;
    }
    
    /**
     * Gets the date/time as a Calendar Object
     *
     * @return Calendar representing the due date/time
     */
    public Calendar getDateAsCalendar() {
        Calendar cal = Calendar.getInstance();
        if (date[0] != -1 && time[0] != -1)
            cal.set(date[2], date[1] - 1, date[0], time[0], time[1]);
        else if (date[0] != -1)
            cal.set(date[2], date[1] - 1, date[0], 0, 0);
        else
            cal = null;
        return cal;
    }
    
    /**
     * Format : day of month / month / year
     *
     * @param date array representing the due date
     */
    public void setDate(int[] date) {
        this.date = date;
    }
    
    /**
     * Format : hour of day / minute
     *
     * @return array representing the time
     */
    public int[] getTime() {
        return time;
    }
    
    /**
     * Format : hour of day / minute
     *
     * @param time array representing the time
     */
    public void setTime(int[] time) {
        this.time = time;
    }
    
    public boolean isState() {
        return state;
    }
    
    public void setState(boolean state) {
        this.state = state;
    }
}
