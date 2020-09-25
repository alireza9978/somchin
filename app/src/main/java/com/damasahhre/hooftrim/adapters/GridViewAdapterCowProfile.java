package com.damasahhre.hooftrim.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.damasahhre.hooftrim.R;
import com.damasahhre.hooftrim.constants.Constants;
import com.damasahhre.hooftrim.database.models.Report;

import java.util.List;

public class GridViewAdapterCowProfile extends BaseAdapter {

    private List<Report> reports;
    private Context context;

    public GridViewAdapterCowProfile(Context context, List<Report> reports) {
        this.reports = reports;
        this.context = context;
    }

    public void setReports(List<Report> reports) {
        this.reports = reports;
    }

    @Override
    public int getCount() {
        return reports.size();
    }

    @Override
    public Object getItem(int i) {
        return reports.get(i);
    }

    @Override
    public long getItemId(int i) {
        return reports.get(i).id;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Holder holder;
        Report report = reports.get(i);
        if (view == null) {
            view = LayoutInflater.from(context)
                    .inflate(R.layout.cow_report_item, viewGroup, false);
            holder = new Holder();
            holder.view = view;
            holder.title = view.findViewById(R.id.report_name);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }
        holder.view.setOnClickListener((v) -> {
            //todo change to report summery activity
            Intent intent = new Intent(context, Report.class);
            intent.putExtra(Constants.FARM_ID, report.id);
            context.startActivity(intent);
        });
        holder.title.setText("" + report.id);
        return view;
    }

    static class Holder {
        View view;
        TextView title;
    }

}
