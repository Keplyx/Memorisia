package com.clubinfo.insat.memorisia.adapters;


import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.clubinfo.insat.memorisia.activities.EditOptionsActivity;
import com.clubinfo.insat.memorisia.activities.EditWorkActivity;
import com.clubinfo.insat.memorisia.activities.MainActivity;
import com.clubinfo.insat.memorisia.fragments.WorkViewFragment;
import com.clubinfo.insat.memorisia.modules.OptionModule;
import com.clubinfo.insat.memorisia.R;
import com.clubinfo.insat.memorisia.SaveManager;
import com.clubinfo.insat.memorisia.modules.WorkModule;
import com.clubinfo.insat.memorisia.utils.Utils;

import java.util.List;

public class WorksRecyclerAdapter extends RecyclerView.Adapter<WorksRecyclerAdapter.ViewHolder>{
    
    private List<WorkModule> modules;
    private List<OptionModule> subjectsList;
    private Context context;
    private FragmentManager fragMan;
    
    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView workTypeHeader;
        public TextView description;
        public ImageView logo;
        public RatingBar priorityBar;
        public CheckBox doneCheckBox;
        public View layout;
        
        public ViewHolder(View v){
            super(v);
            layout = v;
            workTypeHeader = v.findViewById(R.id.workTitle);
            description = v.findViewById(R.id.workDescription);
            logo = v.findViewById(R.id.workLogo);
            priorityBar = v.findViewById(R.id.priorityRatingBar);
            doneCheckBox = v.findViewById(R.id.stateCheckBox);
        }
    }
    
    public void add(int pos, WorkModule item){
        modules.add(pos, item);
        notifyItemInserted(pos);
    }
    
    public void remove(int pos){
        modules.remove(pos);
        notifyItemRemoved(pos);
    }
    
    public WorksRecyclerAdapter(Context context, List<WorkModule> modules){
        this.context = context;
        this.modules = modules;
        SaveManager saver = new SaveManager(context);
        subjectsList = saver.getOptionModuleList(SaveManager.WORKTYPE);
    }
    
    @Override
    public WorksRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.workslist_row_item, parent, false);
        WorksRecyclerAdapter.ViewHolder vh = new WorksRecyclerAdapter.ViewHolder(v);
        return vh;
    }
    
    @Override
    public void onBindViewHolder (WorksRecyclerAdapter.ViewHolder holder, final int pos){
        final String description = modules.get(pos).getText();
        final int agendaId = modules.get(pos).getAgendaId();
        final int subjectId = modules.get(pos).getSubjectId();
        final int workTypeId = modules.get(pos).getWorkTypeId();
        final int priority = modules.get(pos).getPriority();
        final int id = modules.get(pos).getId();
        final boolean state = modules.get(pos).isState();
        final boolean notifications = modules.get(pos).isNotificationsEnabled();
        String tempWorkName = "Work Type not found";
        String tempLogo = null;
        String tempColor = "#ffffff";
        for (int i = 0; i < subjectsList.size(); i++){
            if (subjectsList.get(i).getId() == workTypeId){
                tempWorkName = subjectsList.get(i).getText();
                tempLogo = subjectsList.get(i).getLogo();
                tempColor = subjectsList.get(i).getColor();
            }
        }
        final String workName = tempWorkName;
        final String logo = tempLogo;
        final String color = tempColor;
        
        holder.workTypeHeader.setText(workName);
        holder.description.setText(description);
        holder.priorityBar.setRating((float) priority);
        holder.doneCheckBox.setChecked(state);
        holder.doneCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
               @Override
               public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    SaveManager saver = new SaveManager(context);
                    saver.saveModule(new WorkModule(id, agendaId, subjectId, workTypeId, priority, description, notifications, isChecked));
               }
           }
        );
        holder.logo.setImageBitmap(Utils.getBitmapFromAsset(context, logo));
        holder.logo.setColorFilter(Color.parseColor(color));
    
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EditWorkActivity.class);
                Bundle b = new Bundle();
                b.putString("text", description);
                b.putInt("workTypeId", workTypeId);
                b.putInt("subjectId", subjectId);
                b.putInt("agendaId", agendaId);
                b.putInt("priority", priority);
                b.putInt("id", id);
                b.putBoolean("state", state);
                b.putBoolean("notifications", notifications);
                intent.putExtras(b);
                context.startActivity(intent);
            }
        });
    }
    
    @Override
    public int getItemCount(){
        return modules.size();
    }
}
