package com.finalproject.ui.owner.activity_home.fragments.cinema_module.fragments;

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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.finalproject.R;
import com.finalproject.adapter.OwnerHistoryAdapter;
import com.finalproject.databinding.FragmentCinemaMoviesBinding;
import com.finalproject.model.PostModel;
import com.finalproject.mvvm.FragmentCinemaDataMvvm;
import com.finalproject.ui.common_uis.activity_base.BaseFragment;
import com.finalproject.ui.owner.activity_home.OwnerHomeActivity;
import com.finalproject.ui.owner.activity_owner_booking_details.OwnerBookingDetailsActivity;

import java.util.ArrayList;
import java.util.List;


public class FragmentCinemaMovies extends BaseFragment {
    private OwnerHomeActivity activity;
    private FragmentCinemaMoviesBinding binding;
    private OwnerHistoryAdapter historyAdapter;
    private FragmentCinemaDataMvvm mvvm;
    private int req;


    public static FragmentCinemaMovies newInstance() {
        FragmentCinemaMovies fragment = new FragmentCinemaMovies();

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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cinema_movies, container, false);
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

        mvvm.getOnDataSuccess().observe(activity, movieModels -> {
            Log.e("fllfl",movieModels.size()+"");
            if (movieModels.size() > 0) {
                binding.tvNoData.setVisibility(View.GONE);


                if (historyAdapter != null) {
                    Log.e("fllfl",movieModels.size()+"");

                    historyAdapter.updateList(movieModels);
                }
            } else {
               // Log.e("kkkkk","lllll");
                historyAdapter.updateList(new ArrayList<>());
                //mvvm.getOnDataSuccess().getValue().remove(0);
            }

        });

        mvvm.getMoviesOrShows(getUserModel().getData().getCinema().getId(), "move");
        historyAdapter = new OwnerHistoryAdapter(activity, this);
        binding.recViewMovies.setLayoutManager(new GridLayoutManager(activity, 2));
        binding.recViewMovies.setAdapter(historyAdapter);
        binding.swipeRef.setColorSchemeResources(R.color.primary_dark2);
        binding.swipeRef.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mvvm.getMoviesOrShows(FragmentCinemaMovies.this.getUserModel().getData().getCinema().getId(), "move");
            }
        });

        mvvm.getRemove().observe(activity, pos -> {
            Log.e("?kkkkk",req+" "+pos+" ");
            if(req==1) {
                if (historyAdapter != null) {

                    Toast.makeText(activity, R.string.movie_removed, Toast.LENGTH_SHORT).show();
                    mvvm.getMoviesOrShows(FragmentCinemaMovies.this.getUserModel().getData().getCinema().getId(), "move");


                } else {
                    Log.e("?kkkkk",req+" "+pos+" ");

                    historyAdapter.updateList(new ArrayList<>());
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
        Log.e("llll",adapterPosition+"");
        req=1;
        mvvm.removeFromCinema(activity, getUserModel().getData().getCinema().getId(), postModel.getId(), adapterPosition);
    }
}