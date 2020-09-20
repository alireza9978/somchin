package com.damasahhre.hooftrim.models;

import java.io.Serializable;
import java.util.Date;

public class DateContainer implements Serializable {

    private String mode;
    private String start;
    private String end;
    private Date startDate;
    private Date endDate;

    public DateContainer(String mode, String start, Date startDate) {
        this.mode = mode;
        this.start = start;
        this.startDate = startDate;
    }

    public DateContainer(String mode, String start, String end, Date startDate, Date endDate) {
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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
