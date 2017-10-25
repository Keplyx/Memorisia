package com.clubinfo.insat.memorisia.adapters;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.clubinfo.insat.memorisia.SaveManager;
import com.clubinfo.insat.memorisia.activities.MainActivity;
import com.clubinfo.insat.memorisia.modules.OptionModule;
import com.clubinfo.insat.memorisia.R;
import com.clubinfo.insat.memorisia.fragments.WorkViewFragment;
import com.clubinfo.insat.memorisia.modules.WorkModule;
import com.clubinfo.insat.memorisia.utils.ModulesUtils;
import com.clubinfo.insat.memorisia.utils.Utils;

import java.util.List;


public class SubjectsRecyclerAdapter extends RecyclerView.Adapter<SubjectsRecyclerAdapter.ViewHolder> {
    
    private List<OptionModule> modules;
    private Context context;
    private FragmentManager fragMan;
    
    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView textHeader;
        public TextView textFooter;
        public ImageView logo;
        public ProgressBar bar;
        public View layout;

        public ViewHolder(View v){
            super(v);
            layout = v;
            textHeader = v.findViewById(R.id.subjectTitle);
            textFooter = v.findViewById(R.id.SubjectDescription);
            logo = v.findViewById(R.id.subjectIcon);
            bar = v.findViewById(R.id.subjectProgress);
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

    public SubjectsRecyclerAdapter(Context context, List<OptionModule> modules, FragmentManager fragMan){
        this.context = context;
        this.modules = modules;
        this.fragMan = fragMan;
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
        final String name = modules.get(pos).getText();
        final String logo = modules.get(pos).getLogo();
        final String color = modules.get(pos).getColor();
        final int id = modules.get(pos).getId();
        holder.textHeader.setText(name);
        holder.textFooter.setText("Footer: " + name);
        holder.logo.setImageBitmap(Utils.getBitmapFromAsset(context, logo));
        holder.logo.setColorFilter(Color.parseColor(color));
    
        SaveManager saver = new SaveManager(context);
        List<WorkModule> worksList = saver.getWorkModuleList(-1, id, -1);
        if (worksList.size() != 0) {
            holder.bar.setMax(worksList.size());
            holder.bar.setProgress(ModulesUtils.getWorkDoneNumber(worksList));
            if (ModulesUtils.getWorkDoneNumber(worksList) == worksList.size())
                holder.bar.getProgressDrawable().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);
        }
        else{
            holder.bar.setMax(1);
            holder.bar.setProgress(1);
            holder.bar.getProgressDrawable().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);
        }
        
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new WorkViewFragment();
                Bundle b = new Bundle();
                b.putInt("id", id);
                fragment.setArguments(b);
                android.app.FragmentTransaction ft = fragMan.beginTransaction();
                ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
                ft.replace(R.id.content_frame, fragment, MainActivity.FRAG_WORKS);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
    }

    @Override
    public int getItemCount(){
        return modules.size();
    }
}
