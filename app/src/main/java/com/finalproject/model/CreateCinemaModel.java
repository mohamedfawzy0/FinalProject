package com.finalproject.model;

import android.content.Context;
import android.widget.Toast;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;

import com.finalproject.BR;
import com.finalproject.R;

import java.io.Serializable;
import java.util.List;

public class CreateCinemaModel extends BaseObservable {
    private String user_id;
    private String title;
    private String location;
    private String chairs_count;
    private String price;


    public ObservableField<String> error_title = new ObservableField<>();
    public ObservableField<String> error_location = new ObservableField<>();
    public ObservableField<String> error_chairs_count = new ObservableField<>();
    public ObservableField<String> error_price = new ObservableField<>();

    public boolean isDataValid(Context context) {
        if (!title.isEmpty() &&
                !location.isEmpty() &&
                !chairs_count.isEmpty() &&
                !price.isEmpty()) {
            error_title.set(null);
            error_location.set(null);
            error_chairs_count.set(null);
            error_price.set(null);
            return true;
        } else {
            if (title.isEmpty()) {
                error_title.set(context.getString(R.string.field_required));
            } else {
                error_title.set(null);
            }
            if (location.isEmpty()) {
                error_location.set(context.getString(R.string.field_required));
            } else {
                error_location.set(null);
            }
            if (chairs_count.isEmpty()) {
                error_chairs_count.set(context.getString(R.string.field_required));

            } else {
                error_chairs_count.set(null);

            }
            if (price.isEmpty()) {
                error_price.set(context.getString(R.string.field_required));
            } else {
                error_price.set(null);
            }
            return false;
        }
    }

    public CreateCinemaModel() {
        this.user_id = "";
        this.title = "";
        this.location = "";
        this.chairs_count = "";
        this.price = "";
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    @Bindable
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        notifyPropertyChanged(BR.title);
    }

    @Bindable
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
        notifyPropertyChanged(BR.location);
    }

    @Bindable
    public String getChairs_count() {
        return chairs_count;
    }

    public void setChairs_count(String chairs_count) {
        this.chairs_count = chairs_count;
        notifyPropertyChanged(BR.chairs_count);
    }

    @Bindable
    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
        notifyPropertyChanged(BR.price);
    }
}
