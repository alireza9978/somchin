package com.damasahhre.hooftrim.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.damasahhre.hooftrim.R;
import com.damasahhre.hooftrim.activities.CowProfileActivity;
import com.damasahhre.hooftrim.constants.Constants;
import com.damasahhre.hooftrim.database.models.NextReport;

import java.util.List;

/**
 * کلاس مدیریت لیست بازدید‌های بعدی در صفحه‌ی خانه
 */
public class RecyclerViewAdapterHomeNextVisit extends RecyclerView.Adapter<RecyclerViewAdapterHomeNextVisit.Holder> {

    private List<NextReport> nextReports;
    private Context context;

    public RecyclerViewAdapterHomeNextVisit(List<NextReport> nextReports, Context context) {
        this.nextReports = nextReports;
        this.context = context;
    }

    public void setNextReports(List<NextReport> nextReports) {
        this.nextReports = nextReports;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.next_visit_item_home, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        NextReport report = nextReports.get(position);
        holder.cowName.setText(R.string.cow_title);
        holder.cowName.append(" " + report.cowNumber);
        holder.farmName.setText(report.farmName);
        if (report.nextVisitDate.isToday()) {
            holder.date.setText(R.string.today);
            holder.date.setTextColor(context.getResources().getColor(R.color.red));
        } else {
            holder.date.setText(report.nextVisitDate.toString(context));
            holder.date.setTextColor(context.getResources().getColor(R.color.black));
        }
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, CowProfileActivity.class);
            intent.putExtra(Constants.COW_ID, report.cowId);
            context.startActivity(intent);
        });
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
            farmName = itemView.findViewById(R.id.farm_title_text);
            cowName = itemView.findViewById(R.id.cattel_id);
            date = itemView.findViewById(R.id.date_string);
        }
    }

}
