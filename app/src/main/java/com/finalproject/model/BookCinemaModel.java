package com.finalproject.model;

import android.content.Context;
import android.widget.Toast;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;

import com.finalproject.BR;
import com.finalproject.R;

public class BookCinemaModel extends BaseObservable {
    private String user_id;
    private String cinema_id;
    private String post_id;
    private String day_id;
    private String hour_id;
    private String number_of_seats;
    private String total_price;
    private String ticket_type;


    public boolean isDataValid(Context context) {
        if (!cinema_id.trim().isEmpty() &&
                !post_id.trim().isEmpty() &&
                !day_id.trim().isEmpty() &&
                !hour_id.trim().isEmpty() &&
                !number_of_seats.trim().isEmpty() &&
                !total_price.trim().isEmpty() &&
                !ticket_type.trim().equals("0")) {
            return true;
        } else {
            if (user_id.isEmpty()) {
                Toast.makeText(context, R.string.user_not_found,Toast.LENGTH_SHORT).show();
            } else {

            }
            if (cinema_id.isEmpty()) {
                Toast.makeText(context, R.string.choose_cinema, Toast.LENGTH_SHORT).show();
            } else {

            }
            if (post_id.isEmpty()) {
                Toast.makeText(context, R.string.choose_movie_or_show, Toast.LENGTH_SHORT).show();
            } else {

            }
            if (day_id.isEmpty()) {
                Toast.makeText(context, R.string.choose_day, Toast.LENGTH_SHORT).show();
            } else {

            }
            if (hour_id.isEmpty()) {
                Toast.makeText(context, R.string.choose_hour, Toast.LENGTH_SHORT).show();
            } else {

            }

            if (number_of_seats.isEmpty()) {
                Toast.makeText(context, R.string.select_seats, Toast.LENGTH_SHORT).show();
            } else {

            }
            if (ticket_type.equals("0")) {
                Toast.makeText(context, R.string.choose_ticket_type, Toast.LENGTH_SHORT).show();
            }else {

            }

            return false;
        }
    }

    public BookCinemaModel() {
        setUser_id("");
        setCinema_id("");
        setPost_id("");
        setDay_id("");
        setHour_id("");
        setNumber_of_seats("");
        setTotal_price("");
        setTicket_type("");
    }

    @Bindable
    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
        notifyPropertyChanged(BR.user_id);
    }

    @Bindable
    public String getCinema_id() {
        return cinema_id;
    }

    public void setCinema_id(String cinema_id) {
        this.cinema_id = cinema_id;
        notifyPropertyChanged(BR.cinema_id);
    }

    @Bindable
    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
        notifyPropertyChanged(BR.post_id);
    }

    @Bindable
    public String getDay_id() {
        return day_id;
    }

    public void setDay_id(String day_id) {
        this.day_id = day_id;
        notifyPropertyChanged(BR.day_id);
    }

    @Bindable
    public String getHour_id() {
        return hour_id;
    }

    public void setHour_id(String hour_id) {
        this.hour_id = hour_id;
        notifyPropertyChanged(BR.hour_id);
    }

    @Bindable
    public String getNumber_of_seats() {
        return number_of_seats;
    }

    public void setNumber_of_seats(String number_of_seats) {
        this.number_of_seats = number_of_seats;
        notifyPropertyChanged(BR.number_of_seats);
    }

    @Bindable
    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
        notifyPropertyChanged(BR.total_price);
    }

    public String getTicket_type() {
        return ticket_type;
    }

    public void setTicket_type(String ticket_type) {
        this.ticket_type = ticket_type;
    }
}
