package com.damasahhre.hooftrim.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.damasahhre.hooftrim.R;
import com.damasahhre.hooftrim.constants.Constants;
import com.damasahhre.hooftrim.constants.FormatHelper;
import com.damasahhre.hooftrim.constants.Utilities;
import com.damasahhre.hooftrim.models.DateContainer;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.damasahhre.hooftrim.constants.Constants.DateSelectionMode.RANG;
import static com.prolificinteractive.materialcalendarview.MaterialCalendarView.SELECTION_MODE_NONE;
import static com.prolificinteractive.materialcalendarview.MaterialCalendarView.SELECTION_MODE_RANGE;
import static com.prolificinteractive.materialcalendarview.MaterialCalendarView.SELECTION_MODE_SINGLE;

public class DateSelectionActivity extends AppCompatActivity {

    private Context context = this;
    private TextView month;
    private TextView year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_selection);

        ImageView right = findViewById(R.id.right_arrow);
        ImageView left = findViewById(R.id.left_arrow);
        month = findViewById(R.id.month_title);
        year = findViewById(R.id.year_title);
        TextView clear = findViewById(R.id.clear_text);
        Button submit = findViewById(R.id.submit_date);
        MaterialCalendarView calendar = findViewById(R.id.calendarView);

        submit.setOnClickListener((v) -> {
            Intent intent = new Intent();
            DateContainer container;
            if (calendar.getSelectionMode() == SELECTION_MODE_RANGE) {
                List<CalendarDay> days = calendar.getSelectedDates();
                CalendarDay startDay = days.get(0);
                CalendarDay endDay = days.get(days.size() - 1);
//                container = new DateContainer(RANG, )
            } else if (calendar.getSelectionMode() == SELECTION_MODE_SINGLE) {

            }
//            intent.putExtra()
//            setResult();

        });

        right.setOnClickListener(v -> {
            calendar.goToNext();
        });

        left.setOnClickListener(v -> {
            calendar.goToPrevious();
        });

        String action = getIntent().getAction();
        assert action != null;
        if (action.equals(RANG)) {
            calendar.setSelectionMode(SELECTION_MODE_RANGE);
        } else if (action.equals(Constants.DateSelectionMode.SINGLE)) {
            calendar.setSelectionMode(SELECTION_MODE_SINGLE);
        } else {
            calendar.setSelectionMode(SELECTION_MODE_NONE);
        }

        String language = Constants.getDefualtlanguage(context);
        if (language.isEmpty()) {
            language = "en";
        }
        String finalLanguage = language;

        calendar.setTopbarVisible(false);
        calendar.setOnMonthChangedListener((widget, date) -> {
            setTopCalendarBar(finalLanguage, date.getYear(), date.getMonth(), date.getDay());
        });
        calendar.setDayFormatter(day -> {
            String temp;
            if (finalLanguage.equals("en")) {
                temp = "" + day.getDay();
            } else {
                temp = FormatHelper.toPersianNumber("" + day.getDay());
            }
            return temp;
        });
        calendar.setWeekDayFormatter(dayOfWeek -> {
            String temp = " ";
            if (finalLanguage.equals("en")) {
                switch (dayOfWeek) {
                    case MONDAY:
                        temp = "mon";
                        break;
                    case TUESDAY:
                        temp = "tue";
                        break;
                    case WEDNESDAY:
                        temp = "wed";
                        break;
                    case THURSDAY:
                        temp = "thu";
                        break;
                    case FRIDAY:
                        temp = "fri";
                        break;
                    case SATURDAY:
                        temp = "sat";
                        break;
                    case SUNDAY:
                        temp = "sun";
                        break;
                }
            } else {
                switch (dayOfWeek) {
                    case MONDAY:
                        temp = "ج";
                        break;
                    case TUESDAY:
                        temp = "ش";
                        break;
                    case WEDNESDAY:
                        temp = "ی";
                        break;
                    case THURSDAY:
                        temp = "د";
                        break;
                    case FRIDAY:
                        temp = "س";
                        break;
                    case SATURDAY:
                        temp = "چ";
                        break;
                    case SUNDAY:
                        temp = "پ";
                        break;
                }
            }
            Typeface font = ResourcesCompat.getFont(context, R.font.anjoman_medium);
            SpannableString mNewTitle = new SpannableString(temp);

            mNewTitle.setSpan(new AbsoluteSizeSpan(20, true), 0, mNewTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            mNewTitle.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.calender_blue)), 0, mNewTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            mNewTitle.setSpan(new MainActivity.CustomTypefaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            return mNewTitle;
        });
        calendar.setOnDateChangedListener((widget, date, selected) -> {
            if (selected) {
                String temp = date.toString();
                Toast.makeText(context, temp, Toast.LENGTH_LONG).show();
            }
        });


        Date date = new Date();
        setTopCalendarBar(finalLanguage, date.getYear() + 1900, date.getMonth() + 1, date.getDay());
        calendar.setSelectedDate(getToday(finalLanguage));
        calendar.setCurrentDate(getToday(finalLanguage));
    }

    private CalendarDay getToday(String finalLanguage) {
        Date date = new Date();
        if (finalLanguage.equals("en")) {
            return CalendarDay.from(date.getYear() + 1900, date.getMonth() + 1, date.getDay());
        } else {
            Utilities.SolarCalendar calendar = new Utilities.SolarCalendar();
            int temp = calendar.month + 3;
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
        Typeface font = ResourcesCompat.getFont(context, R.font.anjoman_bold);
        SpannableString mNewTitle = new SpannableString(output_month);

        mNewTitle.setSpan(new AbsoluteSizeSpan(20, true), 0, mNewTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mNewTitle.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.black)), 0, mNewTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mNewTitle.setSpan(new MainActivity.CustomTypefaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        month.setText(mNewTitle);

        font = ResourcesCompat.getFont(context, R.font.anjoman_regular);
        mNewTitle = new SpannableString(output_year);

        mNewTitle.setSpan(new AbsoluteSizeSpan(16, true), 0, mNewTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mNewTitle.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.hit_gray)), 0, mNewTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mNewTitle.setSpan(new MainActivity.CustomTypefaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        year.setText(mNewTitle);
    }

}