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
import com.damasahhre.hooftrim.activities.CowProfileActivity;
import com.damasahhre.hooftrim.constants.Constants;
import com.damasahhre.hooftrim.database.models.Cow;

import java.util.List;

public class RecyclerViewAdapterSearchCow extends RecyclerView.Adapter<RecyclerViewAdapterSearchCow.Holder> {

    private List<Cow> cows;
    private Context context;

    public RecyclerViewAdapterSearchCow(List<Cow> cows, Context context) {
        this.cows = cows;
        this.context = context;
    }

    public void setCows(List<Cow> cows) {
        this.cows = cows;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cow_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Cow cow = cows.get(position);
        holder.cowName.setText(cow.getNumber(context));
        holder.farmName.setText("farm");
        holder.date.setText("date");
        holder.itemView.setOnClickListener((v) -> {
            Intent intent = new Intent(context, CowProfileActivity.class);
            intent.putExtra(Constants.COW_ID, cow.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return cows.size();
    }

    static class Holder extends RecyclerView.ViewHolder {

        TextView cowName;
        TextView farmName;
        TextView date;
        ImageView arrow;

        public Holder(@NonNull View itemView) {
            super(itemView);
            farmName = itemView.findViewById(R.id.cow_count_text);
            cowName = itemView.findViewById(R.id.cow_name);
            date = itemView.findViewById(R.id.date_text);
            arrow = itemView.findViewById(R.id.arrow);

        }
    }

}
