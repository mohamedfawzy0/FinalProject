package com.finalproject.model;

import android.content.Context;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;

import com.finalproject.BR;

import java.io.Serializable;

public class LoginModel extends BaseObservable implements Serializable {
    private String user_name;
    private String password;
    public ObservableField<String> error_user_name = new ObservableField<>();
    public ObservableField<String> error_password = new ObservableField<>();

    public LoginModel() {
        this.user_name = "";
        this.password = "";
    }

    public Boolean isDataValid(Context context) {

        if (!user_name.trim().isEmpty()
                &&
                !password.trim().isEmpty()
        ) {
            error_user_name.set(null);
            error_password.set(null);

            return true;
        } else {
            if (user_name.trim().isEmpty()) {
                error_user_name.set("Field is Required");
            } else {
                error_user_name.set(null);
            }
            if (password.trim().isEmpty()) {
                error_password.set("Field is Required");
            } else {
                error_password.set(null);
            }
            return false;
        }
    }

    @Bindable
    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
        notifyPropertyChanged(BR.user_name);
    }
    @Bindable
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        notifyPropertyChanged(BR.password);
    }
}
