/*
 * Copyright (c) 2018.
 * This file is part of Memorisia.
 *
 * Memorisia is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Memorisia is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Memorisia.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.clubinfo.insat.memorisia.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.clubinfo.insat.memorisia.R;
import com.clubinfo.insat.memorisia.activities.EditOptionsActivity;
import com.clubinfo.insat.memorisia.modules.OptionModule;
import com.clubinfo.insat.memorisia.utils.ModulesUtils;
import com.clubinfo.insat.memorisia.utils.Utils;

import java.util.List;


public class OptionsRecyclerAdapter extends RecyclerView.Adapter<OptionsRecyclerAdapter.ViewHolder> {
    
    private List<OptionModule> modules;
    private Context context;
    
    public OptionsRecyclerAdapter(List<OptionModule> myDataset, Context context) {
        modules = myDataset;
        this.context = context;
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
    public OptionsRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.optionslist_row_item, parent, false);
        return new ViewHolder(v);
    }
    
    @Override
    public void onBindViewHolder(final ViewHolder holder, int pos) {
        final String name = modules.get(pos).getText();
        final String logo = modules.get(pos).getLogo();
        final String color = modules.get(pos).getColor();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EditOptionsActivity.class);
                Bundle b = ModulesUtils.createBundleFromModule(modules.get(holder.getAdapterPosition()));
                intent.putExtras(b);
                context.startActivity(intent);
            }
        });
        holder.text.setText(name);
        holder.logo.setImageBitmap(Utils.getBitmapFromAsset(context, logo));
        holder.logo.setColorFilter(Color.parseColor(color));
    }
    
    @Override
    public int getItemCount() {
        return modules.size();
    }
    
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView text;
        public ImageView logo;
        public View layout;
        
        public ViewHolder(View v) {
            super(v);
            layout = v;
            text = v.findViewById(R.id.workTitle);
            logo = v.findViewById(R.id.workLogo);
        }
    }
}
