package com.clubinfo.insat.memorisia;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


public class OptionsRecyclerAdapter extends RecyclerView.Adapter<OptionsRecyclerAdapter.ViewHolder> {

    private List<OptionModule> modules;

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView text;
        public TextView rowColor;
        public ImageView logo;
        public View layout;

        public ViewHolder(View v){
            super(v);
            layout = v;
            text = v.findViewById(R.id.optionTitle);
            rowColor = v.findViewById(R.id.optionColor);
            logo = v.findViewById(R.id.optionLogo);
        }
    }

    public void add(int pos, OptionModule item){
        modules.add(pos, item);
        notifyItemInserted(pos);
    }

    public void remove(int pos){
        modules.remove(pos);
        notifyItemRemoved(pos);
    }

    public OptionsRecyclerAdapter(List<OptionModule> myDataset){
        modules = myDataset;
    }

    @Override
    public OptionsRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.optionslist_row_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder (ViewHolder holder, final int pos){
        final String name = modules.get(pos).getName();
        final int logo = modules.get(pos).getLogo();
        final int color = modules.get(pos).getOptionColor();
        holder.text.setText(name);
        holder.rowColor.setBackgroundColor(color);
        holder.logo.setImageResource(logo);
    }

    @Override
    public int getItemCount(){
        return modules.size();
    }
}
