package com.damasahhre.hooftrim.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.damasahhre.hooftrim.R;
import com.damasahhre.hooftrim.activities.FarmProfileActivity;
import com.damasahhre.hooftrim.constants.Constants;
import com.damasahhre.hooftrim.database.models.Farm;

import java.util.List;

public class RecyclerViewAdapterSearchFarm extends RecyclerView.Adapter<RecyclerViewAdapterSearchFarm.Holder> {

    private List<Farm> farms;
    private Context context;

    public RecyclerViewAdapterSearchFarm(List<Farm> farms, Context context) {
        this.farms = farms;
        this.context = context;
    }

    public void setCows(List<Farm> cows) {
        this.farms = farms;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.livestrock_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Farm farm = farms.get(position);
        holder.cowCount.setText("1");
        holder.farmName.setText("farm");
        holder.date.setText("date");
        holder.itemView.setOnClickListener((v) -> {
            Intent intent = new Intent(context, FarmProfileActivity.class);
            intent.putExtra(Constants.FARM_ID, farm.id);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return farms.size();
    }

    static class Holder extends RecyclerView.ViewHolder {

        TextView cowCount;
        TextView farmName;
        TextView date;
        ImageView arrow;

        public Holder(@NonNull View itemView) {
            super(itemView);
            farmName = itemView.findViewById(R.id.cow_name);
            cowCount = itemView.findViewById(R.id.cow_count_text);
            date = itemView.findViewById(R.id.date_text);
            arrow = itemView.findViewById(R.id.arrow);

        }
    }

}
