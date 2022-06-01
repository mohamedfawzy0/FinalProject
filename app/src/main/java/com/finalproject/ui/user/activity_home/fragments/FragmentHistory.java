package com.finalproject.ui.user.activity_home.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.finalproject.R;


import com.finalproject.adapter.HistoryAdapter;
import com.finalproject.databinding.FragmentHistoryBinding;
import com.finalproject.model.HistoryModel;
import com.finalproject.mvvm.FragmentHistoryMvvm;
import com.finalproject.ui.common_uis.activity_base.BaseFragment;
import com.finalproject.ui.common_uis.activity_login.LoginActivity;
import com.finalproject.ui.user.activity_home.HomeActivity;
import com.finalproject.ui.user.activity_user_booking_details.UserBookingDetailsActivity;


public class FragmentHistory extends BaseFragment {
    private HomeActivity activity;
    private FragmentHistoryBinding binding;
    private HistoryAdapter historyAdapter;
    private FragmentHistoryMvvm mvvm;
    private ActivityResultLauncher<Intent> launcher;
    private int req = 1;

    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (HomeActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_history, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        mvvm = ViewModelProviders.of(this).get(FragmentHistoryMvvm.class);


        historyAdapter = new HistoryAdapter(activity, this,getLang());
        binding.recyclerHistory.setLayoutManager(new LinearLayoutManager(activity));
        binding.recyclerHistory.setAdapter(historyAdapter);

        mvvm.getIsLoadingLivData().observe(activity, isLoading -> binding.swipeRef.setRefreshing(isLoading));
        binding.swipeRef.setColorSchemeResources(R.color.primary_dark2);
        mvvm.getOnHistorySuccess().observe(activity, historyModels -> {
            if (historyModels!=null && historyModels.size() > 0) {
                binding.cardNoData.setVisibility(View.GONE);
            } else {
                binding.cardNoData.setVisibility(View.VISIBLE);
            }
            historyAdapter.updateList(historyModels);
        });
        if (getUserModel()!=null){
            mvvm.getHistory(getUserModel());
        }else {
            navigateToLoginActivity();
        }


        binding.swipeRef.setOnRefreshListener(() -> {
            if (getUserModel()!=null){
                mvvm.getHistory(getUserModel());
            }else {
                navigateToLoginActivity();
            }
        });
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (req == 1 && result.getResultCode()== Activity.RESULT_OK) {
                if (getUserModel()!=null){
                    mvvm.getHistory(getUserModel());
                }else {
                    navigateToLoginActivity();
                }
            }
        });
    }

    private void navigateToLoginActivity() {
        Intent intent=new Intent(activity, LoginActivity.class);
        startActivity(intent);
    }

    public void navigateToDetails(HistoryModel historyModel, int adapterPosition) {
        req=1;
        Intent intent=new Intent(activity, UserBookingDetailsActivity.class);
        intent.putExtra("model",historyModel);
        launcher.launch(intent);

    }
}