package com.finalproject.ui.user.activity_home.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.finalproject.R;
import com.finalproject.databinding.FragmentProfileBinding;
import com.finalproject.language.Language;
import com.finalproject.model.UserModel;
import com.finalproject.mvvm.FragmentProfileMvvm;
import com.finalproject.preferences.Preferences;
import com.finalproject.tags.Tags;
import com.finalproject.ui.common_uis.activity_base.BaseFragment;
import com.finalproject.ui.common_uis.activity_edit_account.EditAccountActivity;
import com.finalproject.ui.common_uis.activity_contact_us.ContactUsActivity;
import com.finalproject.ui.user.activity_home.HomeActivity;
import com.finalproject.ui.common_uis.activity_login.LoginActivity;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.squareup.picasso.Picasso;


public class FragmentProfile extends BaseFragment {
    private HomeActivity activity;
    private FragmentProfileBinding binding;
    private ActivityResultLauncher<Intent> launcher;
    private int req = 1;
    private Preferences preferences;
    private UserModel userModel;
    private FragmentProfileMvvm mvvm;
    private BottomSheetBehavior behavior;

    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (HomeActivity) context;
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (req == 1 && result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                String lang = result.getData().getStringExtra("lang");
                activity.refreshActivity(lang);
            }else if (req == 2 && result.getResultCode() == Activity.RESULT_OK){
                userModel = getUserModel();
                if (userModel.getData().getImage() != null) {
                    Picasso.get().load(Tags.base_url + userModel.getData().getImage()).into(binding.image);
                }
                binding.setModel(userModel);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        behavior = BottomSheetBehavior.from(binding.sheet.root);
        preferences = Preferences.getInstance();
        userModel = getUserModel();
        mvvm = ViewModelProviders.of(this).get(FragmentProfileMvvm.class);

        if (userModel != null) {
            if (userModel.getData().getImage() != null) {
                String url = userModel.getData().getImage();
                Picasso.get().load(Uri.parse(Tags.base_url + url)).into(binding.image);
                binding.setModel(userModel);
            }
            binding.setModel(userModel);
        }

        if (userModel==null){
            binding.tvName.setOnClickListener(view -> navigateToLoginActivity());
        }


        binding.llEditAccount.setOnClickListener(view -> {
            if (userModel == null) {
                navigateToLoginActivity();
            } else {
                navigateToEditProfileActivity();
            }

        });
        binding.llContactUs.setOnClickListener(view -> {
            Intent intent1 = new Intent(activity, ContactUsActivity.class);
            startActivity(intent1);
        });
        binding.llLanguage.setOnClickListener(view -> {

            if (getLang().equals("en")) {
                activity.refreshActivity("ar");
            } else {
                activity.refreshActivity("en");
            }
        });
        mvvm.logout.observe(activity, aBoolean -> {
            if (aBoolean) {
                logout();
            }
        });
        mvvm.delete.observe(activity, aBoolean -> {
            if (aBoolean) {
                logout();
            }
        });
        binding.langName.setText(Language.getLanguageSelected(requireContext()));
        binding.llLogOut.setOnClickListener(view -> {
            if (userModel == null) {
                logout();
            } else {
                mvvm.logout(activity, userModel);
            }
        });
        binding.llDelete.setOnClickListener(view -> openSheet());
    }

    private void openSheet() {
        binding.sheet.btnYes.setOnClickListener(view -> {
            if (userModel == null) {
                logout();
            } else {
                mvvm.delete(activity, userModel);
            }
        });
        binding.sheet.btnNo.setOnClickListener(view -> behavior.setState(BottomSheetBehavior.STATE_COLLAPSED));

        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private void logout() {
        navigateToLoginActivity();
        clearUserModel(activity);
        userModel = getUserModel();
        binding.setModel(null);
    }

    private void navigateToLoginActivity() {
        Intent intent = new Intent(activity, LoginActivity.class);
        launcher.launch(intent);
        activity.finish();

    }
    private void navigateToEditProfileActivity(){
        req=2;
        Intent intent = new Intent(activity, EditAccountActivity.class);
        launcher.launch(intent);
    }
}