package com.finalproject.ui.user.activity_noification;


import android.os.Bundle;

import com.finalproject.R;
import com.finalproject.ui.common_uis.activity_base.BaseActivity;

public class NotificationActivity extends BaseActivity {
    private String lang;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
    }
}