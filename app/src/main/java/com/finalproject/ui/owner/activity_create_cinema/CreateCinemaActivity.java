package com.finalproject.ui.owner.activity_create_cinema;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.finalproject.R;
import com.finalproject.databinding.ActivityCreateCinemaBinding;
import com.finalproject.model.CreateCinemaModel;

import com.finalproject.model.UserModel;
import com.finalproject.mvvm.ActivityCreateCinemaMvvm;
import com.finalproject.preferences.Preferences;
import com.finalproject.share.Common;
import com.finalproject.ui.common_uis.activity_base.BaseActivity;
import com.finalproject.ui.owner.activity_home.OwnerHomeActivity;

public class CreateCinemaActivity extends BaseActivity {
    private ActivityCreateCinemaBinding binding;
    private CreateCinemaModel model;
    private ActivityCreateCinemaMvvm createCinemaMvvm;
    private UserModel userModel;
    private Preferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_cinema);
        initView();
    }

    private void initView() {
        preferences = Preferences.getInstance();
        userModel=getUserModel();
        Log.e("user",userModel.getData().getId());
        setUpToolbar(binding.toolbar, getString(R.string.create_cinema), R.color.color2, R.color.white);
        createCinemaMvvm= ViewModelProviders.of(this).get(ActivityCreateCinemaMvvm.class);

        createCinemaMvvm.getOnCinemaSuccess().observe(this, cinemaModel -> {
            if (cinemaModel!=null){
                userModel.getData().setCinema(cinemaModel);
                setUserModel(userModel);
                navigateToHomeActivity();
            }
        });
        binding.setLang(getLang());
        model = new CreateCinemaModel();
        binding.setModel(model);


        binding.toolbar.llBack.setOnClickListener(view -> {
            if (userModel!=null){
                finish();
            }else {
                logout();
            }

        });
        binding.btnCreate.setOnClickListener(view -> {
            Common.CloseKeyBoard(this, binding.etName);
            Common.CloseKeyBoard(this, binding.etPrice);
            Common.CloseKeyBoard(this, binding.etLocation);
            Common.CloseKeyBoard(this, binding.etNumber);
            if (model.isDataValid(this)){
                createCinemaMvvm.CreateCinema(getUserModel(),model,this);

            }
        });


    }

    private void navigateToHomeActivity() {
        Intent intent = new Intent(this, OwnerHomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void logout() {
        clearUserModel(this);
        finish();
    }

}