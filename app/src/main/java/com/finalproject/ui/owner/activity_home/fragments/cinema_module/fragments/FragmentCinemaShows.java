package com.finalproject.ui.owner.activity_home.fragments.cinema_module.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;

import com.finalproject.R;
import com.finalproject.adapter.OwnerHistoryAdapter;
import com.finalproject.databinding.FragmentCinemaShowsBinding;
import com.finalproject.model.PostModel;
import com.finalproject.mvvm.FragmentCinemaDataMvvm;
import com.finalproject.ui.common_uis.activity_base.BaseFragment;
import com.finalproject.ui.owner.activity_home.OwnerHomeActivity;
import com.finalproject.ui.owner.activity_owner_booking_details.OwnerBookingDetailsActivity;

import java.util.ArrayList;
import java.util.List;


public class FragmentCinemaShows extends BaseFragment {
    private OwnerHomeActivity activity;
    private FragmentCinemaShowsBinding binding;
    private OwnerHistoryAdapter historyAdapter;
    private FragmentCinemaDataMvvm mvvm;
    private ActivityResultLauncher<Intent> launcher;
    private int req;

    public static FragmentCinemaShows newInstance() {
        FragmentCinemaShows fragment = new FragmentCinemaShows();
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (OwnerHomeActivity) context;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cinema_shows, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        mvvm = ViewModelProviders.of(this).get(FragmentCinemaDataMvvm.class);

        mvvm.getIsLoading().observe(activity, isLoading -> {
            binding.swipeRef.setRefreshing(isLoading);
        });

        mvvm.getOnDataSuccess().observe(activity, showModels -> {
            if (showModels.size() > 0) {

                binding.tvNoData.setVisibility(View.GONE);
                if (historyAdapter != null) {
                    historyAdapter.updateList(showModels);
                }

            } else {
                binding.tvNoData.setVisibility(View.VISIBLE);
                historyAdapter.updateList(new ArrayList<>());
            }
        });

        mvvm.getMoviesOrShows(getUserModel().getData().getCinema().getId(),"show");
        historyAdapter = new OwnerHistoryAdapter(activity, this);
        binding.recViewShows.setLayoutManager(new GridLayoutManager(activity, 2));
        binding.recViewShows.setAdapter(historyAdapter);
        binding.swipeRef.setColorSchemeResources(R.color.primary_dark2);
        binding.swipeRef.setOnRefreshListener(()->mvvm.getMoviesOrShows(getUserModel().getData().getCinema().getId(),"show"));

        mvvm.getRemove().observe(activity, pos -> {
            if(req==1) {

                if (historyAdapter != null) {
                    Toast.makeText(activity, R.string.movie_removed, Toast.LENGTH_SHORT).show();
                    mvvm.getMoviesOrShows(FragmentCinemaShows.this.getUserModel().getData().getCinema().getId(), "show");

                }
                req=0;
            }
        });

    }

    public void navigateToDetails(int adapterPosition, PostModel postModel) {
        Intent intent = new Intent(activity, OwnerBookingDetailsActivity.class);
        intent.putExtra("model", postModel);
        startActivity(intent);
    }

    public void delete(int adapterPosition, PostModel postModel) {
        req=1;
        mvvm.removeFromCinema(activity,getUserModel().getData().getCinema().getId(),postModel.getId(),adapterPosition);
//        showModelList.remove(adapterPosition);
//        historyAdapter.updateList(showModelList);
    }
}