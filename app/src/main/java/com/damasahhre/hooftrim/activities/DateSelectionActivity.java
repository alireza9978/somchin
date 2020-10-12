package com.damasahhre.hooftrim.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.damasahhre.hooftrim.R;
import com.damasahhre.hooftrim.constants.Constants;
import com.damasahhre.hooftrim.constants.Utilities;
import com.damasahhre.hooftrim.models.DateContainer;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ir.mirrajabi.persiancalendar.PersianCalendarView;
import ir.mirrajabi.persiancalendar.core.PersianCalendarHandler;
import ir.mirrajabi.persiancalendar.core.models.PersianDate;

import static com.damasahhre.hooftrim.constants.Constants.DateSelectionMode.RANG;
import static com.damasahhre.hooftrim.constants.Constants.DateSelectionMode.SINGLE;
import static com.damasahhre.hooftrim.models.DateContainer.MyDate;
import static com.prolificinteractive.materialcalendarview.MaterialCalendarView.SELECTION_MODE_NONE;
import static com.prolificinteractive.materialcalendarview.MaterialCalendarView.SELECTION_MODE_RANGE;
import static com.prolificinteractive.materialcalendarview.MaterialCalendarView.SELECTION_MODE_SINGLE;

public class DateSelectionActivity extends AppCompatActivity {

    private Context context = this;
    private TextView month;
    private TextView year;
    private MaterialCalendarView calendar;
    private PersianCalendarView calendarView;
    private ImageView right;
    private ImageView left;
    private TextView startDate;
    private TextView endDate;
    private TextView clear;
    private Button submit;
    private boolean rang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_selection);

        right = findViewById(R.id.right_arrow);
        ImageView close = findViewById(R.id.close_image);
        left = findViewById(R.id.left_arrow);
        startDate = findViewById(R.id.start_date);
        endDate = findViewById(R.id.end_date);
        clear = findViewById(R.id.clear_text);
        submit = findViewById(R.id.submit_date);
        month = findViewById(R.id.month_title);
        year = findViewById(R.id.year_title);
        calendar = findViewById(R.id.calendarView);
        calendarView = findViewById(R.id.persian_calendar);

        Configuration config = context.getResources().getConfiguration();
        String language;
        if (config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
            language = "fa";
        } else {
            language = "en";
        }

        String action = getIntent().getAction();
        assert action != null;
        if (action.equals(RANG)) {
            calendar.setSelectionMode(SELECTION_MODE_RANGE);
            startDate.setVisibility(View.VISIBLE);
            endDate.setVisibility(View.VISIBLE);
            startDate.setText("");
            endDate.setText("");
            rang = true;
        } else if (action.equals(SINGLE)) {
            calendar.setSelectionMode(SELECTION_MODE_SINGLE);
            startDate.setVisibility(View.GONE);
            endDate.setVisibility(View.GONE);
            rang = false;
        } else {
            calendar.setSelectionMode(SELECTION_MODE_NONE);
            rang = false;
        }

        if (language.equals("en")) {
            calendar.setVisibility(View.VISIBLE);
            calendarView.setVisibility(View.INVISIBLE);
            setEnglish();
        } else {
            calendarView.setVisibility(View.VISIBLE);
            calendar.setVisibility(View.INVISIBLE);
            setPersian();
        }

        close.setOnClickListener(view -> finish());

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
        if (rang) {
            calendar.setOnRangeSelectedListener((widget, dates) -> {
                List<CalendarDay> days = calendar.getSelectedDates();
                CalendarDay startDay = days.get(0);
                CalendarDay endDay = days.get(days.size() - 1);
                DateContainer container = new DateContainer(RANG,
                        new MyDate(false, startDay.getDay(), startDay.getMonth(), startDay.getYear()),
                        new MyDate(false, endDay.getDay(), endDay.getMonth(), endDay.getYear()));
                startDate.setText(container.getStartDate().toStringWithoutYear(context));
                endDate.setText(container.getEndDate().toStringWithoutYear(context));
            });
            calendar.setOnDateChangedListener((widget, date, selected) -> {
                startDate.setText("");
                endDate.setText("");
            });
        }
        clear.setOnClickListener(view -> {
            calendar.clearSelection();
            CalendarDay today = CalendarDay.today();
            calendar.setCurrentDate(today);
            startDate.setText("");
            endDate.setText("");
        });
        right.setOnClickListener(v -> calendar.goToNext());
        left.setOnClickListener(v -> calendar.goToPrevious());
        submit.setOnClickListener((v) -> {
            Intent intent = new Intent();
            DateContainer container = null;
            if (calendar.getSelectionMode() == SELECTION_MODE_RANGE) {
                List<CalendarDay> days = calendar.getSelectedDates();
                CalendarDay startDay = days.get(0);
                CalendarDay endDay = days.get(days.size() - 1);
                container = new DateContainer(RANG,
                        new MyDate(false, startDay.getDay(), startDay.getMonth(), startDay.getYear()),
                        new MyDate(false, endDay.getDay(), endDay.getMonth(), endDay.getYear()));
            } else if (calendar.getSelectionMode() == SELECTION_MODE_SINGLE) {
                CalendarDay day = calendar.getSelectedDate();
                assert day != null;
                container = new DateContainer(SINGLE,
                        new MyDate(false, day.getDay(), day.getMonth(), day.getYear()));
            }
            if (container == null) {
                setResult(Constants.DATE_SELECTION_FAIL);
            }
            intent.putExtra(Constants.DATE_SELECTION_RESULT, container);
            setResult(Constants.DATE_SELECTION_OK, intent);
            finish();
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

    private void setTopBarFa(PersianDate persianDate) {
        String monthName = getPersianMonthName(persianDate.getMonth());
        String yearName = "" + persianDate.getYear();

        Typeface font = ResourcesCompat.getFont(context, R.font.anjoman_bold);
        SpannableString mNewTitle = new SpannableString(monthName);

        mNewTitle.setSpan(new AbsoluteSizeSpan(20, true), 0, mNewTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mNewTitle.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.black)), 0, mNewTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mNewTitle.setSpan(new MainActivity.CustomTypefaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        month.setText(mNewTitle);

        font = ResourcesCompat.getFont(context, R.font.anjoman_regular);
        mNewTitle = new SpannableString(yearName);

        mNewTitle.setSpan(new AbsoluteSizeSpan(16, true), 0, mNewTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mNewTitle.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.hit_gray)), 0, mNewTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mNewTitle.setSpan(new MainActivity.CustomTypefaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        year.setText(mNewTitle);
    }

    private int state = 0;
    private PersianDate monthDate;
    private PersianDate startPersianDate;
    private PersianDate endPersianDate;

    private void setPersian() {
        PersianCalendarHandler calendarHandler = calendarView.getCalendar();
        calendarHandler.setOnMonthChangedListener(persianDate -> {
            monthDate = persianDate;
            setTopBarFa(persianDate);
        });
        calendarHandler.setOnDayClickedListener(persianDate -> {
            if (rang) {
                DateContainer dateContainer = new DateContainer(SINGLE,
                        new MyDate(true, persianDate.getDayOfMonth(), persianDate.getMonth(), persianDate.getYear()));
                if (state == 0) {
                    startDate.setText(dateContainer.toString(context));
                    startPersianDate = persianDate;
                    endPersianDate = null;
                    endDate.setText("");
                    state = 1;
                } else if (state == 1) {
                    endDate.setText(dateContainer.toString(context));
                    endPersianDate = persianDate;
                    state = 0;
                }
            } else {
                startPersianDate = persianDate;
            }
        });
        left.setOnClickListener(v -> {
            if (monthDate == null) {
                monthDate = calendarHandler.getToday();
            }
            int month = monthDate.getMonth() + 1;
            int year = monthDate.getYear();
            if (month > 12) {
                month = 1;
                year += 1;
            }
            monthDate.setMonth(month);
            monthDate.setYear(year);
            calendarView.goToDate(monthDate);
        });
        right.setOnClickListener(v -> {
            if (monthDate == null) {
                monthDate = calendarHandler.getToday();
            }
            int month = monthDate.getMonth() - 1;
            int year = monthDate.getYear();
            if (month == 0) {
                month = 12;
                year -= 1;
            }
            monthDate.setMonth(month);
            monthDate.setYear(year);
            calendarView.goToDate(monthDate);
        });
        submit.setOnClickListener((v) -> {
            Intent intent = new Intent();
            DateContainer container = null;
            if (rang) {
                container = new DateContainer(RANG,
                        new MyDate(true, startPersianDate.getDayOfMonth(), startPersianDate.getMonth(), startPersianDate.getYear()),
                        new MyDate(true, endPersianDate.getDayOfMonth(), endPersianDate.getMonth(), endPersianDate.getYear()));
            } else if (calendar.getSelectionMode() == SELECTION_MODE_SINGLE) {
                container = new DateContainer(SINGLE,
                        new MyDate(true, startPersianDate.getDayOfMonth(), startPersianDate.getMonth(), startPersianDate.getYear()));
            }
            if (container == null) {
                setResult(Constants.DATE_SELECTION_FAIL);
            }
            intent.putExtra(Constants.DATE_SELECTION_RESULT, container);
            setResult(Constants.DATE_SELECTION_OK, intent);
            finish();
        });
        clear.setOnClickListener(v -> {
            if (rang) {
                state = 0;
                calendarView.goToToday();
                startPersianDate = calendarHandler.getToday();
                startDate.setText(startPersianDate.toString());
                endDate.setText("");
            } else {
                calendarView.goToToday();
            }
        });
        setTopBarFa(calendarHandler.getToday());
    }

    private String getPersianMonthName(int month) {
        String strMonth = " ";
        switch (month) {
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
        return strMonth;
    }


}