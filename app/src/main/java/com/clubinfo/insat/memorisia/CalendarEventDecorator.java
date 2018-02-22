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
