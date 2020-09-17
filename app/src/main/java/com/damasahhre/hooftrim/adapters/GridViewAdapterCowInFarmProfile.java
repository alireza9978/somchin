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
import com.damasahhre.hooftrim.activities.CattelProfileActivity;
import com.damasahhre.hooftrim.constants.Constants;
import com.damasahhre.hooftrim.database.models.Cow;

import java.util.List;

public class GridViewAdapterCowInFarmProfile extends BaseAdapter {

    private List<Cow> cows;
    private Context context;

    public GridViewAdapterCowInFarmProfile(Context context, List<Cow> cows) {
        this.cows = cows;
        this.context = context;
    }

    @Override
    public int getCount() {
        return cows.size();
    }

    @Override
    public Object getItem(int i) {
        return cows.get(i);
    }

    @Override
    public long getItemId(int i) {
        return cows.get(i).id;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Holder holder;
        Cow cow = cows.get(i);
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
            Intent intent = new Intent(context, CattelProfileActivity.class);
            intent.putExtra(Constants.COW_ID, cow.id);
            context.startActivity(intent);
        });
        holder.icon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_calendar));
        holder.farmTitle.setText("" + cow.number);
        holder.cowCount.setText("  ");
        return view;
    }

    static class Holder {
        View view;
        TextView cowCount;
        TextView farmTitle;
        ImageView icon;
    }

}
