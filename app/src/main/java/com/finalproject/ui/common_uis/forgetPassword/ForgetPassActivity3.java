package com.finalproject.ui.common_uis.forgetPassword;

import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;

import com.finalproject.R;
import com.finalproject.databinding.ActivityForgetPass3Binding;
import com.finalproject.ui.common_uis.activity_base.BaseActivity;
import com.finalproject.ui.user.activity_home.HomeActivity;

import io.paperdb.Paper;

public class ForgetPassActivity3 extends BaseActivity {
    ActivityForgetPass3Binding binding;
    String lang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_forget_pass3);
        initView();
    }

    private void initView() {
        Paper.init(this);
        lang = getLang();
        binding.setLang(getLang());

        binding.llPrevious.setOnClickListener(view -> {
            Intent intent=new Intent(ForgetPassActivity3.this, ForgetPassActivity2.class);
            startActivity(intent);
            finish();
        });

        binding.llconfirm.setOnClickListener(view -> {
                Intent intent = new Intent(ForgetPassActivity3.this, HomeActivity.class);
                startActivity(intent);
                finish();

        });
    }

}