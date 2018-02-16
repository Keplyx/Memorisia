package com.clubinfo.insat.memorisia.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.clubinfo.insat.memorisia.R;
import com.clubinfo.insat.memorisia.modules.OptionModule;
import com.clubinfo.insat.memorisia.utils.Utils;

import java.util.List;

public class CustomSpinnerAdapter extends ArrayAdapter<String> {
    private final LayoutInflater inflater;
    private final Context context;
    private final List<OptionModule> modules;
    private final int resource;
    
    public CustomSpinnerAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List objects) {
        super(context, resource, 0, objects);
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.resource = resource;
        this.modules = objects;
    }
    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }
    
    @Override
    public @NonNull View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }
    
    private View createItemView(int pos, View convertView, ViewGroup parent){
        final View view = inflater.inflate(resource, parent, false);
        
        TextView text = view.findViewById(R.id.spinnerTextView);
        text.setText(modules.get(pos).getText());
        ImageView image = view.findViewById(R.id.spinnerImageView);
        image.setImageBitmap(Utils.getBitmapFromAsset(context, modules.get(pos).getLogo()));
        image.setColorFilter(Color.parseColor(modules.get(pos).getColor()));
        return view;
    }
}
