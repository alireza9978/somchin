package com.damasahhre.hooftrim.activities.tabs.report_activites;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.damasahhre.hooftrim.R;
import com.damasahhre.hooftrim.activities.MainActivity;
import com.damasahhre.hooftrim.adapters.RecyclerViewAdapterHomeNextVisit;
import com.damasahhre.hooftrim.adapters.RecyclerViewAdapterNextVisit;
import com.damasahhre.hooftrim.constants.Constants;
import com.damasahhre.hooftrim.constants.FormatHelper;
import com.damasahhre.hooftrim.constants.Utilities;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class ReportVisitFragment extends Fragment {

    private TextView month;
    private TextView year;
    private Context context;
    private MaterialCalendarView calendar;
    private ImageView right;
    private ImageView left;

    private RecyclerViewAdapterNextVisit mAdapter;
    private TextView dateText;
    private TextView noVisit;
    private RecyclerView nextVisitList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report_visit, container, false);
        context = requireContext();
        right = view.findViewById(R.id.right_arrow);
        left = view.findViewById(R.id.left_arrow);
        month = view.findViewById(R.id.month_title);
        year = view.findViewById(R.id.year_title);
        calendar = view.findViewById(R.id.calendarView);
        dateText = view.findViewById(R.id.date_text);
        noVisit = view.findViewById(R.id.not_fount_text);
        nextVisitList = view.findViewById(R.id.next_visits_list);

        String language = Constants.getDefualtlanguage(requireContext());
        if (language.isEmpty()) {
            language = "en";
        }
        String finalLanguage = language;
        if (finalLanguage.equals("en")) {
            setEnglish();
        } else {
            setPersian();
        }
        nextVisitList.setVisibility(View.INVISIBLE);
        noVisit.setVisibility(View.VISIBLE);

        nextVisitList.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext());
        nextVisitList.setLayoutManager(layoutManager);
        mAdapter = new RecyclerViewAdapterNextVisit(new ArrayList<>());
        nextVisitList.setAdapter(mAdapter);
        //todo read data base
        return view;

//        calendar.setDayFormatter(day -> {
//            String temp;
//            if (finalLanguage.equals("en")) {
//                temp = "" + day.getDay();
//            } else {
//                temp = FormatHelper.toPersianNumber("" + day.getDay());
//            }
//            return temp;
//        });
//        calendar.setWeekDayFormatter(dayOfWeek -> {
//            String temp = " ";
//            if (finalLanguage.equals("en")) {
//                switch (dayOfWeek) {
//                    case MONDAY:
//                        temp = "mon";
//                        break;
//                    case TUESDAY:
//                        temp = "tue";
//                        break;
//                    case WEDNESDAY:
//                        temp = "wed";
//                        break;
//                    case THURSDAY:
//                        temp = "thu";
//                        break;
//                    case FRIDAY:
//                        temp = "fri";
//                        break;
//                    case SATURDAY:
//                        temp = "sat";
//                        break;
//                    case SUNDAY:
//                        temp = "sun";
//                        break;
//                }
//            } else {
//                switch (dayOfWeek) {
//                    case MONDAY:
//                        temp = "ج";
//                        break;
//                    case TUESDAY:
//                        temp = "ش";
//                        break;
//                    case WEDNESDAY:
//                        temp = "ی";
//                        break;
//                    case THURSDAY:
//                        temp = "د";
//                        break;
//                    case FRIDAY:
//                        temp = "س";
//                        break;
//                    case SATURDAY:
//                        temp = "چ";
//                        break;
//                    case SUNDAY:
//                        temp = "پ";
//                        break;
//                }
//            }
//            Typeface font = ResourcesCompat.getFont(requireContext(), R.font.anjoman_medium);
//            SpannableString mNewTitle = new SpannableString(temp);
//
//            mNewTitle.setSpan(new AbsoluteSizeSpan(20, true), 0, mNewTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//            mNewTitle.setSpan(new ForegroundColorSpan(requireContext().getResources().getColor(R.color.calender_blue)), 0, mNewTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//            mNewTitle.setSpan(new MainActivity.CustomTypefaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//            return mNewTitle;
//        });
//        calendar.setOnDateChangedListener((widget, date, selected) -> {
//            if (selected) {
//                String temp = date.toString();
//                Toast.makeText(requireContext(), temp, Toast.LENGTH_LONG).show();
//            }
//        });
//        Date date = new Date();
//        setTopCalendarBar(finalLanguage, date.getYear() + 1900, date.getMonth() + 1, date.getDay());
//        calendar.setSelectedDate(getToday(finalLanguage));
//        calendar.setCurrentDate(getToday(finalLanguage));
    }

    private void setEnglish() {
        calendar.setTopbarVisible(false);
        calendar.setOnMonthChangedListener((widget, date) -> {
            Typeface font = ResourcesCompat.getFont(context, R.font.anjoman_bold);
            SpannableString mNewTitle = new SpannableString(Utilities.monthToString(date, context));

            mNewTitle.setSpan(new AbsoluteSizeSpan(20, true), 0, mNewTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            mNewTitle.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.black)), 0, mNewTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            mNewTitle.setSpan(new MainActivity.CustomTypefaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            month.setText(mNewTitle);

            font = ResourcesCompat.getFont(context, R.font.anjoman_regular);
            mNewTitle = new SpannableString(Utilities.yearToString(date, context));

            mNewTitle.setSpan(new AbsoluteSizeSpan(16, true), 0, mNewTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            mNewTitle.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.hit_gray)), 0, mNewTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            mNewTitle.setSpan(new MainActivity.CustomTypefaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            year.setText(mNewTitle);
        });
        right.setOnClickListener(v -> calendar.goToNext());
        left.setOnClickListener(v -> calendar.goToPrevious());
        calendar.setOnDateChangedListener((widget, date, selected) -> {
            if (selected){

            }else{

            }
        });


        Date date = new Date();
        CalendarDay today = CalendarDay.today();
        calendar.setSelectedDate(today);
        calendar.setCurrentDate(today);
        setTopCalendarBarEn(date);
    }

    private void setTopCalendarBarEn(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.set(date.getYear() + 1900, date.getMonth(), date.getDay());
        SimpleDateFormat month_date = new SimpleDateFormat("MMMM", Locale.ENGLISH);
        SimpleDateFormat year_date = new SimpleDateFormat("yyyy", Locale.ENGLISH);
        String output_year = year_date.format(cal.getTime());
        String output_month = month_date.format(cal.getTime());
        Typeface font = ResourcesCompat.getFont(context, R.font.anjoman_bold);
        SpannableString mNewTitle = new SpannableString(output_year);

        mNewTitle.setSpan(new AbsoluteSizeSpan(20, true), 0, mNewTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mNewTitle.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.black)), 0, mNewTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mNewTitle.setSpan(new MainActivity.CustomTypefaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        month.setText(mNewTitle);

        font = ResourcesCompat.getFont(context, R.font.anjoman_regular);
        mNewTitle = new SpannableString(output_month);

        mNewTitle.setSpan(new AbsoluteSizeSpan(16, true), 0, mNewTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mNewTitle.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.hit_gray)), 0, mNewTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mNewTitle.setSpan(new MainActivity.CustomTypefaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        year.setText(mNewTitle);
    }

    private void setPersian() {
        setEnglish();
    }

    private CalendarDay getToday(String finalLanguage) {
        Date date = new Date();
        if (finalLanguage.equals("en")) {
            return CalendarDay.from(date.getYear() + 1900, date.getMonth() + 1, date.getDay());
        } else {
            Utilities.SolarCalendar calendar = new Utilities.SolarCalendar();
//            int tempDay = date.getDay() + 10;
            int temp = calendar.month + 3;
//            if (tempDay > 31){
//                tempDay -= 31;
//                temp += 1;
//            }
            return CalendarDay.from(1900 + date.getYear(), temp, calendar.date);
        }
    }

    private void setTopCalendarBar(String finalLanguage, int year_number, int month_number, int day_number) {
        String output_year;
        String output_month;
        if (finalLanguage.equals("en")) {
            Calendar cal = Calendar.getInstance();
            cal.set(year_number, month_number - 1, day_number);
            SimpleDateFormat month_date = new SimpleDateFormat("MMMM", Locale.ENGLISH);
            SimpleDateFormat year_date = new SimpleDateFormat("yyyy", Locale.ENGLISH);
            output_month = month_date.format(cal.getTime());
            output_year = year_date.format(cal.getTime());
        } else {
            Utilities.SolarCalendar calendar = new Utilities.SolarCalendar(new Date(year_number - 1900, month_number - 1, day_number));
            int temp = month_number - 3;
            int tempYear = calendar.year;
            if (temp < 1) {
                temp += 12;
            }
            String strMonth = " ";
            switch (temp) {
                case 1:
                    strMonth = "فروردين";
                    break;
                case 2:
                    strMonth = "ارديبهشت";
                    break;
                case 3:
                    strMonth = "خرداد";
                    break;
                case 4:
                    strMonth = "تير";
                    break;
                case 5:
                    strMonth = "مرداد";
                    break;
                case 6:
                    strMonth = "شهريور";
                    break;
                case 7:
                    strMonth = "مهر";
                    break;
                case 8:
                    strMonth = "آبان";
                    break;
                case 9:
                    strMonth = "آذر";
                    break;
                case 10:
                    strMonth = "دي";
                    break;
                case 11:
                    strMonth = "بهمن";
                    break;
                case 12:
                    strMonth = "اسفند";
                    break;
            }
            output_month = strMonth;
            output_year = FormatHelper.toPersianNumber("" + tempYear);
        }
        Typeface font = ResourcesCompat.getFont(requireContext(), R.font.anjoman_bold);
        SpannableString mNewTitle = new SpannableString(output_month);

        mNewTitle.setSpan(new AbsoluteSizeSpan(20, true), 0, mNewTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mNewTitle.setSpan(new ForegroundColorSpan(requireContext().getResources().getColor(R.color.black)), 0, mNewTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mNewTitle.setSpan(new MainActivity.CustomTypefaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        month.setText(mNewTitle);

        font = ResourcesCompat.getFont(requireContext(), R.font.anjoman_regular);
        mNewTitle = new SpannableString(output_year);

        mNewTitle.setSpan(new AbsoluteSizeSpan(16, true), 0, mNewTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mNewTitle.setSpan(new ForegroundColorSpan(requireContext().getResources().getColor(R.color.hit_gray)), 0, mNewTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mNewTitle.setSpan(new MainActivity.CustomTypefaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        year.setText(mNewTitle);
    }

}