package com.finalproject.ui.user.activity_home;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.finalproject.R;
import com.finalproject.databinding.ActivityHomeBinding;
import com.finalproject.language.Language;
import com.finalproject.ui.common_uis.activity_base.BaseActivity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import io.paperdb.Paper;


public class HomeActivity extends BaseActivity {
    private String lang;
    private ActivityHomeBinding binding;
    private NavController navController;
    private ActivityResultLauncher<Intent> launcher;
    private int req;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //anguage.setLocale(this, Language.getLanguageSelected(this));

        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        initView();


    }

    private void initView() {
//        Log.e("reqq",req+"");
        Paper.init(this);
        lang = getLang();
        binding.setLang(getLang());
        setSupportActionBar(binding.toolBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        navController = Navigation.findNavController(this, R.id.navHostFragment);
        NavigationUI.setupWithNavController(binding.toolBar, navController);
        NavigationUI.setupActionBarWithNavController(this, navController);
        NavigationUI.setupWithNavController(binding.bottomNav, navController);

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (binding.toolBar.getNavigationIcon() != null) {
                binding.toolBar.getNavigationIcon().setColorFilter(ContextCompat.getColor(HomeActivity.this, R.color.white), PorterDuff.Mode.SRC_ATOP);

            }
        });

        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (req == 1 && result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                navigateToHistory();
            }
        });
    }

    public void navigateToHistory() {
        //Navigation.findNavController(binding.getRoot()).navigate(R.id.history);
        binding.bottomNav.setSelectedItemId(R.id.history);
    }


    @Override
    public void onBackPressed() {
        int currentFragmentId = navController.getCurrentDestination().getId();
        if (currentFragmentId == R.id.home) {
            finish();

        } else {
            navController.popBackStack();
        }

    }

    public void refreshActivity(String lang) {
//        Log.e("lang", lang);
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