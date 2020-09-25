package com.damasahhre.hooftrim.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.damasahhre.hooftrim.R;
import com.damasahhre.hooftrim.activities.FarmProfileActivity;
import com.damasahhre.hooftrim.constants.Constants;
import com.damasahhre.hooftrim.database.models.Farm;

import java.util.List;

public class GridViewAdapterHomeFarm extends BaseAdapter {

    private List<Farm> farms;
    private Context context;

    public GridViewAdapterHomeFarm(Context context, List<Farm> farms) {
        this.farms = farms;
        this.context = context;
    }

    public void setFarms(List<Farm> farms) {
        this.farms = farms;
    }

    @Override
    public int getCount() {
        return farms.size();
    }

    @Override
    public Object getItem(int i) {
        return farms.get(i);
    }

    @Override
    public long getItemId(int i) {
        return farms.get(i).id;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Holder holder;
        Farm farm = farms.get(i);
        if (view == null) {
            view = LayoutInflater.from(context)
                    .inflate(R.layout.livestock_grid_item, viewGroup, false);
            holder = new Holder();
            holder.view = view;
            holder.cowCount = view.findViewById(R.id.cow_count);
            holder.farmTitle = view.findViewById(R.id.farm_text);
            holder.icon = view.findViewById(R.id.cow_icon);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }
        holder.view.setOnClickListener((v) -> {
            Intent intent = new Intent(context, FarmProfileActivity.class);
            intent.putExtra(Constants.FARM_ID, farm.id);
            context.startActivity(intent);
        });
        holder.farmTitle.setText(farm.name);
        holder.cowCount.setText("" + farm.birthCount);
        holder.icon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_cow));
        return view;
    }

    static class Holder {
        View view;
        TextView cowCount;
        TextView farmTitle;
        ImageView icon;
    }

}
