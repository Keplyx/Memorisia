package com.clubinfo.insat.memorisia.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;

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
    
    // create a new button for each item referenced by the Adapter
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ImageButton button;
        button = new ImageButton(context);
        button.setLayoutParams(new GridView.LayoutParams(200, 200));
        button.setImageBitmap(Utils.getBitmapFromAsset(context, logosList.get(position)));
        button.setBackgroundColor(Color.TRANSPARENT);
        if (logosList.get(position).equals(selected))
            button.setColorFilter(Color.parseColor(color));
        else
            button.setColorFilter(Color.GRAY);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i =0; i < buttonsList.size(); i++){
                    buttonsList.get(i).setColorFilter(Color.GRAY);
                }
                button.setColorFilter(Color.parseColor(color));
                EditOptionsActivity.setSelectedLogo(logosList.get(position));
            }
        });

        buttonsList.add(button);
        return button;
    }
}
