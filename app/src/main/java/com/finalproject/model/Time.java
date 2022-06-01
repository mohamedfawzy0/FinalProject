package com.finalproject.model;

import java.io.Serializable;

public class Time implements Serializable {
    public String id;
    public String hour;

    public Time(String hour) {
        this.hour = hour;
    }

    public String getId() {
        return id;
    }

    public String getHour() {
        return hour;
    }

}
