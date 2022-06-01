package com.finalproject.model;

import java.io.Serializable;

public class HistoryModel implements Serializable {
    private String id;
    private String user_id;
    private String cinema_id;
    private String post_id;
    private String day_id;
    private String hour_id;
    private String number_of_seats;
    private String total_price;
    private String ticket_type;
    private String created_at;
    private String updated_at;
    private PostModel post;
    private CinemaModel.Model cinema;
    private TimeModel hour;
    private DayModel day;

    public String getId() {
        return id;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getCinema_id() {
        return cinema_id;
    }

    public String getPost_id() {
        return post_id;
    }

    public String getDay_id() {
        return day_id;
    }

    public String getHour_id() {
        return hour_id;
    }

    public String getNumber_of_seats() {
        return number_of_seats;
    }

    public String getTotal_price() {
        return total_price;
    }

    public String getTicket_type() {
        return ticket_type;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public PostModel getPost() {
        return post;
    }

    public CinemaModel.Model getCinema() {
        return cinema;
    }

    public TimeModel getHour() {
        return hour;
    }

    public DayModel getDay() {
        return day;
    }
}
