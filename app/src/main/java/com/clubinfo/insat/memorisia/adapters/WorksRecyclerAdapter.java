package com.clubinfo.insat.memorisia.adapters;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.clubinfo.insat.memorisia.R;
import com.clubinfo.insat.memorisia.SaveManager;
import com.clubinfo.insat.memorisia.activities.EditWorkActivity;
import com.clubinfo.insat.memorisia.activities.SettingsActivity;
import com.clubinfo.insat.memorisia.modules.OptionModule;
import com.clubinfo.insat.memorisia.modules.WorkModule;
import com.clubinfo.insat.memorisia.utils.ModulesUtils;
import com.clubinfo.insat.memorisia.utils.Utils;

import java.util.Calendar;
import java.util.List;

public class WorksRecyclerAdapter extends RecyclerView.Adapter<WorksRecyclerAdapter.ViewHolder> {
    
    private List<WorkModule> modules;
    private List<OptionModule> workTypesList;
    private Context context;
    
    private static final String outdatedColor = "#d20707";
    private static final String tomorrowColor = "#e75c00";
    private static final String thisWeekColor = "#e7df00";
    private static final String normalColor = "#919191";
    
    public WorksRecyclerAdapter(Context context, List<WorkModule> modules) {
        this.context = context;
        this.modules = modules;
        SaveManager saver = new SaveManager(context);
        workTypesList = saver.getOptionModuleList(SaveManager.WORK_TYPE);
    }
    
    public void add(int pos, WorkModule item) {
        modules.add(pos, item);
        notifyItemInserted(pos);
    }
    
    public void remove(int pos) {
        modules.remove(pos);
        notifyItemRemoved(pos);
    }
    
    @Override
    public WorksRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.workslist_row_item, parent, false);
        WorksRecyclerAdapter.ViewHolder vh = new WorksRecyclerAdapter.ViewHolder(v);
        return vh;
    }
    
    @Override
    public void onBindViewHolder(WorksRecyclerAdapter.ViewHolder holder, int pos) {
        final WorkModule work = modules.get(pos);
        OptionModule workType = ModulesUtils.getModuleOfId(workTypesList, work.getWorkTypeId());
        
        holder.workTypeHeader.setText(workType.getText());
        holder.logo.setImageBitmap(Utils.getBitmapFromAsset(context, workType.getLogo()));
        holder.logo.setColorFilter(Color.parseColor(workType.getColor()));
        
        Calendar c = Calendar.getInstance();;
        int[] today = new int[] {c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.MONTH) + 1, c.get(Calendar.YEAR)};
        int delta = Utils.getDateDelta(today, work.getDate());
        if (delta <= 0)
            setupDateDisplay(holder.date, outdatedColor, work.getDate());
        else if (delta <= 1)
            setupDateDisplay(holder.date, tomorrowColor, work.getDate());
        else if (delta <= 7)
            setupDateDisplay(holder.date, thisWeekColor, work.getDate());
        else
            setupDateDisplay(holder.date, normalColor, work.getDate());
    
        if (delta == 0){
            int current = c.get(Calendar.MINUTE) + c.get(Calendar.HOUR_OF_DAY) * 60;
            int due = work.getTime()[1] + work.getTime()[0] * 60;
            if (current < due)
                setupTimeDisplay(holder.time, tomorrowColor, work.getTime());
            else
                setupTimeDisplay(holder.time, outdatedColor, work.getTime());
        } else if (delta < 0){
            setupTimeDisplay(holder.time, outdatedColor, work.getTime());
        } else {
            setupTimeDisplay(holder.time, normalColor, work.getTime());
        }
        
        
        holder.scroll.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        holder.description.setText(work.getText());
        holder.priorityBar.setRating((float) work.getPriority());
        holder.doneCheckBox.setChecked(work.isState());
        setWorkChecked(holder.layout, work.isState());
        final View holderLayout = holder.layout;
        holder.doneCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                           @Override
                                                           public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                               SaveManager saver = new SaveManager(context);
                                                               work.setState(isChecked);
                                                               setWorkChecked(holderLayout, isChecked);
                                                               saver.saveModule(work);
                                                           }
                                                       }
        );
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editSelectedWork(work);
            }
        });
    }
    
    /**
     * Sets the Drawable for the date textView based on the due date
     *
     * @param text textView used to display the date
     * @param color Drawable color
     * @param date Work due date
     */
    private void setupDateDisplay(TextView text, String color, int[] date) {
        if (date[0] != -1) {
            text.setText(Utils.getDateText(date));
            text.setTextColor(Color.parseColor(color));
            Drawable d = ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_date_range_black_24dp, null);
            d.mutate().setColorFilter(Color.parseColor(color), PorterDuff.Mode.SRC_IN);
            text.setCompoundDrawablesWithIntrinsicBounds(d, null, null, null);
        } else {
            text.setText("");
            text.setCompoundDrawables(null, null, null, null);
        }
    }
    
    
    /**
     * Sets the Drawable for the time textView based on the due date
     *
     * @param text textView used to display the time
     * @param color Drawable color
     * @param time Work due date
     */
    private void setupTimeDisplay(TextView text, String color, int[] time) {
        if (time[0] != -1) {
            text.setText(Utils.getTimeText(time));
            text.setTextColor(Color.parseColor(color));
            Drawable d = ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_access_time_black_24dp, null);
            d.mutate().setColorFilter(Color.parseColor(color), PorterDuff.Mode.SRC_IN);
            text.setCompoundDrawablesWithIntrinsicBounds(d, null, null, null);
        } else {
            text.setText("");
            text.setCompoundDrawables(null, null, null, null);
        }
    }
    
    /**
     * Starts a new EditWorkActivity to let the user edit the selected work
     *
     * @param work Selected WorkModule
     */
    private void editSelectedWork(WorkModule work) {
        Intent intent = new Intent(context, EditWorkActivity.class);
        Bundle b = ModulesUtils.createBundleFromModule(work);
        intent.putExtras(b);
        context.startActivity(intent);
    }
    
    /**
     * Makes the selected view turn green if it is selected (based on current theme), normal if not
     *
     * @param v         View to change color
     * @param isChecked Whether the view is selected or not
     */
    private void setWorkChecked(View v, boolean isChecked) {
        if (!isChecked) {
            v.setBackgroundColor(0);
            return;
        }
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        Boolean nightMode = sharedPref.getBoolean(SettingsActivity.KEY_NIGHT_MODE, false);
        if (nightMode)
            v.setBackgroundColor(ContextCompat.getColor(context, R.color.colorWorkDoneDark));
        else
            v.setBackgroundColor(ContextCompat.getColor(context, R.color.colorWorkDoneLight));
    }
    
    @Override
    public int getItemCount() {
        return modules.size();
    }
    
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView workTypeHeader;
        public TextView description;
        public TextView date;
        public TextView time;
        public ImageView logo;
        public RatingBar priorityBar;
        public CheckBox doneCheckBox;
        public View layout;
        
        public ScrollView scroll;
        
        public ViewHolder(View v) {
            super(v);
            layout = v;
            workTypeHeader = v.findViewById(R.id.workTitle);
            description = v.findViewById(R.id.workDescription);
            date = v.findViewById(R.id.dateTextView);
            time = v.findViewById(R.id.timeTextView);
            logo = v.findViewById(R.id.workLogo);
            priorityBar = v.findViewById(R.id.priorityRatingBar);
            doneCheckBox = v.findViewById(R.id.stateCheckBox);
    
            scroll = v.findViewById(R.id.descriptionScroll);
        }
    }
    
}
