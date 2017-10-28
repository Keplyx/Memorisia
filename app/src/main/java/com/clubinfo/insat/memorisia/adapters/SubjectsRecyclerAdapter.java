package com.clubinfo.insat.memorisia.adapters;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
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

import java.util.ArrayList;
import java.util.List;


public class SubjectsRecyclerAdapter extends RecyclerView.Adapter<SubjectsRecyclerAdapter.ViewHolder> {
    
    private List<OptionModule> modules;
    private Context context;
    private List<Integer> agendas;
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

    public SubjectsRecyclerAdapter(Context context, List<OptionModule> modules, List<Integer> agendas, FragmentManager fragMan){
        this.context = context;
        this.modules = modules;
        this.agendas = agendas;
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
        holder.logo.setImageBitmap(Utils.getBitmapFromAsset(context, logo));
        holder.logo.setColorFilter(Color.parseColor(color));
    
        SaveManager saver = new SaveManager(context);
        List<Integer> subjects = new ArrayList<>();
        subjects.add(id);
        List<WorkModule> worksList = saver.getWorkModuleList(agendas, subjects, null);
        if (worksList.size() != 0) {
            holder.bar.setMax(worksList.size());
            holder.bar.setProgress(ModulesUtils.getWorkDoneNumber(worksList));
            if (ModulesUtils.getWorkDoneNumber(worksList) == worksList.size()) {
                holder.bar.getProgressDrawable().setColorFilter(ContextCompat.getColor(context, R.color.colorWorkDoneLight), PorterDuff.Mode.SRC_IN);
                holder.textHeader.setTextColor(ContextCompat.getColor(context, R.color.colorWorkDoneLight));
            }
            if (Build.VERSION.SDK_INT >= 24)
                holder.textFooter.setText(Html.fromHtml(context.getString(R.string.done) + ": " +
                    ModulesUtils.getWorkDoneNumber(worksList) + " / " + "<b>" + worksList.size() + "</b>", Html.FROM_HTML_MODE_LEGACY));
            else
                holder.textFooter.setText(Html.fromHtml(context.getString(R.string.done) + ": " +
                        ModulesUtils.getWorkDoneNumber(worksList) + " / " + "<b>" + worksList.size() + "</b>"));
        }
        else{
            holder.bar.getProgressDrawable().setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.SRC_IN);
            holder.textHeader.setTextColor(ContextCompat.getColor(context, R.color.dividerColorInverse));
            holder.textFooter.setText("");
        }
        
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new WorkViewFragment();
                Bundle b = new Bundle();
                b.putInt("id", id);
                fragment.setArguments(b);
                android.app.FragmentTransaction ft = fragMan.beginTransaction();
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
