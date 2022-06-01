package com.finalproject.ui.common_uis.forgetPassword;

import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;

import com.finalproject.R;
import com.finalproject.databinding.ActivityForgetPass2Binding;
import com.finalproject.ui.common_uis.activity_base.BaseActivity;

import io.paperdb.Paper;

public class ForgetPassActivity2 extends BaseActivity {
    ActivityForgetPass2Binding binding;
    private String lang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_forget_pass2);
        initView();
    }

    private void initView() {
        Paper.init(this);
        lang = getLang();
        binding.setLang(getLang());

        binding.llPrevious.setOnClickListener(view -> {
            Intent intent = new Intent(ForgetPassActivity2.this, ForgetPasswordActivity1.class);
            startActivity(intent);
            finish();
        });

        binding.llnext.setOnClickListener(view -> {
                Intent intent = new Intent(ForgetPassActivity2.this, ForgetPassActivity3.class);
                startActivity(intent);
                finish();

        });
    }

}