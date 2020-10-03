package com.damasahhre.hooftrim.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.damasahhre.hooftrim.R;
import com.damasahhre.hooftrim.database.models.NextVisit;
import com.damasahhre.hooftrim.models.MyDate;

import java.util.Date;
import java.util.List;

public class RecyclerViewAdapterNextVisitFarmProfile extends RecyclerView.Adapter<RecyclerViewAdapterNextVisitFarmProfile.Holder> {

    private List<NextVisit> nextVisits;

    public RecyclerViewAdapterNextVisitFarmProfile(List<NextVisit> nextVisits) {
        this.nextVisits = nextVisits;
    }

    public void setNextVisits(List<NextVisit> nextVisits) {
        this.nextVisits = nextVisits;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //todo change view
        return new Holder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.next_visit_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        NextVisit visit = nextVisits.get(position);
        holder.cowName.setText(R.string.cow_title);
        holder.cowName.append("" + visit.cowNumber);
    }

    @Override
    public int getItemCount() {
        return Math.min(nextVisits.size(), 3);
    }

    static class Holder extends RecyclerView.ViewHolder {

        TextView cowName;
        TextView farmName;
        TextView date;

        public Holder(@NonNull View itemView) {
            super(itemView);
            farmName = itemView.findViewById(R.id.cow_count_text);
            cowName = itemView.findViewById(R.id.cattel_id);
            date = itemView.findViewById(R.id.date_string);
        }
    }

}
