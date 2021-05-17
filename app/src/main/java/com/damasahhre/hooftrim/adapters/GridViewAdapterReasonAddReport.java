package com.damasahhre.hooftrim.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.damasahhre.hooftrim.R;
import com.damasahhre.hooftrim.models.CheckBoxItem;

import java.util.List;

/**
 * کلاس مدیریت لیست ایتم ها در صفحه‌‌های افزودن گزارش
 */
public class GridViewAdapterReasonAddReport extends BaseAdapter {

    private List<CheckBoxItem> items;
    private Context context;

    public GridViewAdapterReasonAddReport(Context context, List<CheckBoxItem> farms) {
        this.items = farms;
        this.context = context;
    }

    public void setFarms(List<CheckBoxItem> farms) {
        this.items = farms;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Holder holder;
        CheckBoxItem item = items.get(i);
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.reason_list_item, viewGroup, false);
            holder = new Holder();
            holder.view = view;
            holder.item = view.findViewById(R.id.reason_check);
            holder.name = view.findViewById(R.id.reason_text);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }
        holder.name.setText(item.getName());
        if (item.isActive()) {
            holder.item.setChecked(item.isCheck());
            if (item.isCheck()) {
                holder.name.setTextColor(context.getResources().getColor(R.color.black));
            } else {
                holder.name.setTextColor(context.getResources().getColor(R.color.report_gray));
            }
            holder.view.setEnabled(true);
            holder.item.setEnabled(true);
            holder.name.setEnabled(true);
            holder.view.setOnClickListener(view1 -> {
                boolean b = !item.isCheck();
                item.setCheck(b);
                if (b) {
                    item.disableOther();
                } else {
                    item.enableOther();
                }
                notifyDataSetChanged();
            });
        } else {
            holder.name.setTextColor(context.getResources().getColor(R.color.athens_gray));
            holder.item.setChecked(false);
            holder.view.setEnabled(true);
            holder.view.setOnClickListener(view1 -> {
                Toast.makeText(context, context.getResources().getString(R.string.cant_select), Toast.LENGTH_SHORT).show();
            });
            holder.item.setEnabled(false);
            holder.name.setEnabled(false);
        }
        holder.item.setOnCheckedChangeListener((compoundButton, b) -> {
            item.setCheck(b);
            if (b) {
                item.disableOther();
            } else {
                item.enableOther();
            }
            notifyDataSetChanged();
        });
        return view;
    }

    static class Holder {
        View view;
        TextView name;
        CheckBox item;
    }

}
