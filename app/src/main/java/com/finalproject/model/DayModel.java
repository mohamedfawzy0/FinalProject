package com.finalproject.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DayModel implements Serializable {
    private String id;
    private String day;
    private boolean isSelected;
    private List<TimeModel> times=new ArrayList<>();

    public DayModel() {
    }

    public DayModel(String day) {
        this.day = day;
    }


    public String getId() {
        return id;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public List<TimeModel> getTimeModelList() {
        return times;
    }

    public void setTimeModelList(List<TimeModel> timeModelList) {
        this.times = timeModelList;
    }
}
