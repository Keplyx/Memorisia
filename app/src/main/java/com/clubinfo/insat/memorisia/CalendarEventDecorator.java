package com.clubinfo.insat.memorisia;

import android.graphics.Color;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;
import java.util.List;

public class CalendarEventDecorator implements DayViewDecorator {
    
    private final List<CalendarDay> dates;

    public CalendarEventDecorator(List<CalendarDay> dates) {
        this.dates = dates;
    }
    
    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.contains(day);
    }
    
    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new DotSpan(5, Color.RED));
    }
}
