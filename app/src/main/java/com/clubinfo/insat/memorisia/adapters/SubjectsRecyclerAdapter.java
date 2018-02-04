package com.clubinfo.insat.memorisia.adapters;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
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

import com.clubinfo.insat.memorisia.R;
import com.clubinfo.insat.memorisia.SaveManager;
import com.clubinfo.insat.memorisia.activities.MainActivity;
import com.clubinfo.insat.memorisia.fragments.WorkViewFragment;
import com.clubinfo.insat.memorisia.modules.OptionModule;
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
    
    public SubjectsRecyclerAdapter(Context context, List<OptionModule> modules, List<Integer> agendas, FragmentManager fragMan) {
        this.context = context;
        this.modules = modules;
        this.agendas = agendas;
        this.fragMan = fragMan;
    }
    
    public void add(int pos, OptionModule item) {
        modules.add(pos, item);
        notifyItemInserted(pos);
    }
    
    public void remove(int pos) {
        modules.remove(pos);
        notifyItemRemoved(pos);
    }
    
    @Override
    public SubjectsRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.subjects_row_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }
    
    @Override
    public void onBindViewHolder(ViewHolder holder, int pos) {
        final OptionModule subject = modules.get(pos);
        holder.textHeader.setText(subject.getText());
        holder.logo.setImageBitmap(Utils.getBitmapFromAsset(context, subject.getLogo()));
        holder.logo.setColorFilter(Color.parseColor(subject.getColor()));
        
        List<Integer> subjects = new ArrayList<>();
        subjects.add(subject.getId());
        List<WorkModule> worksList = new SaveManager(context).getWorkModuleList(agendas, subjects, null);
        if (worksList.size() != 0) {
            holder.bar.setMax(worksList.size());
            holder.bar.setProgress(ModulesUtils.getWorkDoneNumber(worksList));
            if (ModulesUtils.getWorkDoneNumber(worksList) == worksList.size())
                setViewComplete(holder);
            setPercentDoneText(holder.textFooter, worksList);
        } else
            setViewInactive(holder);
        
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startWorkViewFragment(subject);
            }
        });
    }
    
    /**
     * Displays in the given TextView the percent of work done
     *
     * @param text TextView used to display the progress
     */
    private void setPercentDoneText(TextView text, List<WorkModule> worksList) {
        if (Build.VERSION.SDK_INT >= 24)
            text.setText(Html.fromHtml(context.getString(R.string.done) + ": " +
                    ModulesUtils.getWorkDoneNumber(worksList) + " / " + "<b>" + worksList.size() + "</b>", Html.FROM_HTML_MODE_LEGACY));
        else
            text.setText(Html.fromHtml(context.getString(R.string.done) + ": " +
                    ModulesUtils.getWorkDoneNumber(worksList) + " / " + "<b>" + worksList.size() + "</b>"));
    }
    
    /**
     * Sets the current view as complete: Turns the text and progress bar to green
     *
     * @param holder ViewHolder containing the views
     */
    private void setViewComplete(ViewHolder holder) {
        holder.bar.getProgressDrawable().setColorFilter(ContextCompat.getColor(context, R.color.colorWorkDoneLight), PorterDuff.Mode.SRC_IN);
        holder.textHeader.setTextColor(ContextCompat.getColor(context, R.color.colorWorkDoneLight));
    }
    
    /**
     * Sets the current view as inactive: Hides the progress bar and changes the text color to gray
     *
     * @param holder ViewHolder containing the views
     */
    private void setViewInactive(ViewHolder holder) {
        holder.bar.getProgressDrawable().setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.SRC_IN);
        holder.textHeader.setTextColor(ContextCompat.getColor(context, R.color.dividerColorInverse));
        holder.textFooter.setText("");
    }
    
    /**
     * Starts a WorkViewFragment for the selected subject
     *
     * @param module Selected subject as an {@link com.clubinfo.insat.memorisia.modules.OptionModule OptionModule}
     */
    private void startWorkViewFragment(OptionModule module) {
        Fragment fragment = new WorkViewFragment();
        Bundle b = ModulesUtils.createBundleFromModule(module);
        fragment.setArguments(b);
        android.app.FragmentTransaction ft = fragMan.beginTransaction();
        ft.setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left, R.animator.slide_in_left, R.animator.slide_out_right);
        ft.replace(R.id.content_frame, fragment, MainActivity.Frags.FRAG_WORKS.name());
        ft.addToBackStack(null);
        ft.commit();
    }
    
    @Override
    public int getItemCount() {
        return modules.size();
    }
    
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textHeader;
        public TextView textFooter;
        public ImageView logo;
        public ProgressBar bar;
        public View layout;
        
        public ViewHolder(View v) {
            super(v);
            layout = v;
            textHeader = v.findViewById(R.id.subjectTitle);
            textFooter = v.findViewById(R.id.SubjectDescription);
            logo = v.findViewById(R.id.subjectIcon);
            bar = v.findViewById(R.id.subjectProgress);
        }
    }
}
