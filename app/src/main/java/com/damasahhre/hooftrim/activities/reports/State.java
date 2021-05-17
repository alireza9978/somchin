package com.damasahhre.hooftrim.activities.reports;

/**
 * کلاسی برای مدیریت موقعیت برنامه بین چهار صفحه‌ برای ثبت گزارش
 */
enum State {

    info, reason, injury, moreInfo;

    public static int getNumber(State state) {
        switch (state) {
            case info:
                return 0;
            case reason:
                return 1;
            case injury:
                return 2;
            case moreInfo:
                return 3;
        }
        return 0;
    }

}
