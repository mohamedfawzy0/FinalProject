package com.finalproject.ui.owner.activity_owner_booking_details;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.finalproject.R;
import com.finalproject.adapter.OwnerBookingTimesAdapter;
import com.finalproject.adapter.DayAdapter;
import com.finalproject.databinding.ActivityOwnerBookingDetailsBinding;
import com.finalproject.model.DayModel;
import com.finalproject.model.OwnerHistoryModel;
import com.finalproject.model.PostModel;
import com.finalproject.mvvm.ActivityOwnerBookingDetailsMvvm;
import com.finalproject.ui.common_uis.activity_base.BaseActivity;


import java.util.ArrayList;
import java.util.List;

public class OwnerBookingDetailsActivity extends BaseActivity {
    private ActivityOwnerBookingDetailsBinding binding;
    private DayAdapter dayAdapter;
    private List<DayModel> dayModelList;
    private OwnerBookingTimesAdapter timesAdapter;
    private List<OwnerHistoryModel> timeModelList;
    private ActivityOwnerBookingDetailsMvvm mvvm;
    private PostModel postModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_owner_booking_details);
        getDAtaFromIntent();
        initView();
    }

    private void getDAtaFromIntent() {
        Intent intent = getIntent();
        postModel = (PostModel) intent.getSerializableExtra("model");
    }

    private void initView() {
        binding.setModel(postModel);
        mvvm = ViewModelProviders.of(this).get(ActivityOwnerBookingDetailsMvvm.class);
        mvvm.getIsLoadingLivData().observe(this, isLoading -> binding.swipeRef.setRefreshing(isLoading));

        setUpToolbar(binding.toolbar, getString(R.string.bookings_details), R.color.color2, R.color.white);
        binding.toolbar.llBack.setOnClickListener(view -> finish());
        dayModelList = new ArrayList<>();
        timeModelList = new ArrayList<>();

        mvvm.getIsLoadingLivData().observe(this, loading -> {
            binding.swipeRef.setRefreshing(loading);
        });
        mvvm.getOnDaysSuccess().observe(this, dayModels -> {
            if (dayModels.size() > 0) {
                binding.tvNoDays.setVisibility(View.GONE);
                dayModelList.clear();
                dayModelList.addAll(dayModels);
                if (dayAdapter != null) {
                    dayAdapter.updateList(dayModels);
                }
                binding.llTime.setVisibility(View.VISIBLE);
            } else {
                binding.tvNoDays.setVisibility(View.VISIBLE);
                binding.llTime.setVisibility(View.GONE);
                dayAdapter.updateList(new ArrayList<>());
            }

        });
        mvvm.getDays(getUserModel(), postModel.getId());
        mvvm.getOnHoursSuccess().observe(this, hours -> {
            if (mvvm.getDayId() != null) {
                if (hours.size() > 0) {
                    binding.tvNoTimes.setVisibility(View.GONE);
                    timeModelList.clear();
                    timeModelList.addAll(hours);
                    if (timesAdapter != null) {
                        timesAdapter.updateList(hours);
                    }
                } else {
                    binding.tvNoTimes.setVisibility(View.VISIBLE);
                    timesAdapter.updateList(new ArrayList<>());
                }
            }

        });

        dayAdapter = new DayAdapter(dayModelList, this);
        binding.recViewDays.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.recViewDays.setAdapter(dayAdapter);

        timesAdapter = new OwnerBookingTimesAdapter(timeModelList, this, getLang());
        binding.recViewTimes.setLayoutManager(new LinearLayoutManager(this));
        binding.recViewTimes.setAdapter(timesAdapter);

        binding.swipeRef.setOnRefreshListener(() -> {
            if (mvvm.getDayId().getValue() != null) {
                mvvm.getOwnerBookings(getUserModel(), postModel.getId());
            } else {
                binding.swipeRef.setRefreshing(false);
            }
        });
        binding.swipeRef.setColorSchemeResources(R.color.primary_dark2);

    }

    public void setDayItem(DayModel model, int currentPos) {
        mvvm.getDayId().setValue(model.getId());
        mvvm.getOwnerBookings(getUserModel(), postModel.getId());
    }

}