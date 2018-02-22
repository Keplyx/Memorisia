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

package com.clubinfo.insat.memorisia.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;

import com.clubinfo.insat.memorisia.R;
import com.clubinfo.insat.memorisia.activities.EditOptionsActivity;
import com.clubinfo.insat.memorisia.utils.Utils;

import java.util.ArrayList;
import java.util.List;


public class LogosListAdapter extends BaseAdapter {
    
    private Context context;
    private List<String> logosList;
    private String selected;
    private String color;
    
    private List<ImageButton> buttonsList = new ArrayList<>();
    
    public LogosListAdapter(Context context, List<String> logosList, String selected, String color) {
        this.context = context;
        this.logosList = logosList;
        this.selected = selected;
        this.color = color;
    }
    
    public int getCount() {
        return logosList.size();
    }
    
    public Object getItem(int position) {
        return null;
    }
    
    public long getItemId(int position) {
        return 0;
    }
    
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ImageButton button = new ImageButton(context);
        int size = (int) context.getResources().getDimension(R.dimen.logo_size);
        
        button.setLayoutParams(new GridView.LayoutParams(size, size));
        button.setImageBitmap(Utils.getBitmapFromAsset(context, logosList.get(position)));
        button.setBackgroundColor(Color.TRANSPARENT);
        if (logosList.get(position).equals(selected))
            button.setColorFilter(Color.parseColor(color));
        else
            button.setColorFilter(Color.GRAY);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < buttonsList.size(); i++) {
                    buttonsList.get(i).setColorFilter(Color.GRAY);
                }
                button.setColorFilter(Color.parseColor(color));
                EditOptionsActivity act = (EditOptionsActivity) context;
                act.setSelectedLogo(logosList.get(position));
            }
        });
        buttonsList.add(button);
        return button;
    }
}
