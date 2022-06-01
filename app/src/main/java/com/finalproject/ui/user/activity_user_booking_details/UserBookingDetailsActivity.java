package com.finalproject.ui.user.activity_user_booking_details;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.finalproject.R;
import com.finalproject.databinding.ActivityUserBookingDetailsBinding;
import com.finalproject.model.HistoryModel;
import com.finalproject.mvvm.ActivityUserBookingDetailsMvvm;
import com.finalproject.ui.common_uis.activity_base.BaseActivity;
import com.finalproject.ui.user.activity_home.HomeActivity;

public class UserBookingDetailsActivity extends BaseActivity {
    private ActivityUserBookingDetailsBinding binding;
    private HistoryModel historyModel;
    private ActivityUserBookingDetailsMvvm mvvm;
    private HomeActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_user_booking_details);
        getDataFromIntent();
        initView();
    }
    private void getDataFromIntent() {
        Intent intent = getIntent();
        historyModel = (HistoryModel) intent.getSerializableExtra("model");
    }

    private void initView() {
        mvvm = ViewModelProviders.of(this).get(ActivityUserBookingDetailsMvvm.class);

        mvvm.cancelBooking.observe(this, canceled -> {
            if (canceled){
                Toast.makeText(this, R.string.canceled, Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            }
        });
        setUpToolbar(binding.toolbar, getString(R.string.booking_details), R.color.color2, R.color.white);
        binding.toolbar.llBack.setOnClickListener(view -> finish());
        binding.setModel(historyModel);

        binding.btnCancel.setOnClickListener(view -> {
            mvvm.cancelBooking(this,historyModel.getId());
        });

    }


}