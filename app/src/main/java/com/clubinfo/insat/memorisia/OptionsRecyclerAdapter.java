package com.clubinfo.insat.memorisia;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.solver.SolverVariable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


public class OptionsRecyclerAdapter extends RecyclerView.Adapter<OptionsRecyclerAdapter.ViewHolder> {

    private List<OptionModule> modules;
    private Context context;
    
    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView text;
        public TextView rowColor;
        public ImageView logo;
        public ImageButton editButton;
        public View layout;

        public ViewHolder(View v){
            super(v);
            layout = v;
            text = v.findViewById(R.id.optionTitle);
            rowColor = v.findViewById(R.id.optionColor);
            logo = v.findViewById(R.id.optionLogo);
            editButton = v.findViewById(R.id.editWork);
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

    public OptionsRecyclerAdapter(List<OptionModule> myDataset, Context context){
        modules = myDataset;
        this.context = context;
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
        final int type = modules.get(pos).getType();
        final boolean notifications = modules.get(pos).isNotificationsEnabled();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EditOptionsActivity.class);
                Bundle b = new Bundle();
                b.putString("name", name);
                b.putInt("logo", logo);
                b.putInt("color", color);
                b.putInt("type", type);
                b.putBoolean("notifications", notifications);
                intent.putExtras(b);
                context.startActivity(intent);
            }
        });
        
        holder.text.setText(name);
        holder.logo.setImageResource(logo);
        holder.rowColor.setBackgroundColor(color);
    }

    
    
    
    @Override
    public int getItemCount(){
        return modules.size();
    }
}