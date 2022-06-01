package com.finalproject.ui.user.activity_cinema_users;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.finalproject.R;
import com.finalproject.adapter.CinemaUsersAdapter;

import com.finalproject.databinding.ActivityCinemasUserBinding;
import com.finalproject.model.CinemaModel;
import com.finalproject.model.PostModel;
import com.finalproject.mvvm.ActivityCinemasMvvm;
import com.finalproject.ui.common_uis.activity_base.BaseActivity;
import com.finalproject.ui.user.activity_booking_seats.BookingSeatsActivity;

public class CinemasUserActivity extends BaseActivity {
    private String lang;
    private CinemaUsersAdapter cinemaUsersAdapter;
    private ActivityCinemasUserBinding binding;
    private ActivityCinemasMvvm mvvm;
    private String id;
    private PostModel model;
    private int req;
    private ActivityResultLauncher<Intent> launcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cinemas_user);
        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        model = (PostModel) intent.getSerializableExtra("postModel");
    }

    private void initView() {
        setUpToolbar(binding.toolbar, getString(R.string.choose_Cinema), R.color.color2, R.color.white);
        binding.toolbar.llBack.setOnClickListener(view -> finish());
        mvvm = ViewModelProviders.of(this).get(ActivityCinemasMvvm.class);

        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (req == 1 && result.getResultCode() == Activity.RESULT_OK) {
                setResult(RESULT_OK);
                finish();
            }
        });
        mvvm.getIsLoading().observe(this, isLoading -> {
            binding.swipeRef.setRefreshing(isLoading);
        });

        mvvm.getOnCinemaSuccess().observe(this, cinemaModels -> {
            if (cinemaModels.size() > 0) {
                binding.cardNoData.setVisibility(View.GONE);
                if (cinemaUsersAdapter != null) {
                    cinemaUsersAdapter.updateList(cinemaModels);
                }
            } else {
                binding.cardNoData.setVisibility(View.VISIBLE);
            }
        });
        mvvm.getCinemas(model.getId());
//        Log.e("iddd",model.getId());
        binding.swipeRef.setOnRefreshListener(() -> {
            mvvm.getCinemas(model.getId());
        });
        binding.swipeRef.setColorSchemeResources(R.color.colorPrimary);
        cinemaUsersAdapter = new CinemaUsersAdapter(this, getLang());
        binding.recViewCinemas.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        binding.recViewCinemas.setAdapter(cinemaUsersAdapter);
    }


    public void navigateToBookingActivity(CinemaModel cinemaModel, int position) {
        req = 1;
        Intent intent = new Intent(CinemasUserActivity.this, BookingSeatsActivity.class);
        intent.putExtra("postModel", model);
        intent.putExtra("cinemaModel", cinemaModel);
        launcher.launch(intent);
    }
}