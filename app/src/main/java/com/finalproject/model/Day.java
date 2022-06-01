package com.finalproject.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Day implements Serializable {
    private String id;
    private String day;
    private List<Time> times=new ArrayList<>();

    public Day(String day) {
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


    public List<Time> getTimeModelList() {
        return times;
    }

    public void setTimeModelList(List<Time> timeModelList) {
        this.times = timeModelList;
    }
}
