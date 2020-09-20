package com.damasahhre.hooftrim.models;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.io.Serializable;

import ir.mirrajabi.persiancalendar.core.models.PersianDate;

public class DateContainer implements Serializable {

    private String mode;
    private String start;
    private String end;
    private CalendarDay startDate;
    private CalendarDay endDate;
    private PersianDate startPersianDate;
    private PersianDate endPersianDate;

    public DateContainer(String mode, String start, String end, PersianDate startPersianDate, PersianDate endPersianDate) {
        this.mode = mode;
        this.start = start;
        this.end = end;
        this.startPersianDate = startPersianDate;
        this.endPersianDate = endPersianDate;
    }

    public DateContainer(String mode, String start, PersianDate startPersianDate) {
        this.mode = mode;
        this.start = start;
        this.startPersianDate = startPersianDate;
    }

    public DateContainer(String mode, String start, CalendarDay startDate) {
        this.mode = mode;
        this.start = start;
        this.startDate = startDate;
    }

    public DateContainer(String mode, String start, String end, CalendarDay startDate, CalendarDay endDate) {
        this.mode = mode;
        this.start = start;
        this.end = end;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public CalendarDay getStartDate() {
        return startDate;
    }

    public void setStartDate(CalendarDay startDate) {
        this.startDate = startDate;
    }

    public CalendarDay getEndDate() {
        return endDate;
    }

    public void setEndDate(CalendarDay endDate) {
        this.endDate = endDate;
    }

    public PersianDate getStartPersianDate() {
        return startPersianDate;
    }

    public void setStartPersianDate(PersianDate startPersianDate) {
        this.startPersianDate = startPersianDate;
    }

    public PersianDate getEndPersianDate() {
        return endPersianDate;
    }

    public void setEndPersianDate(PersianDate endPersianDate) {
        this.endPersianDate = endPersianDate;
    }
}
