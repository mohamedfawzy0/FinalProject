package com.finalproject.ui.common_uis.activity_login;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;

import android.os.Bundle;
import android.widget.Toast;


import com.finalproject.R;
import com.finalproject.databinding.ActivityLoginBinding;
import com.finalproject.model.LoginModel;
import com.finalproject.mvvm.ActivityLoginMvvm;
import com.finalproject.share.Common;
import com.finalproject.ui.common_uis.activity_base.BaseActivity;
import com.finalproject.ui.common_uis.activity_signup.SignUpActivity;
import com.finalproject.ui.owner.activity_create_cinema.CreateCinemaActivity;
import com.finalproject.ui.owner.activity_home.OwnerHomeActivity;
import com.finalproject.ui.user.activity_home.HomeActivity;

public class LoginActivity extends BaseActivity {
    private ActivityLoginBinding binding;
    private ActivityLoginMvvm mvvm;
    private LoginModel loginModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        initView();
    }

    private void initView() {
        mvvm = ViewModelProviders.of(this).get(ActivityLoginMvvm.class);
        loginModel = new LoginModel();
        binding.setModel(loginModel);

        mvvm.getOnLoginSuccess().observe(this, userModel -> {
            setUserModel(userModel);
            if (userModel.getData().getType().equals("customer")){
                navigateToUserHome();
            }else if (userModel.getData().getType().equals("owner")){
                if (userModel.getData().getCinema()!=null){
                    navigateToOwnerHome();
                }else {
                    navigateToCreateCinema();
                }

            }


        });

        binding.btnLogin.setOnClickListener(view -> {
            Common.CloseKeyBoard(this, binding.edUserName);
            Common.CloseKeyBoard(this, binding.edPassword);
            if (loginModel.isDataValid(this)) {
                    mvvm.login(this, loginModel);
                } else {
                    Toast.makeText(this, R.string.please_choose_the_user, Toast.LENGTH_SHORT).show();
                }

        });
        binding.txtCreate.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
        });

//        binding.forgetPass.setOnClickListener(view -> {
//            Intent intent = new Intent(LoginActivity.this, ForgetPasswordActivity1.class);
//            startActivity(intent);
//
//        });

    }

    private void navigateToCreateCinema() {
        Intent intent=new Intent(this, CreateCinemaActivity.class);
        startActivity(intent);
        finish();
    }

    private void navigateToOwnerHome() {
        Intent intent = new Intent(LoginActivity.this, OwnerHomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void navigateToUserHome() {
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

}