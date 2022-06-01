package com.finalproject.model;

import android.content.Context;
import android.widget.Toast;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AddDayTimeModel implements Serializable {
    private String post_id;
    private String cinema_id;
    private List<Day> days;


    public AddDayTimeModel() {
        this.post_id = "";
        this.cinema_id = "";
        this.days = new ArrayList<>();

    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getCinema_id() {
        return cinema_id;
    }

    public void setCinema_id(String cinema_id) {
        this.cinema_id = cinema_id;
    }


    public List<Day> getDays() {
        return days;
    }

    public void setDays(List<Day> days) {
        this.days = days;

    }


}
