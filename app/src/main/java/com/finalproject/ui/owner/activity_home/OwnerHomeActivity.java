package com.finalproject.ui.owner.activity_home;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;

import android.os.Handler;


import com.finalproject.R;
import com.finalproject.databinding.ActivityOwnerHomeBinding;
import com.finalproject.language.Language;
import com.finalproject.ui.common_uis.activity_base.BaseActivity;

import io.paperdb.Paper;

public class OwnerHomeActivity extends BaseActivity {
    private ActivityOwnerHomeBinding binding;
    private NavController navController;
    private int req;
    private ActivityResultLauncher<Intent> launcher;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_owner_home);
        initView();
    }


    private void initView() {
//        Log.e("cinema",getUserModel().getData().getCinema().getId());
        Paper.init(this);
        binding.setLang(getLang());
        setSupportActionBar(binding.toolBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        navController = Navigation.findNavController(this, R.id.navHostFragment);
        NavigationUI.setupWithNavController(binding.toolBar, navController);
        NavigationUI.setupActionBarWithNavController(this, navController);
        NavigationUI.setupWithNavController(binding.bottomNav, navController);

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (binding.toolBar.getNavigationIcon() != null) {
                binding.toolBar.getNavigationIcon().setColorFilter(ContextCompat.getColor(OwnerHomeActivity.this, R.color.white), PorterDuff.Mode.SRC_ATOP);

            }
        });

        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (req==1&&result.getResultCode()==RESULT_OK&&result.getData()!=null){
                String lang = result.getData().getStringExtra("lang");
                refreshActivity(lang);
            }
        });

        binding.setLang(getLang());


    }
    @Override
    public void onBackPressed() {
        int currentFragmentId = navController.getCurrentDestination().getId();
        if (currentFragmentId == R.id.movies) {
            finish();

        } else {
            navController.popBackStack();
        }

    }


    public void refreshActivity(String lang) {
        Paper.book().write("lang", lang);
        Language.setNewLocale(this, lang);
        new Handler()
                .postDelayed(() -> {

                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }, 500);


    }


}