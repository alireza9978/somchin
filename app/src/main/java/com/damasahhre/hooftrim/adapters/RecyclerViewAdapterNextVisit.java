package com.damasahhre.hooftrim.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.damasahhre.hooftrim.R;
import com.damasahhre.hooftrim.database.models.NextReport;
import com.damasahhre.hooftrim.models.MyDate;

import java.util.Date;
import java.util.List;

public class RecyclerViewAdapterNextVisit extends RecyclerView.Adapter<RecyclerViewAdapterNextVisit.Holder> {

    private List<NextReport> nextReports;

    public RecyclerViewAdapterNextVisit(List<NextReport> nextReports) {
        this.nextReports = nextReports;
    }

    public void setNextReports(List<NextReport> nextReports) {
        this.nextReports = nextReports;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.next_visit_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        NextReport report = nextReports.get(position);
        holder.cowName.setText(R.string.cow_title);
        holder.cowName.append("" + report.cowNumber);
        holder.farmName.setText(report.farmName);
        if (report.nextVisitDate.compareTo(new MyDate(new Date())) == 0) {
            holder.date.setText(R.string.today);
        } else
            holder.date.setText(report.nextVisitDate.toString());
    }

    @Override
    public int getItemCount() {
        return Math.min(nextReports.size(), 3);
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
