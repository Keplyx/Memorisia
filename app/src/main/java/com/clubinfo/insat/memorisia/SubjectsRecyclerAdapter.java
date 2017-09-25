package com.clubinfo.insat.memorisia;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


public class SubjectsRecyclerAdapter extends RecyclerView.Adapter<SubjectsRecyclerAdapter.ViewHolder> {
    
    private List<OptionModule> modules;

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView textHeader;
        public TextView textFooter;
        public ImageView logo;
        public View layout;

        public ViewHolder(View v){
            super(v);
            layout = v;
            textHeader = v.findViewById(R.id.subjectTitle);
            textFooter = v.findViewById(R.id.SubjectDescription);
            logo = v.findViewById(R.id.SubjectIcon);
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

    public SubjectsRecyclerAdapter(List<OptionModule> modules){
        this.modules = modules;
    }

    @Override
    public SubjectsRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.subjects_row_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder (ViewHolder holder, final int pos){
        final String name = modules.get(pos).getName();
        final int logo = modules.get(pos).getLogo();
        final int color = modules.get(pos).getOptionColor();
        holder.textHeader.setText(name);
        holder.textFooter.setText("Footer: " + name);
        holder.logo.setImageResource(logo);
        holder.logo.setColorFilter(color);
    }

    @Override
    public int getItemCount(){
        return modules.size();
    }
}
